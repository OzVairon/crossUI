/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozv.starttrack.graphics.starttrack_widgets.base;

import com.badlogic.gdx.utils.Align;
import com.ozv.starttrack.StartTrackApp;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.ImageActor;
import com.idp.engine.ui.graphics.base.Rect;
import com.idp.engine.ui.graphics.actors.Text;

/**
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class RadioButton extends Rect {
	
	private final int sp = StartTrackApp.dp2px(8);   // small gap
	private final int mp = StartTrackApp.dp2px(12);  // medium gap
	private final int lp = StartTrackApp.dp2px(16);  // large gap
	
	private final Text number;
	private boolean selected;
	private final ImageActor icon;
	
	
	public RadioButton(String text) {
		
		this.number = new Text(text,
				StartTrackApp.getResources().getLabelStyle("participant-number")); //h1
		number.getStyle().fontColor = App.Colors.MAIN;
		number.setAlignment(Align.center);
		float s = number.getHeight() + mp * 2;
		
		addActor(number);
		setSize(s, s);
		setBackgroundColor(App.Colors.TRANSPARENT);
		setBorderColor(App.Colors.MAIN);
		setBorder(App.dp2px(1.5f));
		number.setSize(s, s);
		
		icon = null;
	}
	
	public RadioButton(ImageActor icon) {
		
		this.number = new Text("1",
				StartTrackApp.getResources().getLabelStyle("h1"));
		number.getStyle().fontColor = App.Colors.MAIN;
		number.setAlignment(Align.center);
		float s = number.getHeight() + mp * 2;
		
		setSize(s, s);
		setBackgroundColor(App.Colors.TRANSPARENT);
		
        icon.setBounds(s / 4, s / 4, s / 2, s / 2);
        icon.setColor(App.Colors.MAIN);
        addActor(icon);
		
		this.icon = icon;
	}
	
	
	public boolean isSelected() {
		return selected;
	}
	
	public void select() {
		if (selected)
			return;
		number.getStyle().fontColor = App.Colors.TEXT_NAVBAR;
		if (icon == null)
			setBackgroundColor(App.Colors.MAIN);
		selected = true;
		onSelected();
	}
	
	public void unselect() {
		if (!selected)
			return;
		number.getStyle().fontColor = App.Colors.MAIN;
		setBackgroundColor(App.Colors.TRANSPARENT);
		selected = false;
		onUnselected();
	}
	
	public void onSelected() {
		
	}
	
	public void onUnselected() {
		
	}

	public ImageActor getIcon() {
		return icon;
	}
	
}
