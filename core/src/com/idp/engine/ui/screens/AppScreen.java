package com.idp.engine.ui.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.idp.engine.App;
import com.idp.engine.resources.assets.IdpColorPixmap;
import com.idp.engine.ui.graphics.base.Navbar;
import com.idp.engine.ui.screens.layers.MainLayer;
import com.idp.engine.ui.screens.layers.PopupLayer;

/**
 * Screen with navbar and layer structure.
 *
 *
 */
public class AppScreen extends IdpBaseScreen {

	private MainLayer mainLayer;
	private PopupLayer popupLayer;
	private Color navbarColor;
	private String name;
	private Actor fader;


	/**
	 * Calls {@link AppScreen#AppScreen(String, Color)} with empty name and white color.
	 */
	public AppScreen() {
		this("Screen");
	}

	/**
	 * Calls {@link AppScreen#AppScreen(String, Color)} with given name and white color.
	 * @param name screen name
	 */
	public AppScreen(String name) {
		this(name, App.Colors.MAIN);
	}

	/**
	 * Calls {@link AppScreen#AppScreen(String, Color)} with empty name and given color.
	 * @param navbarColor background color of the navbar
	 */
	public AppScreen(Color navbarColor) {
		this("Screen", navbarColor);
	}

	/**
	 * Constructs screen with given name(shown in navbar) and navbar colored with the given color.
	 * @param name name of the screen
	 * @param navbarColor background color of navbar
	 */
	public AppScreen(String name, Color navbarColor) {
		super(true);
		this.name = name;
		this.navbarColor = navbarColor;
		init();
	}


	/**
	 * @return navbar
	 */
	public Navbar getNavbar() {
		return mainLayer.navbar;
	}

	/**
	 * @return main layer
	 */
    public MainLayer getMainLayer() {
        return mainLayer;
    }

	/**
	 * @return popup layer
	 */
    public PopupLayer getPopupLayer() {
        return popupLayer;
    }

	/**
	 * Adds actor to the content of the screen.
	 * (Content is all-that-is-not-navbar).
	 * @param a actor to add
	 */
	public void addActor(Actor a) {
		mainLayer.content.addActor(a);
	}

	/**
	 * returns name of the screen that is shown in navbar.
	 * @return screen name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Fades screen with black semitransparent black.
	 * @param duration animation duration
	 */
	public void fadeOut(float duration) {
		fader.getColor().a = 0;
		fader.addAction(Actions.alpha(0.4f, duration));
	}

	/**
	 * Removes black fade from the screen.
	 * @param duration animation duration
	 */
	public void fadeIn(float duration) {
		fader.getColor().a = 0.4f;
		fader.addAction(Actions.alpha(0f, duration));
	}

	/**
	 * Initialization of the class members. Called in constructor.
	 */
	protected void init() {
		this.mainLayer = new MainLayer();
		mainLayer.navbar.setBackgroundColor(navbarColor);
		mainLayer.navbar.setText(name);
		stage.addActor(mainLayer);

		this.fader = new IdpColorPixmap(Color.WHITE).buildActor();
		fader.setSize(mainLayer.getWidth(), mainLayer.getHeight());
		fader.setColor(Color.CLEAR);
		fader.setTouchable(Touchable.disabled);
		stage.addActor(fader);

        this.popupLayer = new PopupLayer();
        stage.addActor(popupLayer);
	}

    public void getConfirmationDialog(String titleString, String message, ClickListener confirm) {
		popupLayer.getConfirmationDialog(titleString, message, confirm, null);
    }

	public void getProgressDialog(String titleString, String message) {
		popupLayer.getProgressDialog(titleString, message);
	}

	public void getAlertDialog(String titleString, String message) {
		popupLayer.getAlertDialog(titleString, message);
	}

    protected void clearScene() {
        stage.clear();
    }

	@Override
	public void render(float delta) {
		super.render(delta);
		//System.out.println("RENDER");
	}

	@Override
	public void show() {
		super.show();
		System.out.println(getName());
	}
}
