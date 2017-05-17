package com.ozv.starttrack;

import com.ozv.starttrack.api.StartTrackApi;
import com.ozv.starttrack.screens.LoginScreen;
import com.idp.engine.App;
import com.idp.engine.base.Idp;
import com.ozv.starttrack.api.model.GameModule;
import com.ozv.starttrack.api.model.Report;
import com.ozv.starttrack.screens.ModulesScreen;

import java.util.ArrayList;

/**
 * Mobile app for Startrack project in centergame.
 *
 * @author idp
 */
public class StartTrackApp extends App {


	public static class State {
		public com.ozv.starttrack.api.model.Game game;
		public GameModule gameModule;
		public com.ozv.starttrack.api.model.Team team;
		public com.ozv.starttrack.api.model.Participant participant;
		public Report report;
		public ArrayList<Report> reports = new ArrayList<Report>();
	}

	private State state;

	public static State getState() {
		if (getInstance().state == null) getInstance().state = new State();
		return getInstance().state;
	}

	public Report getReport() {
		for (Report r : state.reports) {
			if (r.game_module == state.gameModule.id && r.team == state.team.id)
				return r;
		}
		Report r = new Report(state.gameModule.id, state.team.id);
		state.reports.add(r);
		return r;
	}

	public com.ozv.starttrack.api.model.Grade getGrade(com.ozv.starttrack.api.model.Indicator indicator) {
		for (com.ozv.starttrack.api.model.Grade g : state.report.grades) {
			if (g.indicator == indicator.id && g.participant == state.participant.id)
				return g;
		}
		com.ozv.starttrack.api.model.Grade g = new com.ozv.starttrack.api.model.Grade(state.participant.id, indicator.id);
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
	 * {@link ProjectsScreen} on success. Switches to {@link LoginScreen} if
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
		StartTrackApi.setPrivateToken(token);

		showScreen(new ModulesScreen());
		Idp.input.setCatchBackKey(true);
	}

	/**
	 * Logs out from user account and deletes private token.
	 */
	public void logOut() {
		Idp.files.local("token").delete();
		deleteState();
		showScreen(new LoginScreen());
	}

	/**
	 * @return current app instance
	 */
	public static StartTrackApp getInstance() {
		return (StartTrackApp) instance;
	}
}
