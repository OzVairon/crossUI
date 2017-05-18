package com.ozv.crossui.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.idp.engine.App;
import com.ozv.crossui.StartTrackApp;
import com.ozv.crossui.graphics.starttrack_widgets.base.IconButton;
import com.ozv.crossui.api.model.Game;

import java.util.HashMap;

/**
 * Start screen of Startrack app.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class TeamScreen extends com.ozv.crossui.screens.base.StartTrackBaseScreen<Game> {

	private final HashMap<Integer, com.ozv.crossui.graphics.starttrack_widgets.TeamWidget> teamWidgets;
	
	
	public TeamScreen() {
		super("Команды");
		this.teamWidgets = new HashMap<Integer, com.ozv.crossui.graphics.starttrack_widgets.TeamWidget>();
	}


	@Override
	protected void initWidgets() {
		teamWidgets.clear();
		for (final com.ozv.crossui.api.model.Team t : StartTrackApp.getState().game.teams) {
			com.ozv.crossui.graphics.starttrack_widgets.TeamWidget rect = new com.ozv.crossui.graphics.starttrack_widgets.TeamWidget(t);
			rect.addListener(new ActorGestureListener() {
				@Override
				public void tap(InputEvent event, float x, float y, int count, int button) {
					StartTrackApp.getState().team = t;
					App.pushScreen(new TeamReportScreen(t));
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
		for (com.ozv.crossui.api.model.Report r : StartTrackApp.getState().reports) {
			if (r.game_module == StartTrackApp.getState().gameModule.id && r.sent) {
				com.ozv.crossui.graphics.starttrack_widgets.TeamWidget w = teamWidgets.get(r.team);
				if (w != null) {
					w.setBackgroundColor(App.Colors.getColorByName("ELEMENT_BACK_SELECTED"));
					IconButton a = new IconButton("accept", 40);
					a.getIcon().setColor(App.Colors.getColorByName("ICON_TICK"));
					a.setPosition(w.getWidth() - a.getWidth(), w.getHeight() / 2 - a.getHeight() / 2);
					w.addActor(a);
				}
			}
		}
	}
	
}
