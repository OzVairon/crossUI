package org.ozv.crossUI;

import com.idp.engine.App;
import com.idp.engine.base.AppUtils;

import org.ozv.crossUI.api.StartTrackApi;
import org.ozv.crossUI.api.model.Game;
import org.ozv.crossUI.api.model.GameModule;
import org.ozv.crossUI.api.model.Grade;
import org.ozv.crossUI.api.model.Indicator;
import org.ozv.crossUI.api.model.Participant;
import org.ozv.crossUI.api.model.Report;
import org.ozv.crossUI.api.model.Team;
import org.ozv.crossUI.screens.LoginScreen;
import org.ozv.crossUI.screens.ModulesScreen;

import java.util.ArrayList;

/**
 * Mobile app for Startrack project in centergame.
 *
 * @author idp
 */
public class StartTrackApp extends App {


	public static class State {
		public Game game;
		public GameModule gameModule;
		public Team team;
		public Participant participant;
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
		AppUtils.files.writeLocalJson("state", getInstance().state);
	}

	public static void loadState() {
		try {
			getInstance().state = AppUtils.files.readLocalJson("state", State.class);
		} catch (Exception e) {
			getInstance().state = new State();
		}
	}

	public void deleteState() {
		AppUtils.files.local("state").delete();
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
			token = AppUtils.files.readLocalString("token");
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

		AppUtils.files.writeLocalString("token", token);
		StartTrackApi.setPrivateToken(token);

		setCurrentScreen(new ModulesScreen());
		AppUtils.input.setCatchBackKey(true);
	}

	/**
	 * Logs out from user account and deletes private token.
	 */
	public void logOut() {
		AppUtils.files.local("token").delete();
		deleteState();
		setCurrentScreen(new LoginScreen());
	}

	/**
	 * @return current app instance
	 */
	public static StartTrackApp getInstance() {
		return (StartTrackApp) instance;
	}


}
