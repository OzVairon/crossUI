/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.centergame.starttrack.api.model;

/**
 *
 * @author idp
 */
public class Grade {
	
	public int participant;
	public int indicator;
	public Integer grade;
	
	
	public Grade(int participant, int indicator) {
		this.participant = participant;
		this.indicator = indicator;
	}
}
