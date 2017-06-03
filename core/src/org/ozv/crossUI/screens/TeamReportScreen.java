package org.ozv.crossUI.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.idp.engine.App;
import com.idp.engine.net.JsonListener;

import org.ozv.crossUI.StartTrackApp;
import org.ozv.crossUI.api.StartTrackApi;
import org.ozv.crossUI.api.model.Grade;
import org.ozv.crossUI.api.model.Participant;
import org.ozv.crossUI.api.model.Report;
import org.ozv.crossUI.api.model.Team;
import org.ozv.crossUI.graphics.starttrack_widgets.ParticipantWidget;
import org.ozv.crossUI.graphics.starttrack_widgets.TeamGradeWidget;
import org.ozv.crossUI.graphics.starttrack_widgets.base.FloatingIconButton;
import org.ozv.crossUI.screens.base.StartTrackBaseScreen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Start screen of Startrack app.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class TeamReportScreen extends StartTrackBaseScreen<Team> {

	private final HashMap<Integer, ParticipantWidget> widgets;
	
	
	public TeamReportScreen(Team team) {
		super("Команда " + team.number);
		this.data = team;
		this.widgets = new HashMap<Integer, ParticipantWidget>();
	}


	@Override
	protected void initWidgets() {
		widgets.clear();
		final Report r = StartTrackApp.getInstance().getReport();
		//if (StartTrackApp.getState().game.team_grade_required) {
			listView.getContent().addActor(new TeamGradeWidget(r));
		//}

		for (final Participant p : data.participants) {
			ParticipantWidget widget = new ParticipantWidget(p);
			widget.addListener(new ActorGestureListener() {
				@Override
				public void tap(InputEvent event, float x, float y, int count, int button) {
					StartTrackApp app = StartTrackApp.getInstance();
					StartTrackApp.getState().participant = p;
					StartTrackApp.getState().report = r;
					App.pushScreen(new IndicatorsScreen(p));
				}
			});
			listView.getContent().addActor(widget);
			widgets.put(p.id, widget);
		}
        initFloatingButtons();
	}


    private void initFloatingButtons() {
        final FloatingIconButton sendButton = new FloatingIconButton("forward", 60);

        sendButton.setPadding(StartTrackApp.dp2px(20));
        sendButton.setBackColor(App.Colors.MAIN);
        sendButton.setIconColor(App.Colors.TEXT_NAVBAR);
        getMainLayer().content.addActor(sendButton);
        sendButton.setPosition(
                getMainLayer().content.getWidth() - sendButton.getWidth() - StartTrackApp.dp2px(16),
                getMainLayer().content.getHeight() - sendButton.getHeight() - StartTrackApp.dp2px(16)
        );

        sendButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {

                Report r = StartTrackApp.getInstance().getReport();

                StartTrackApp.State state = StartTrackApp.getState();

                boolean tgr = state.game.team_grade_required;



                if (tgr && r.team_grade == null) {
                    getPopupLayer().getAlertDialog("Отчет отклонен", "Вы не поставили командную оценку");
                } else {
                    int ungradedCounter = state.game.getTeamByID(r.team).participants.size() - findEstimatedParticipants().size();
                    String message = "";

                    if (ungradedCounter != 0) {
                        message = "Вы не оценили " + ungradedCounter;
                        if (ungradedCounter % 10 == 1 && ungradedCounter/10 != 1) message += " участника";
                        else message += " участников";
                    } else {
                        message += "\nОтчет будет сохранен";
                    }

                    getPopupLayer().getConfirmationDialog("Вы точно хотите отправить отчет?", message, new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            final Report r = StartTrackApp.getInstance().getReport();
                            System.out.println(r.team_grade);
                            Iterator<Grade> it = r.grades.iterator();
                            while (it.hasNext()) {
                                Grade g = it.next();
                                if (g.grade == null)
                                    it.remove();
                            }

                            getPopupLayer().getProgressDialog("Подождите", "Идет отправка отчета на сервер");

                            StartTrackApi.postReport(r, new JsonListener() {

                                @Override
                                public void loaded(String json, Map<String, List<String>> headers) {
                                    r.sent = true;
                                    StartTrackApp.saveState();
                                    getPopupLayer().dismissProgressDialog();
                                    App.backScreen();
                                }

                                @Override
                                public void failed(Throwable t) {
                                    System.out.println("Sending report is failed");
                                    getPopupLayer().dismissProgressDialog();

                                    String errorMessage = "Нет соединения с сервером";

                                    System.out.println("Sending report is failed");
                                    if (t.getMessage().equals("{\"detail\":\"User inactive or deleted.\"}")) {
                                        errorMessage = "Данная игра уже завершена\nОтчеты экспертов не принимаются";
                                    }
                                    final String finalErrorMessage = errorMessage;
                                    Gdx.app.postRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            App.getCurrentScreen().getPopupLayer().getAlertDialog("Ошибка отправки отчета", finalErrorMessage);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }



            }
        });
    }

	
	@Override
	protected void loadData() {
		dataLoaded(this.data);
	}

    private HashSet<Integer> findEstimatedParticipants() {
        HashSet<Integer> estimated = new HashSet<Integer>();

        Report report = StartTrackApp.getInstance().getReport();

        Team t = StartTrackApp.getState().game.getTeamByID(report.team);


        for (Grade g : report.grades) {
            if (g.grade != null && t.isMember(g.participant)) {
                estimated.add(g.participant);
            }
        }

        return estimated;
    }

	@Override
	public void show() {
		super.show();
		for (ParticipantWidget w : widgets.values()) {
			w.setBackgroundColor(App.Colors.WIDGET_WHITE);
		}

        HashSet<Integer> estimated = findEstimatedParticipants();
        for (Integer i : estimated) {
            ParticipantWidget widget = widgets.get(i);
            if (widget != null) {
                widget.setBackgroundColor(App.Colors.getColorByName("ELEMENT_BACK_SELECTED"));
            }
        }

	}

	@Override
	public void pause() {
		StartTrackApp.saveState();
	}

	@Override
	public void hide() {
		StartTrackApp.saveState();
	}
	
}
