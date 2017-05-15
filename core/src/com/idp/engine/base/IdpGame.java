package com.idp.engine.base;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.idp.engine.resources.assets.IdpAssetManager;
import com.idp.engine.ui.graphics.base.IdpLogger;
import com.idp.engine.ui.screens.IdpBaseScreen;
import com.idp.engine.ui.screens.IdpGameScreen;

/**
 * Base class for idp games.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class IdpGame extends Game {

	protected IdpGame() {
		if (Idp.game != null) {
			Idp.game.dispose();
		}
		Idp.game = this;
		Idp.input = new IdpInput();
		Idp.files = new IdpFiles();
		Idp.logger = new IdpLogger();
	}


	/**
	 * Checks whether device aspect ratio is close to 16x9.
	 * @return true if it is closer to 16x9 than to 4x3, false otherwise
	 */
	public boolean is16x9() {
		float f = Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
		return Math.abs(16.0 / 9 - f) < Math.abs(4.0 / 3 - f);
	}

	@Override
	public void create() {
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glEnable(GL20.GL_NICEST);
		Gdx.gl20.glEnable(GL20.GL_LINEAR);
		Idp.logger.setFont(IdpAssetManager.getInstance().loadFont("fonts/lucida.ttf", 20));
		Idp.input.startProcessing();
	}

	@Override
	public void setScreen(Screen screen) {
		IdpBaseScreen idpEmptyScreen = (IdpBaseScreen)screen;

		if (!IdpAssetManager.getInstance().update()) {

			if (idpEmptyScreen instanceof IdpGameScreen) {

				IdpGameScreen idpScreen = (IdpGameScreen)idpEmptyScreen;
				if (idpScreen.getLoadScreen() != null) {
					super.setScreen(idpScreen.getLoadScreen());
				}
			}
			else {
				IdpAssetManager.getInstance().finishLoading();
				super.setScreen(idpEmptyScreen);
			}
		}
		else {
			super.setScreen(idpEmptyScreen);
		}
	}

	@Override
	public IdpBaseScreen getScreen() {
		return (IdpBaseScreen)super.getScreen();
	}

	@Override
	public void dispose() {
		super.dispose();
		IdpAssetManager.getInstance().dispose();
		Idp.game = null;
		Idp.input = null;
		Idp.files = null;
		Idp.logger = null;
	}

	public static boolean isAndroid() {
		return Gdx.app.getType() == Application.ApplicationType.Android;
	}

	public static boolean isIos() {
		return Gdx.app.getType() == Application.ApplicationType.iOS;
	}

	public static boolean isMobile() {
		return isAndroid() || isIos();
	}
}
