package com.ozv.crossui;

import com.ozv.crossui.api.model.Game;
import com.ozv.crossui.api.model.Grade;
import com.ozv.crossui.api.model.Indicator;
import com.ozv.crossui.api.model.Participant;
import com.ozv.crossui.api.model.Team;
import com.idp.engine.App;
import com.idp.engine.base.Idp;

import java.util.ArrayList;

/**
 * Mobile app for Startrack project in centergame.
 *
 * @author idp
 */
public class StartTrackApp extends App {


	public static class State {
		public Game game;
		public com.ozv.crossui.api.model.GameModule gameModule;
		public Team team;
		public Participant participant;
		public com.ozv.crossui.api.model.Report report;
		public ArrayList<com.ozv.crossui.api.model.Report> reports = new ArrayList<com.ozv.crossui.api.model.Report>();
	}

	private State state;

	public static State getState() {
		if (getInstance().state == null) getInstance().state = new State();
		return getInstance().state;
	}

	public com.ozv.crossui.api.model.Report getReport() {
		for (com.ozv.crossui.api.model.Report r : state.reports) {
			if (r.game_module == state.gameModule.id && r.team == state.team.id)
				return r;
		}
		com.ozv.crossui.api.model.Report r = new com.ozv.crossui.api.model.Report(state.gameModule.id, state.team.id);
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
		loadState();
		//setScreen(new LoginScreen());
		//logIn();
	}

	public void logIn() {
		String token = null;
		try {
			token = Idp.files.readLocalString("token");
		}
		catch (Exception ex) {}
//		System.out.println(token);
		logIn(token);
	}

	/**
	 * Tries to log in using given private token. Switches to
	 * {@link ProjectsScreen} on success. Switches to {@link com.ozv.crossui.screens.LoginScreen} if
	 * token is null.
	 *
	 * @param token
	 */
	public void logIn(String token) {
		if (token == null) {
//			setScreen(new LoginScreen());
			return;
		}

		Idp.files.writeLocalString("token", token);
		com.ozv.crossui.api.StartTrackApi.setPrivateToken(token);

		showScreen(new com.ozv.crossui.screens.ModulesScreen());
		Idp.input.setCatchBackKey(true);
	}

	/**
	 * Logs out from user account and deletes private token.
	 */
	public void logOut() {
		Idp.files.local("token").delete();
		deleteState();
		showScreen(new com.ozv.crossui.screens.LoginScreen());
	}

	/**
	 * @return current app instance
	 */
	public static StartTrackApp getInstance() {
		return (StartTrackApp) instance;
	}
}