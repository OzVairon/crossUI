/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ozv.crossUI.api;

import com.badlogic.gdx.Net;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.idp.engine.net.JsonListener;
import com.idp.engine.net.Request;

import org.ozv.crossUI.api.model.Game;
import org.ozv.crossUI.api.model.Participant;
import org.ozv.crossUI.api.model.Report;
import org.ozv.crossUI.api.model.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main API class for TestApp.
 *
 * @author idp
 */
public final class StartTrackApi extends com.idp.engine.net.Api {

	/**
	 * Requests project statistics.
	 */
	public static Request getGame(Net.HttpResponseListener listener) {
		return request("GET", "expert-home/", null, listener);
	}

	/**
	 * Listener for  and
	 *   {@link StartTrackApi#getLocalProfile(Integer, int, HttpResponseListener)}.
	 */
	public abstract static class GameListener extends JsonListener {

		@Override
		public void loaded(String json, Map<String, List<String>> headers) {
			json = json.substring(1, json.length() - 1);  // temporary fix
			System.out.println(json);
			Game game = getGson().fromJson(json, Game.class);
			
			HashMap<Integer, Team> teams = new HashMap<Integer, Team>();
			for (Team t : game.teams) {
				teams.put(t.id, t);
			}
			
			for (Participant p : game.participants) {
				if (p.team != null)
					teams.get(p.team).participants.add(p);
			}
			
			loaded(game);
		}

		public abstract void loaded(Game game);
	}

	/**
	 * Requests project participants.
	 * @see Participant
	 * @see ParticipantListListener
	 */
	public static Request postReport(
			Report report,
			Net.HttpResponseListener listener) {

		return request("POST", "reports/", report, listener);
	}

	/**
	 * Authentication via expert's code.
	 * Once this request is complete, your should get the private token from the response
	 *   and call {@link StartTrackApi#setPrivateToken(String)}.
	 * If you miss this, all requests that require authentication will fail.
	 * @see AuthListener
	 */
	public static Request auth(String code, Net.HttpResponseListener listener) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		return request("POST", "token-auth/", params, listener);
	}

	/**
	 * Listener for {@link StartTrackApi#auth(String, String, HttpResponseListener)}.
	 */
	public abstract static class AuthListener extends JsonListener {

		@Override
		public void loaded(String json, Map<String, List<String>> headers) {
			try {
				JsonObject o = new JsonParser().parse(json).getAsJsonObject();
                String token = o.get("token").getAsString();
				if (token == null) {
//					String err = o.get("error").getAsString();
					failed(new RuntimeException("Authentication error"));//: " + err));
				}
				loaded(token);
			}
			catch (Exception ex) {
				failed(ex);
			}
		}

		public abstract void loaded(String privateToken);
	}












}
