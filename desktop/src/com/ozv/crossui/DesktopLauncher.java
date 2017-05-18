package com.ozv.crossui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.idp.engine.base.IdpFiles;
import com.idp.engine.resources.assets.IdpAssetPacker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;


public class DesktopLauncher {

	public static void main(String[] args) {

		IdpAssetPacker.pack(new String[] {
			"icons"
		});

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new StartTrackApp(), config);
	}


}
