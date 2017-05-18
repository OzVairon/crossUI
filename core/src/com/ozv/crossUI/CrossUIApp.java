package com.ozv.crossui;

import com.idp.engine.App;

/**
 * Mobile app for Startrack project in centergame.
 *
 * @author idp
 */
public class CrossUIApp extends App {

	@Override
	public void create() {
		super.create();
	}

	/**
	 * @return current app instance
	 */
	public static CrossUIApp getInstance() {
		return (CrossUIApp) instance;
	}
}
