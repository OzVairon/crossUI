package com.idp.engine.ui.graphics.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.ImageActor;

/**
 * UI element with a spinning icon.
 * Usually indicates that something is loading.
 *
 * Created by ozvairon on 28.08.16.
 */
public class Loader extends Group {

	private final ImageActor icon;
	private boolean started;


	public Loader(TextureRegion iconRegion) {
		icon = new ImageActor(iconRegion);
		icon.setSize(App.dp2px(20), App.dp2px(20));
		icon.setOrigin(App.dp2px(10), App.dp2px(10));
		addActor(icon);
		layout();
	}


	private void layout() {
		icon.setX((getWidth() - icon.getWidth()) / 2);
		icon.setY((getHeight() - icon.getHeight()) / 2);
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		layout();
	}

	public void start() {
		if (!started) {
			icon.addAction(Actions.forever(Actions.rotateBy(200, 1f)));
			started = true;
		}
	}

	public void stop() {
		if (started) {
			icon.clearActions();
			started = false;
		}
	}
}
