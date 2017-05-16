/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozv.starttrack.graphics.starttrack_widgets;

import com.badlogic.gdx.Gdx;
import com.ozv.starttrack.StartTrackApp;
import com.ozv.starttrack.api.model.Indicator;
import com.ozv.starttrack.graphics.starttrack_widgets.base.BinaryRadioGroup;
import com.idp.engine.ui.graphics.actors.Text;

/**
 * Widget representing a module.
 *
 * @author idp
 */
public class IndicatorWidget extends Widget<Indicator> {

	public IndicatorWidget(Indicator data) {
		super(data);
	}
	
	
	protected void init() {
		setWidth(Gdx.graphics.getWidth());
		
		layout.paddingTop = lp;
		layout.paddingBottom = lp;
		Text title = new Text(data.title, StartTrackApp.getResources().getLabelStyle("h1"));
		layout.addActor(title);
		title.setWidth(getWidth() - StartTrackApp.dp2px(40));
		title.setWrap(true);
		
		BinaryRadioGroup rg = new BinaryRadioGroup(StartTrackApp.getInstance().getGrade(data));
		rg.paddingTop = sp;
		rg.setWidth(getWidth() - layout.paddingLeft - layout.paddingRight);
		rg.layout();
		layout.addActor(rg);
	}
}
