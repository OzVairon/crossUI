package com.ozv.starttrack.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.ozv.starttrack.StartTrackApp;
import com.ozv.starttrack.api.StartTrackApi;
import com.ozv.starttrack.api.model.Game;
import com.ozv.starttrack.api.model.GameModule;
import com.ozv.starttrack.graphics.starttrack_widgets.ModuleWidget;
import com.idp.engine.App;
import com.idp.engine.ui.screens.TeamScreen;

/**
 * Start screen of Startrack app.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class ModulesScreen extends com.ozv.starttrack.screens.base.StartTrackBaseScreen<Game> {

	
	public ModulesScreen() {
		super("Список модулей");
	}


	@Override
	protected void initWidgets() {
		for (final GameModule gm : data.game_modules) {
			ModuleWidget rect = new ModuleWidget(gm);
			rect.addListener(new ActorGestureListener() {
				@Override
				public void tap(InputEvent event, float x, float y, int count, int button) {
					StartTrackApp app = StartTrackApp.getInstance();
					StartTrackApp.getState().gameModule = gm;
					app.pushScreen(new TeamScreen());
				}
			});
			listView.getContent().addActor(rect);
		}
		
		listView.getContentWrapper().setSpace(App.dp2px(8));
	}

	@Override
	protected void loadData() {
		StartTrackApi.GameListener listener = new StartTrackApi.GameListener() {
			@Override
			public void loaded(com.ozv.starttrack.api.model.Game game) {
				StartTrackApp.State s = StartTrackApp.getState();
				s.game = game;
				dataLoaded(game);
			}

			@Override
			public void failed(final Throwable t) {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						dataFailed(t, getError());
					}
				});
			}
		};
		try {
 			StartTrackApi.getGame(listener);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
