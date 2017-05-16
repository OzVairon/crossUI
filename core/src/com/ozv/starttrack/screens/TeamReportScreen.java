package com.ozv.starttrack.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ozv.starttrack.StartTrackApp;
import com.ozv.starttrack.api.StartTrackApi;
import com.ozv.starttrack.api.model.Report;
import com.ozv.starttrack.api.model.Team;
import com.ozv.starttrack.graphics.starttrack_widgets.ParticipantWidget;
import com.ozv.starttrack.graphics.starttrack_widgets.TeamGradeWidget;
import com.ozv.starttrack.graphics.starttrack_widgets.base.FloatingIconButton;
import com.idp.engine.net.IdpJsonListener;
import com.idp.engine.ui.screens.IdpAppScreen;

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
public class TeamReportScreen extends com.ozv.starttrack.screens.base.StartTrackBaseScreen<Team> {

	private final HashMap<Integer, ParticipantWidget> widgets;
	
	
	public TeamReportScreen(com.ozv.starttrack.api.model.Team team) {
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

		for (final com.ozv.starttrack.api.model.Participant p : data.participants) {
			ParticipantWidget widget = new ParticipantWidget(p);
			widget.addListener(new ActorGestureListener() {
				@Override
				public void tap(InputEvent event, float x, float y, int count, int button) {
					StartTrackApp app = StartTrackApp.getInstance();
					StartTrackApp.getState().participant = p;
					StartTrackApp.getState().report = r;
					app.pushScreen(new IndicatorsScreen(p));
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
        sendButton.setBackColor(StartTrackApp.ColorPallete.MAIN);
        sendButton.setIconColor(StartTrackApp.ColorPallete.TEXT_NAVBAR);
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

                    getConfirmationDialog("Вы точно хотите отправить отчет?", message, new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            final Report r = StartTrackApp.getInstance().getReport();
                            System.out.println(r.team_grade);
                            Iterator<com.ozv.starttrack.api.model.Grade> it = r.grades.iterator();
                            while (it.hasNext()) {
                                com.ozv.starttrack.api.model.Grade g = it.next();
                                if (g.grade == null)
                                    it.remove();
                            }

                            final GDXProgressDialog d = getPopupLayer().getProgressDialog("Подождите", "Идет отправка отчета на сервер");

                            StartTrackApi.postReport(r, new IdpJsonListener() {

                                @Override
                                public void loaded(String json, Map<String, List<String>> headers) {
                                    r.sent = true;
                                    StartTrackApp.saveState();
                                    d.dismiss();
                                    StartTrackApp.getInstance().popScreen();
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
                                            ((IdpAppScreen)(StartTrackApp.getInstance().getScreen())).getPopupLayer().getAlertDialog("Ошибка отправки отчета", finalErrorMessage);
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

        com.ozv.starttrack.api.model.Team t = StartTrackApp.getState().game.getTeamByID(report.team);


        for (com.ozv.starttrack.api.model.Grade g : report.grades) {
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
			w.setBackgroundColor(StartTrackApp.ColorPallete.ELEMENT_BACK);
		}

        HashSet<Integer> estimated = findEstimatedParticipants();
        for (Integer i : estimated) {
            ParticipantWidget widget = widgets.get(i);
            if (widget != null) {
                widget.setBackgroundColor(StartTrackApp.ColorPallete.ELEMENT_BACK_SELECTED);
            }
        }

//		for (Grade g : StartTrackApp.getInstance().getReport().grades) {
//            if (g.grade != null) {
//				ParticipantWidget widget = widgets.get(g.participant);
//				if (widget != null) {
//					widget.setBackgroundColor(StartTrackApp.ColorPallete.ELEMENT_BACK_SELECTED);
//				}
//			}
//		}
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
