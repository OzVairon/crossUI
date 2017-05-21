/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.screens;

import com.idp.engine.base.AppUtils;
import com.idp.engine.resources.assets.IdpAssetManager;


/**
 * Base class for screens that loads some resources.
 *
 *
 */
public abstract class IdpLoadScreen extends IdpBaseScreen {

	private final com.idp.engine.ui.screens.IdpGameScreen nextScreen;


	/**
	 * @param nextScreen screen that will be shown as resources are loaded
	 */
	public IdpLoadScreen(com.idp.engine.ui.screens.IdpGameScreen nextScreen) {
		this.nextScreen = nextScreen;
	}


	@Override
	public void show() {
		AppUtils.input.setBackKeyProcessor(null);
		AppUtils.input.setCatchBackKey(true);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (IdpAssetManager.getInstance().update()) {
			AppUtils.game.setScreen(nextScreen);
		}
	}
}
