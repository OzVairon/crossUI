/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ozv.crossUI.graphics.starttrack_widgets;

import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.Text;

import org.ozv.crossUI.StartTrackApp;
import org.ozv.crossUI.api.model.Team;

/**
 * Widget representing a module.
 *
 * @author idp
 */
public class TeamWidget extends Widget<Team> {


	public TeamWidget(Team data) {
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
		setBorderColor(App.Colors.WIDGET_BORDER);
	}
}
