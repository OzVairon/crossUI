/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ozv.crossUI.graphics.starttrack_widgets;

import com.badlogic.gdx.Gdx;
import com.idp.engine.ui.graphics.actors.Text;

import org.ozv.crossUI.StartTrackApp;
import org.ozv.crossUI.api.model.Indicator;
import org.ozv.crossUI.graphics.starttrack_widgets.base.BinaryRadioGroup;

/**
 * StartTrackWidget representing a module.
 *
 * @author idp
 */
public class IndicatorWidget extends StartTrackWidget<Indicator> {

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
