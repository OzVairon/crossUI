/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.centergame.starttrack.StartTrackApp;
import com.centergame.starttrack.screens.base.StartTrackBaseScreen;
import com.idp.engine.resources.Resources;
import com.idp.engine.ui.graphics.base.Navbar;
import com.idp.engine.ui.graphics.base.Rect;
import com.idp.engine.resources.assets.IdpAssetManager;
import com.idp.engine.base.Idp;
import com.idp.engine.base.IdpInput;
import com.idp.engine.ui.screens.IdpAppScreen;
import com.idp.engine.ui.screens.IdpBaseScreen;
import com.idp.engine.ui.screens.TransitionManager;

import java.util.EmptyStackException;
import java.util.Stack;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;

/**
 * Base class for non-gaming mobile applications.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class App extends Game {
    
	protected static App instance;
    protected Resources resources;
	protected static float dp2pxCoeff;
	protected GDXDialogs dialogs;

	protected TransitionManager transitionManager;
	protected IdpAppScreen currentScreen;
	protected final Stack<IdpAppScreen> stack = new Stack<IdpAppScreen>();

	private Color glColor = Color.BLACK;



	@Override
	public void create() {

		if (com.idp.engine.base.Idp.app != null) {
			com.idp.engine.base.Idp.app.dispose();
		}
        instance = this;
		com.idp.engine.base.Idp.app = this;
		com.idp.engine.base.Idp.input = new IdpInput();
		com.idp.engine.base.Idp.files = new com.idp.engine.base.IdpFiles();
		com.idp.engine.base.Idp.logger = null;
        initDp();
		com.idp.engine.base.Idp.input.startProcessing();

		Idp.input.setBackKeyProcessor(new InputAdapter() {
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.BACK) {
					try {
						popScreen();
					} catch (EmptyStackException ex) {
						Gdx.app.exit();
					}
					return true;
				}
				return false;
			}
		});
		Idp.input.setCatchBackKey(true);
		Gdx.graphics.setContinuousRendering(false);  // important to save battery

		dialogs = GDXDialogsSystem.install();

		this.resources = new Resources();
		this.resources .enqueueAll();
		this.resources .awaitLoad();
		transitionManager = new TransitionManager();
		setGLColor(Color.valueOf("006FC0"));
	}

	protected void setGLColor(Color сolor) {
		glColor = сolor;
	}
    
	@Override
	public void render() {
		Gdx.gl.glClearColor(glColor.r, glColor.g, glColor.b, glColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	@Override
	public IdpBaseScreen getScreen() {
		return (IdpBaseScreen)super.getScreen();
	}

	@Override
	public void dispose() {
		super.dispose();
		IdpAssetManager.getInstance().dispose();
		com.idp.engine.base.Idp.app = null;
		com.idp.engine.base.Idp.input = null;
		com.idp.engine.base.Idp.files = null;
		Idp.logger = null;
        instance = null;
	}
    
    /**
     * @return current app instance
     */
    public static App getInstance() {
        return instance;
    }
    
    /**
     * @return resources of current app instance
     */
    public static Resources getResources() {
        return instance.resources;
    }
    
	/**
	 * Converts dp to px.
     * @param dp
     * @return px
	 */
	public static int dp2px(float dp) {
		return (int)(dp * dp2pxCoeff);
	}

	/**
	 * Converts px to dp.
     * @param px
     * @return dp
	 */
	public static int px2dp(float px) {
		return (int)(px / dp2pxCoeff);
	}

	private void initDp() {
		float ppi = Gdx.graphics.getPpiX();
		switch (Gdx.app.getType()) {

			case Android:

				if (ppi <= 160)
					dp2pxCoeff = 1;
				else if (ppi <= 240)
					dp2pxCoeff = 1.5f;
				else if (ppi <= 320)
					dp2pxCoeff = 2;
				else if (ppi <= 480)
					dp2pxCoeff = 3;
				else
					dp2pxCoeff = 4;
				break;

			case iOS:
				if (Gdx.graphics.getWidth() < 900)
					dp2pxCoeff = 2;
				else
					dp2pxCoeff = 3;
				break;

			case Desktop:
				dp2pxCoeff = 1.5f;
				break;
		}
	}

	public GDXDialogs getDialogs() {
		return dialogs;
	}





	//TRANSITIONS

	/**
	 * Adds new screen to the screen stack.
	 * @param s new screen
	 */
	public void pushScreen(IdpAppScreen s) {
		stack.push(currentScreen);

		currentScreen = s;
		Navbar navbar = currentScreen.getNavbar();

		Rect back = new Navbar.NavButton("back");
		back.setBackgroundColor(Color.CLEAR);
		back.setColor(StartTrackApp.ColorPallete.TEXT_NAVBAR);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				popScreen();
			}
		});
		navbar.getLeftIcons().addActor(back);

		changeScreen(currentScreen, TransitionManager.TransitionType.SLIDE_RIGHT_LEFT);
	}

	/**
	 * Removes one screen from the screen stack.
	 */
	public void popScreen() {
		this.currentScreen = stack.pop();
		changeScreen(currentScreen, TransitionManager.TransitionType.SLIDE_LEFT_RIGHT);
	}

	@Override
	public void setScreen(Screen screen) {
		currentScreen = (IdpAppScreen) screen;
		super.setScreen(screen);
	}

	/**
	 * performs screen transition from current screen to another.
	 *
	 * @param screen screen that will be shown after transition
	 * @param type transition type
	 */
	public void changeScreen(IdpAppScreen screen, TransitionManager.TransitionType type) {
		if (getScreen() == null) {
			setScreen(screen);
		} else {
			if (getScreen() == transitionManager) return;
			transitionManager.fadeScreens(type, (StartTrackBaseScreen<?>) screen, 0.4f);
		}
	}

	public static class ColorPallete {

		public static Color MAIN = Color.valueOf("006FC0");
		public static Color BACK = Color.valueOf("F3F3F3");
		public static Color ELEMENT_BACK = Color.valueOf("FFFFFF");
		public static Color ELEMENT_BACK_SELECTED = Color.valueOf("D6FBB1");
		public static Color ELEMENT_BORDER = Color.valueOf("e7e7e7");

		public static Color ICON_TICK = Color.valueOf("396809");
		public static Color ICON_CANCEL = Color.valueOf("FF3333");

		public static Color TEXT_MAIN = Color.valueOf("000000");
		public static Color TEXT_NAVBAR = Color.valueOf("FFFFFF");
		public static Color TEXT_HINT = Color.valueOf("666666");

		public static Color TEXT_NUMBER = Color.valueOf("666666");

		public static Color TRANSPARENT = Color.valueOf("00000000");
	}


}
