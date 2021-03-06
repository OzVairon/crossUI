/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.centergame.starttrack.graphics.starttrack_widgets;

import com.centergame.starttrack.StartTrackApp;
import com.centergame.starttrack.api.model.Team;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.Text;

/**
 * Widget representing a module.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class TeamWidget extends Widget<Team> {


	public TeamWidget(com.centergame.starttrack.api.model.Team data) {
		super(data);
	}
	
	protected void init() {
		layout.paddingTop = lp;
		layout.paddingBottom = lp;
        setPadding(StartTrackApp.dp2px(20));
		Text title = new Text("Команда " + data.number, StartTrackApp.getResources().getLabelStyle("h1"));
		title.setWrap(true);
		layout.addActor(title);
		setBorder(App.dp2px(1));
		setBorderColor(StartTrackApp.ColorPallete.ELEMENT_BORDER);
	}
}
