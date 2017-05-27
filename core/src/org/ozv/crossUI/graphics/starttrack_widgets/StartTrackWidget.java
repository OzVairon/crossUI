/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ozv.crossUI.graphics.starttrack_widgets;

import com.badlogic.gdx.Gdx;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.layouts.VLayout;
import com.idp.engine.ui.graphics.base.Rect;

/**
 * Base StartTrackWidget.
 *
 * @param <T>
 * @author idp
 */
public abstract class StartTrackWidget<T> extends Rect {

	protected final int sp = App.dp2px(8);  // small gap
	protected final int mp = App.dp2px(12); // medium gap
	protected final int lp = App.dp2px(16); // large gap

	protected final VLayout layout;
	protected T data;

	
	public StartTrackWidget(T data) {
		setBackgroundColor(App.Colors.WIDGET_WHITE);
		this.layout = new VLayout();
		addActor(layout);
		this.data = data;
		init();
		setSize(Gdx.graphics.getWidth(), layout.getHeight());
	}
	
	
	public T getData() {
		return data;
	}
	
	protected abstract void init();

	public void setPadding(int p) {
        layout.setPadding(p);
        setSize(Gdx.graphics.getWidth(), layout.getHeight());
    }
	
}
