/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozv.starttrack.api.model;

import java.util.ArrayList;

/**
 *
 * @author idp
 */
public class Report {
	
	public int game_module;
	public int team;
	public Integer team_grade;
	public ArrayList<Grade> grades;
	
	
	public Report(int gameModule, int team) {
		this.game_module = gameModule;
		this.team = team;
		this.grades = new ArrayList<Grade>();
	}

	
	public boolean sent;

    public boolean isParticipantGraded(int id) {
        for (Grade g : grades) {
            if (g.participant == id) return true;
        }
        return false;

    }

}
