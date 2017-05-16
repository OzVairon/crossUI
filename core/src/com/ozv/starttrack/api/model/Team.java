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
public class Team {
	
	public int id;
	public int number;
	public ArrayList<Participant> participants;
	
	
	public Team() {
		this.participants = new ArrayList<Participant>();
	}

	public boolean isMember(int id) {
		for (Participant p : participants) {
			if (p.id == id) return true;
		}
		return false;
	}
}
