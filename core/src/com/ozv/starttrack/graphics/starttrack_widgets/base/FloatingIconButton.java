/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozv.starttrack.graphics.starttrack_widgets.base;

import com.badlogic.gdx.graphics.Color;
import com.idp.engine.ui.graphics.actors.ImageActor;

/**
 *
 * @author dhabensky
 */
public class FloatingIconButton extends IconButton {

    private ImageActor back;
	
	public FloatingIconButton() {
		this("backScreen");
	}

	
	public FloatingIconButton(String iconName) {
        this(iconName, 48);
    }

    public FloatingIconButton(String iconName, int sizeDp) {
        super(iconName, sizeDp);
        this.back =  new ImageActor(com.ozv.starttrack.StartTrackApp.getResources().getIcon("medal"));
        back.setSize(this.getWidth(), this.getHeight());
        addActor(back);
        back.toBack();
//		icon.setOrigin(Align.center);
//		setOrigin(Align.center);
    }


    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (back != null)
            back.setSize(this.getWidth(), this.getHeight());
    }

    public void setBackColor(Color color) {
        this.back.setColor(color);
    }

    public void setIconColor(Color color) {
        getIcon().setColor(color);
    }
}
