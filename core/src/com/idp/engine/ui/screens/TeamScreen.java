package com.idp.engine.ui.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.centergame.starttrack.StartTrackApp;
import com.centergame.starttrack.graphics.starttrack_widgets.TeamWidget;
import com.centergame.starttrack.graphics.starttrack_widgets.base.IconButton;
import com.centergame.starttrack.screens.base.StartTrackBaseScreen;
import com.centergame.starttrack.api.model.Game;
import com.centergame.starttrack.api.model.Report;
import com.centergame.starttrack.api.model.Team;
import com.centergame.starttrack.screens.TeamReportScreen;

import java.util.HashMap;

/**
 * Start screen of Startrack app.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class TeamScreen extends StartTrackBaseScreen<Game> {

	private final HashMap<Integer, TeamWidget> teamWidgets;
	
	
	public TeamScreen() {
		super("Команды");
		this.teamWidgets = new HashMap<Integer, TeamWidget>();
	}


	@Override
	protected void initWidgets() {
		teamWidgets.clear();
		for (final Team t : StartTrackApp.getState().game.teams) {
			TeamWidget rect = new TeamWidget(t);
			rect.addListener(new ActorGestureListener() {
				@Override
				public void tap(InputEvent event, float x, float y, int count, int button) {
					StartTrackApp.getState().team = t;
					StartTrackApp.getInstance().pushScreen(new TeamReportScreen(t));
				}
			});
			listView.getContent().addActor(rect);
			teamWidgets.put(t.id, rect);
		}
	}

	@Override
	protected void loadData() {
		dataLoaded(this.data);
	}

	@Override
	public void show() {
		super.show();
		for (Report r : StartTrackApp.getState().reports) {
			if (r.game_module == StartTrackApp.getState().gameModule.id && r.sent) {
				TeamWidget w = teamWidgets.get(r.team);
				if (w != null) {
					w.setBackgroundColor(StartTrackApp.ColorPallete.ELEMENT_BACK_SELECTED);
					IconButton a = new IconButton("accept", 40);
					a.getIcon().setColor(StartTrackApp.ColorPallete.ICON_TICK);
					a.setPosition(w.getWidth() - a.getWidth(), w.getHeight() / 2 - a.getHeight() / 2);
					w.addActor(a);
				}
			}
		}
	}
	
}
