package com.ozv.crossui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.idp.engine.App;
import com.ozv.crossui.api.model.Grade;
import com.ozv.crossui.api.model.Participant;
import com.ozv.crossui.screens.base.StartTrackBaseScreen;
import com.ozv.crossui.StartTrackApp;
import com.ozv.crossui.api.model.Team;
import com.ozv.crossui.graphics.starttrack_widgets.TeamGradeWidget;
import com.ozv.crossui.graphics.starttrack_widgets.base.FloatingIconButton;
import com.idp.engine.net.IdpJsonListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.tomgrill.gdxdialogs.core.dialogs.GDXProgressDialog;

/**
 * Start screen of Startrack app.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class TeamReportScreen extends StartTrackBaseScreen<Team> {

	private final HashMap<Integer, com.ozv.crossui.graphics.starttrack_widgets.ParticipantWidget> widgets;
	
	
	public TeamReportScreen(com.ozv.crossui.api.model.Team team) {
		super("Команда " + team.number);
		this.data = team;
		this.widgets = new HashMap<Integer, com.ozv.crossui.graphics.starttrack_widgets.ParticipantWidget>();
	}


	@Override
	protected void initWidgets() {
		widgets.clear();
		final com.ozv.crossui.api.model.Report r = StartTrackApp.getInstance().getReport();
		//if (StartTrackApp.getState().game.team_grade_required) {
			listView.getContent().addActor(new TeamGradeWidget(r));
		//}

		for (final Participant p : data.participants) {
			com.ozv.crossui.graphics.starttrack_widgets.ParticipantWidget widget = new com.ozv.crossui.graphics.starttrack_widgets.ParticipantWidget(p);
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

                com.ozv.crossui.api.model.Report r = StartTrackApp.getInstance().getReport();

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

                    getConfirmationDialog("Вы точно хотите отправить отчет?", message, new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            final com.ozv.crossui.api.model.Report r = StartTrackApp.getInstance().getReport();
                            System.out.println(r.team_grade);
                            Iterator<Grade> it = r.grades.iterator();
                            while (it.hasNext()) {
                                Grade g = it.next();
                                if (g.grade == null)
                                    it.remove();
                            }

                            final GDXProgressDialog d = getPopupLayer().getProgressDialog("Подождите", "Идет отправка отчета на сервер");

                            com.ozv.crossui.api.StartTrackApi.postReport(r, new IdpJsonListener() {

                                @Override
                                public void loaded(String json, Map<String, List<String>> headers) {
                                    r.sent = true;
                                    StartTrackApp.saveState();
                                    d.dismiss();
                                    App.getInstance().backScreen();
                                }

                                @Override
                                public void failed(Throwable t) {
                                    System.out.println("Sending report is failed");
                                    d.dismiss();

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

        com.ozv.crossui.api.model.Report report = StartTrackApp.getInstance().getReport();

        com.ozv.crossui.api.model.Team t = StartTrackApp.getState().game.getTeamByID(report.team);


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
		for (com.ozv.crossui.graphics.starttrack_widgets.ParticipantWidget w : widgets.values()) {
			w.setBackgroundColor(App.Colors.WIDGET_WHITE);
		}

        HashSet<Integer> estimated = findEstimatedParticipants();
        for (Integer i : estimated) {
            com.ozv.crossui.graphics.starttrack_widgets.ParticipantWidget widget = widgets.get(i);
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