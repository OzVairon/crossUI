package org.ozv.crossUI.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.idp.engine.App;

import org.ozv.crossUI.StartTrackApp;
import org.ozv.crossUI.api.model.Game;
import org.ozv.crossUI.api.model.Report;
import org.ozv.crossUI.api.model.Team;
import org.ozv.crossUI.graphics.starttrack_widgets.TeamWidget;
import org.ozv.crossUI.graphics.starttrack_widgets.base.IconButton;
import org.ozv.crossUI.screens.base.StartTrackBaseScreen;

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
		for (Report r : StartTrackApp.getState().reports) {
			if (r.game_module == StartTrackApp.getState().gameModule.id && r.sent) {
				TeamWidget w = teamWidgets.get(r.team);
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
