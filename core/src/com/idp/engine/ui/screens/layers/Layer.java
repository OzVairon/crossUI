package com.idp.engine.ui.screens.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.idp.engine.ui.graphics.base.Rect;

/**
 * Root element that holds hierarchy of others.
 *
 *
 */
public class Layer extends Rect {

	public Layer() {
		//setFixHeight(true);
		this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		this.setPadding(0);
//		this.setGap(0);
		this.setBackgroundColor(Color.valueOf("eeeeeeff"));
	}

}
