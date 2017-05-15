package com.idp.engine.ui.screens.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.idp.engine.ui.graphics.base.Rect;

/**
 * Root element that holds hierarchy of others.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class Layer extends Rect {

	public Layer() {
		this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.setBackgroundColor(Color.valueOf("eeeeee"));
	}
}
