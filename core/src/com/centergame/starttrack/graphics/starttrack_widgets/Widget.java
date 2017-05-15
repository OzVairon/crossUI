/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.centergame.starttrack.graphics.starttrack_widgets;

import com.badlogic.gdx.Gdx;
import com.centergame.starttrack.StartTrackApp;
import com.centergame.starttrack.graphics.starttrack_widgets.base.VLayout;
import com.idp.engine.ui.graphics.base.Rect;

/**
 * Base Widget.
 *
 * @param <T>
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class Widget<T> extends Rect {

	protected final int sp = StartTrackApp.dp2px(8);  // small gap
	protected final int mp = StartTrackApp.dp2px(12); // medium gap
	protected final int lp = StartTrackApp.dp2px(16); // large gap

	protected final VLayout layout;
	protected T data;

	
	public Widget(T data) {
		setBackgroundColor(StartTrackApp.ColorPallete.ELEMENT_BACK);
		this.layout = new VLayout();
		addActor(layout);
		this.data = data;
		init();
		setSize(Gdx.graphics.getWidth(), layout.getHeight());
	}
	
	
	public T getData() {
		return data;
	}
	
	protected void init() {
		
	}

    public void setPadding(int p) {
        layout.setPadding(p);
        setSize(Gdx.graphics.getWidth(), layout.getHeight());
    }
	
}
