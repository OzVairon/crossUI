package com.ozv.crossui.screens;

import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.Text;
import com.idp.engine.ui.screens.IdpAppScreen;

/**
 * Screen with sign in form.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class MainScreen extends IdpAppScreen {


	public MainScreen() {
		super("Screen");
	}

	@Override
	protected void init() {
		super.init();
		getNavbar().setBackgroundColor(App.Colors.MAIN);
		Text t = new Text("Hello, World!", App.getResources().getLabelStyle("text"));

		addActor(t);

	}


	@Override
	public void show() {
		super.show();

	}



}
