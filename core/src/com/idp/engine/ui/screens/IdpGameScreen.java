/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.idp.engine.resources.assets.IdpAsset;
import com.idp.engine.resources.assets.IdpAssetManager;

/**
 * Base class for screens that need extra resources to load.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public abstract class IdpGameScreen extends IdpBaseScreen {

	protected IdpAsset<TextureAtlas> atlas;
	protected IdpLoadScreen loadScreen;


	public IdpGameScreen() {
		this.loadScreen = null;
	}


	/**
	 * @return atlas with packed images that this screen requires
	 */
	public IdpAsset<TextureAtlas> getAtlas() {
		return atlas;
	}

	/**
	 * @return screen that should be displayed during resource load
	 */
	public IdpLoadScreen getLoadScreen() {
		return loadScreen;
	}

	protected IdpAsset<BitmapFont> loadFontAsync(String filename, int size) {
		return IdpAssetManager.getInstance().loadFont(filename, size);
	}

	protected IdpAsset<TextureAtlas> loadAtlasAsync(String filename) {
		return IdpAssetManager.getInstance().loadAtlas(filename);
	}

	protected IdpAsset<Sound> loadSoundAsync(String filename) {
		return IdpAssetManager.getInstance().loadSound(filename);
	}

	protected IdpAsset<Music> loadMusicAsync(String filename) {
		return IdpAssetManager.getInstance().loadMusic(filename);
	}
}
