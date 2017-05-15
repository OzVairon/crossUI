/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.screens;

import com.idp.engine.base.Idp;
import com.idp.engine.resources.assets.IdpAssetManager;


/**
 * Base class for screens that loads some resources.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
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
		Idp.input.setBackKeyProcessor(null);
		Idp.input.setCatchBackKey(true);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (IdpAssetManager.getInstance().update()) {
			Idp.game.setScreen(nextScreen);
		}
	}
}
