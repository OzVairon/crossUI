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
public class GameModule {
	
	public int id;
	public String title;
	public ArrayList<Indicator> indicators;
	
	public GameModule() {
		this.indicators = new ArrayList<Indicator>();
	}
	
}
