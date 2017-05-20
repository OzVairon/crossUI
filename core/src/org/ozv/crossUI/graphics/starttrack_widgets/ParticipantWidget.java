/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozv.crossui.graphics.starttrack_widgets;

import com.badlogic.gdx.utils.Align;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Rect;
import com.idp.engine.ui.graphics.actors.Text;
import com.ozv.crossui.StartTrackApp;
import com.ozv.crossui.api.model.Participant;
import com.ozv.crossui.graphics.starttrack_widgets.base.HLayout;
import com.ozv.crossui.graphics.starttrack_widgets.base.VLayout;

/**
 * Widget representing a module.
 *
 * @author idp
 */
public class ParticipantWidget extends Widget<Participant> {


	public ParticipantWidget(Participant data) {
		super(data);
	}
	
	
	protected void init() {
		layout.paddingTop = 0;
		layout.paddingBottom = 0;
		layout.paddingLeft = 0;
		
		HLayout hl = new HLayout();
		hl.paddingTop = 0;
		hl.paddingBottom = 0;
		hl.paddingLeft = 0;
        hl.setGap(0);
		
		Text number = new Text(data.number + "",
				StartTrackApp.getResources().getLabelStyle("participant-number"));
		number.setWidth(App.dp2px(32));
		number.getStyle().fontColor = App.Colors.getColorByName("TEXT_NUMBER");
		number.setAlignment(Align.center);

		
		Rect textRect = new Rect();
		textRect.addActor(number);
        textRect.setBackgroundColor(App.Colors.WIDGET_BORDER);

        VLayout nameLayout = new VLayout();
        nameLayout.gap = 0;
        nameLayout.setPadding(StartTrackApp.dp2px(20));
		
		Text title = new Text(data.first_name + " " + data.last_name,
				StartTrackApp.getResources().getLabelStyle("h1"));
//		title.setWidth(Gdx.graphics.getWidth() - 2 * mp);
		title.setWrap(true);
        nameLayout.addActor(title);

        float s = nameLayout.getHeight();
        textRect.setSize(s, s);
        number.setSize(s, s);


        hl.addActor(textRect);
		hl.addActor(nameLayout);
		
		layout.addActor(hl);
		setBorder(App.dp2px(1));
		setBorderColor(App.Colors.WIDGET_BORDER);
	}
}
