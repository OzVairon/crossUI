/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.centergame.starttrack.api.model;

import java.util.ArrayList;

/**
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class Game {
    
    public ArrayList<Participant> participants;
	public ArrayList<com.centergame.starttrack.api.model.GameModule> game_modules;
	public ArrayList<Team> teams;
	public boolean team_grade_required;
	
	
	public Game() {
		this.participants = new ArrayList<Participant>();
		this.game_modules = new ArrayList<com.centergame.starttrack.api.model.GameModule>();
		this.teams = new ArrayList<Team>();
	}

	public Team getTeamByID(int id) {
		for (Team t : teams) {
			if (t.id == id) return t;
		}
		return null;
	}
    
}
