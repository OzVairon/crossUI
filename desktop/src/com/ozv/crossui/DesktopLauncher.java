package com.ozv.crossui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.idp.engine.resources.assets.IdpAssetPacker;


public class DesktopLauncher {

	public static void main(String[] args) {

		IdpAssetPacker.pack(new String[] {
			"icons"
		});

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new CrossUIApp(), config);
	}


}
