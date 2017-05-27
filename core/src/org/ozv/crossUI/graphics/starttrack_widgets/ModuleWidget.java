/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ozv.crossUI.graphics.starttrack_widgets;

import com.idp.engine.ui.graphics.actors.Text;

import org.ozv.crossUI.StartTrackApp;
import org.ozv.crossUI.api.model.GameModule;

/**
 * StartTrackWidget representing a module.
 *
 * @author idp
 */
public class ModuleWidget extends StartTrackWidget<GameModule> {


	public ModuleWidget(GameModule data) {
		super(data);
	}
	
	protected void init() {
		Text title = new Text(data.title, StartTrackApp.getResources().getLabelStyle("h1-bold"));

		layout.addActor(title);
        setPadding(StartTrackApp.dp2px(20));
		title.setWidth(getWidth() - StartTrackApp.dp2px(40));
		title.setWrap(true);
		String s;
		switch (data.indicators.size() % 10) {
			case 1:
				s = "индикатор";
				break;
			case 2:
			case 3:
			case 4:
				s = "индикатора";
				break;
			default:
				s = "индикаторов";
		}
		
		Text numIndicators = new Text(data.indicators.size() + " " + s, StartTrackApp.getResources().getLabelStyle("label"));
		layout.addActor(numIndicators);
	}
}
