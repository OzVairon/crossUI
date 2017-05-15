package com.centergame.starttrack;

import com.badlogic.gdx.graphics.Color;
import com.centergame.starttrack.api.StartTrackApi;
import com.centergame.starttrack.screens.ModulesScreen;
import com.centergame.starttrack.api.model.Game;
import com.centergame.starttrack.api.model.Grade;
import com.centergame.starttrack.api.model.Indicator;
import com.centergame.starttrack.api.model.Participant;
import com.centergame.starttrack.api.model.Team;
import com.centergame.starttrack.screens.LoginScreen;
import com.idp.engine.App;
import com.idp.engine.ui.screens.TransitionManager;
import com.idp.engine.base.Idp;
import com.idp.engine.resources.Resources;

import java.util.ArrayList;

/**
 * Mobile app for Startrack project in centergame.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class StartTrackApp extends App {


	public static class State {
		public Game game;
		public com.centergame.starttrack.api.model.GameModule gameModule;
		public Team team;
		public Participant participant;
		public com.centergame.starttrack.api.model.Report report;
		public ArrayList<com.centergame.starttrack.api.model.Report> reports = new ArrayList<com.centergame.starttrack.api.model.Report>();
	}

	private State state;

	public static State getState() {
		if (getInstance().state == null) getInstance().state = new State();
		return getInstance().state;
	}

	public com.centergame.starttrack.api.model.Report getReport() {
		for (com.centergame.starttrack.api.model.Report r : state.reports) {
			if (r.game_module == state.gameModule.id && r.team == state.team.id)
				return r;
		}
		com.centergame.starttrack.api.model.Report r = new com.centergame.starttrack.api.model.Report(state.gameModule.id, state.team.id);
		state.reports.add(r);
		return r;
	}

	public Grade getGrade(Indicator indicator) {
		for (Grade g : state.report.grades) {
			if (g.indicator == indicator.id && g.participant == state.participant.id)
				return g;
		}
		Grade g = new Grade(state.participant.id, indicator.id);
		state.report.grades.add(g);
		return g;
	}

	public static void saveState() {
		Idp.files.writeLocalJson("state", getInstance().state);
	}

	public static void loadState() {
		try {
			getInstance().state = Idp.files.readLocalJson("state", State.class);
		} catch (Exception e) {
			getInstance().state = new State();
		}
	}

	public void deleteState() {
		Idp.files.local("state").delete();
		state = null;
	}


	@Override
	public void create() {
		super.create();

		Resources r = new Resources();
		this.resources = r;
		r.enqueueAll();
		r.awaitLoad();

		transitionManager = new TransitionManager();

		setGLColor(Color.valueOf("006FC0"));
		loadState();
		logIn();

	}

	private void logIn() {
		String token = null;
		try {
			token = Idp.files.readLocalString("token");
		}
		catch (Exception ex) {}
		logIn(token);
	}

	/**
	 * Tries to log in using given private token. Switches to
	 * {@link ProjectsScreen} on success. Switches to {@link LoginScreen} if
	 * token is null.
	 *
	 * @param token
	 */
	public void logIn(String token) {
		if (token == null) {
			setScreen(new LoginScreen());
			return;
		}

		Idp.files.writeLocalString("token", token);
		StartTrackApi.setPrivateToken(token);

		setScreen(new ModulesScreen());
		Idp.input.setCatchBackKey(true);
	}

	/**
	 * Logs out from user account and deletes private token.
	 */
	public void logOut() {
		Idp.files.local("token").delete();
		deleteState();
		setScreen(new LoginScreen());
	}




	/**
	 * @return current app instance
	 */
	public static StartTrackApp getInstance() {
		return (StartTrackApp) instance;
	}


/**
	private Text fps;

	public Actor getFpsLabel() {
		if (fps == null) {
			this.fps = new Text("", StartTrackApp.getResources().getLabelStyle("debug_info")) {
				long lastUpdate;
				int lastFps;

				public void draw(Batch batch, float parentAlpha) {
					long millis = TimeUtils.millis();
					if (millis > lastUpdate + 1000) {
						lastUpdate = millis;
						int newFps = Gdx.graphics.getFramesPerSecond();
						if (newFps != lastFps) {
							setText(newFps + " fps");
							lastFps = newFps;
						}
					}
					super.draw(batch, parentAlpha);
				}
			};
			fps.setY(StartTrackApp.dp2px(28));
			fps.setX(StartTrackApp.dp2px(12));
			fps.setWidth(Gdx.graphics.getWidth() - fps.getX() * 2);
			fps.setAlignment(Align.right);
			fps.setTouchable(Touchable.disabled);
		}
		return fps;
	}

*/

	public static class ColorPallete {

		public static Color MAIN = Color.valueOf("006FC0");
		public static Color BACK = Color.valueOf("F3F3F3");
		public static Color ELEMENT_BACK = Color.valueOf("FFFFFF");
		public static Color ELEMENT_BACK_SELECTED = Color.valueOf("D6FBB1");
		public static Color ELEMENT_BORDER = Color.valueOf("e7e7e7");

		public static Color ICON_TICK = Color.valueOf("396809");
		public static Color ICON_CANCEL = Color.valueOf("FF3333");

		public static Color TEXT_MAIN = Color.valueOf("000000");
		public static Color TEXT_NAVBAR = Color.valueOf("FFFFFF");
		public static Color TEXT_HINT = Color.valueOf("666666");
		
		public static Color TEXT_NUMBER = Color.valueOf("666666");
		
		public static Color TRANSPARENT = Color.valueOf("00000000");
	}


}
