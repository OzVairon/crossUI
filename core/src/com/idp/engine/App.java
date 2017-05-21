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
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.idp.engine.ui.screens.ScreenManager;
import com.idp.engine.resources.Resources;
import com.idp.engine.resources.assets.IdpAssetManager;
import com.idp.engine.base.AppUtils;
import com.idp.engine.base.IdpInput;
import com.idp.engine.ui.screens.AppScreen;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.HashMap;

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
	protected ScreenManager screenManager;
	private Color glColor = Color.BLACK;

	@Override
	public void create() {

		if (AppUtils.app != null) {
			AppUtils.app.dispose();
		}
        instance = this;
		AppUtils.app = this;
		AppUtils.input = new IdpInput();
		AppUtils.files = new com.idp.engine.base.IdpFiles();
		AppUtils.logger = null;
        initDp();
		AppUtils.input.startProcessing();

		AppUtils.input.setBackKeyProcessor(new InputAdapter() {
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.BACK) {
					try {
						App.backScreen();
					} catch (EmptyStackException ex) {
						Gdx.app.exit();
					}
					return true;
				}
				return false;
			}
		});
		AppUtils.input.setCatchBackKey(true);
		Gdx.graphics.setContinuousRendering(false);  // important to save battery

		dialogs = GDXDialogsSystem.install();

		this.resources = new Resources();
		screenManager = new ScreenManager();
		loadXmlConfig();

		setGLColor(Colors.MAIN);

		screenManager.start();
	}

	protected void setGLColor(Color сolor) {
		glColor = сolor;
	}

	private void loadXmlConfig() {
		XmlReader xr = new XmlReader();
		try {
			XmlReader.Element config = xr.parse(AppUtils.files.internal("appconfig.xml"));
			String packageName = config.getAttribute("package");
			String screenname = config.getChildByName("mainscreen").getAttribute("name");
			Colors.loadColorScheme(config.getChildByName("colors"));
			this.resources .loadFonts(config.getChildByName("fonts"));
			this.resources .loadIcons("icons.atlas");
			this.resources .awaitLoad();


			Object screenObject = null;
			try {
				screenObject = ClassReflection.forName(packageName + ".screens." + screenname).newInstance();

			} catch (Exception ex) {
				screenObject = new AppScreen("DUMMY SCREEN");
			}

			screenManager.setStartScreen((AppScreen) screenObject);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	@Override
	public void render() {
		Gdx.gl.glClearColor(glColor.r, glColor.g, glColor.b, glColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}



	@Override
	public void dispose() {
		super.dispose();
		IdpAssetManager.getInstance().dispose();
		AppUtils.app = null;
		AppUtils.input = null;
		AppUtils.files = null;
		AppUtils.logger = null;
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

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}

	//TRANSITIONS

	public static AppScreen getCurrentScreen() {
		return App.getInstance().screenManager.getCurrentScreen();
	}

	public static void showScreen(AppScreen screen) { getInstance().screenManager.setScreen(screen);}

	/**
	 * Adds new screen to the screen stack.
	 * @param s new screen
	 */
	public static void pushScreen(AppScreen s) {
		getInstance().screenManager.pushScreen(s);
	}

	/**
	 * Set previous screen
	 */
	public static void backScreen() {
		getInstance().screenManager.popScreen();
	}


	public static class Colors {
		public static Color MAIN = Color.valueOf("000000");
		public static Color BACK = Color.valueOf("F3F3F3");
		public static Color TEXT_NAVBAR = Color.valueOf("FFFFFF");
		public static Color TRANSPARENT = Color.valueOf("00000000");
		public static Color WIDGET_WHITE = Color.valueOf("FFFFFF");
		public static Color WIDGET_BORDER = Color.valueOf("e7e7e7");
		private static HashMap<String, Color> colors = new HashMap<String, Color>();

		public static Color getColorByName(String name) {
			if (colors.containsKey(name)) return colors.get(name);
			else return Color.BLACK;
		}
		public static void loadColorScheme(XmlReader.Element colorsXml) {
			for(XmlReader.Element e : colorsXml.getChildrenByName("color")){
                try {
                    Field f = ClassReflection.getField(Colors.class, e.getAttribute("name"));
                    f.set(null, Color.valueOf(e.getAttribute("value")));
                } catch (ReflectionException e1) {
                    colors.put(e.getAttribute("name"), Color.valueOf(e.getAttribute("value")));
                }
            }
		}
	}


}
