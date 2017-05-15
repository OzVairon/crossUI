/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.resources.assets;

import com.badlogic.gdx.assets.AssetLoaderParameters.LoadedCallback;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader.TextureAtlasParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import java.util.*;

/**
 * Helpers for loading assets and managing them.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public final class IdpAssetManager {

	private static IdpAssetManager instance;

	private final AssetManager man;
	private final HashMap<String, ArrayList<IdpAsset>> loadingAssets;
	private final LoadedCallback callback;

	private final ArrayList<IdpColorPixmap> pixmaps;

	private final HashMap<String, PixmapAndTexture> cache;


	private IdpAssetManager() {
		this.man = new AssetManager();
		FileHandleResolver resolver = new InternalFileHandleResolver();
		man.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		man.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		this.loadingAssets = new HashMap<String, ArrayList<IdpAsset>>();
		this.pixmaps = new ArrayList<IdpColorPixmap>();
		this.cache = new HashMap<String, PixmapAndTexture>();

		this.callback = new LoadedCallback() {
			@Override
			public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
				ArrayList<IdpAsset> list = loadingAssets.remove(fileName);
				if (list != null) {
					Object loaded = assetManager.get(fileName, type);
					for (IdpAsset asset : list) {
						asset.setAsset(loaded);
					}
				}
			}
		};
	}


	/**
	 * Adds new TrueType font to loading queue.
	 * @param filename .ttf file path
	 * @param size size of bitmap font that will be generated
	 * @return loading asset handle
	 */
	public IdpAsset<BitmapFont> loadFont(String filename, int size) {
		FreeTypeFontLoaderParameter par = new FreeTypeFontLoaderParameter();
		par.fontFileName = filename;
		par.fontParameters.flip = true;
		par.fontParameters.minFilter = Texture.TextureFilter.Linear;
		par.fontParameters.magFilter = Texture.TextureFilter.Linear;
		par.fontParameters.size = size;
		par.fontParameters.characters = FreeTypeFontGenerator.DEFAULT_CHARS +
				"ЙЦУКЕНГШЩЗХЪЁФЫВАПРОЛДЖЭЯЧСМИТЬБЮйцукенгшщзхъёфывапролджэячсмитьбю";
		par.loadedCallback = callback;
		man.load(getGenericFontName(filename, size), BitmapFont.class, par);
		IdpAsset<BitmapFont> res = new IdpAsset<BitmapFont>();
		put(getGenericFontName(filename, size), res);
		return res;
	}

	/**
	 * Adds new Fnt font to loading queue.
	 * @param filename .fnt file path
	 * @return loading asset handle
	 */
    public IdpAsset<BitmapFont> loadFntFont(String filename) {
        BitmapFontLoader.BitmapFontParameter par = new BitmapFontLoader.BitmapFontParameter();
        par.flip = true;
		par.loadedCallback = callback;
        man.load(filename, BitmapFont.class, par);
        IdpAsset<BitmapFont> res = new IdpAsset<BitmapFont>();
        put(filename, res);
        return res;
    }

	/**
	 * Adds new image atlas to loading queue.
	 * @param filename .atlas file path
	 * @return loading asset handle
	 */
	public IdpAsset<TextureAtlas> loadAtlas(String filename) {
		TextureAtlasParameter par = new TextureAtlasParameter(true);
		par.loadedCallback = callback;
		man.load(filename, TextureAtlas.class, par);
		IdpAsset<TextureAtlas> res = new IdpAsset<TextureAtlas>();
		put(filename, res);
		return res;
	}

	/**
	 * Adds new sound to loading queue.
	 * @param filename audio file path
	 * @return loading asset handle
	 */
	public IdpAsset<Sound> loadSound(String filename) {
		SoundLoader.SoundParameter par = new SoundLoader.SoundParameter();
		par.loadedCallback = callback;
		man.load(filename, Sound.class, par);
		IdpAsset<Sound> res = new IdpAsset<Sound>();
		put(filename, res);
		return res;
	}

	/**
	 * Adds new music to loading queue.
	 * @param filename audio file path
	 * @return loading asset handle
	 */
	public IdpAsset<Music> loadMusic(String filename) {
		MusicLoader.MusicParameter par = new MusicLoader.MusicParameter();
		par.loadedCallback = callback;
		man.load(filename, Music.class, par);
		IdpAsset<Music> res = new IdpAsset<Music>();
		put(filename, res);
		return res;
	}

	/**
	 * Adds a pixmap to the runtime cache.
	 * Check {@link #getTextureFromCache(String)} before starting new download.
	 * If the image from that url was already downloaded, it will be returned.
	 * @param url where the image is located
	 * @param pixmap downloaded image
	 */
	public void cachePixmap(String url, Gdx2DPixmap pixmap) {
		if (url == null)
			throw new NullPointerException("url cannot be null");
		if (pixmap == null)
			throw new NullPointerException("pixmap cannot be null");

		cache.put(url, new PixmapAndTexture(pixmap));
	}


	/**
	 * Stops current thread until all resources get loaded.
	 */
	public void finishLoading() {
		man.finishLoading();
	}

	/**
	 * Updates progress of the asset manager.
	 * @return true if all resources are loaded, false otherwise
	 */
	public boolean update() {
		if (loadingAssets.isEmpty())
			return true;
		return man.update();
	}

	/**
	 * @return progress of resource loading. 1.0 indicates that all of the are loaded
	 */
	public float getProgress() {
		return man.getProgress();
	}


	/**
	 * @param filename ttf font path
	 * @param size font size
	 * @return bitmap font of size {@code size} generated from ttf font loaded from the given path
	 */
	public BitmapFont getFont(String filename, int size) {
		return man.get(getGenericFontName(filename, size), BitmapFont.class);
	}

	/**
	 * @param filename .atlas path
	 * @return texture atlas loaded from the given path
	 */
	public TextureAtlas getAtlas(String filename) {
		return man.get(filename, TextureAtlas.class);
	}

	/**
	 * @param filename audio file path
	 * @return sound object for the audio file located at {@code path}
	 */
	public Sound getSound(String filename) {
		return man.get(filename, Sound.class);
	}

	/**
	 * @param filename audio file path
	 * @return music object for the audio file located at {@code path}
	 */
	public Music getMusic(String filename) {
		return man.get(filename, Music.class);
	}

	/**
	 * @see #cachePixmap(String, Gdx2DPixmap)
	 * @return texture generated from pixmap loaded from the given url
	 *   or null if no cache entry for the given url
	 */
	public Texture getTextureFromCache(String url) {
		PixmapAndTexture e = cache.get(url);
		if (e != null) {
			if (e.texture == null)
				e.texture = new Texture(new Pixmap(e.pixmap));
			return e.texture;
		}
		else {
			return null;
		}
	}

	/**
	 * Disposes the asset manager and unloads all managed resources.
	 */
	public void dispose() {
		man.dispose();

		for (IdpColorPixmap tex : pixmaps)
			tex.dispose();
		pixmaps.clear();

		for (Map.Entry<String, PixmapAndTexture> e : cache.entrySet()) {
			e.getValue().texture.dispose();
		}
		cache.clear();

		instance = null;
	}


	void managePixmap(IdpColorPixmap pixmap) {
		pixmaps.add(pixmap);
	}


	/**
	 * @return instance of the asset manager
	 */
	public static IdpAssetManager getInstance() {
		if (instance == null)
			instance = new IdpAssetManager();
		return instance;
	}

	private static String getGenericFontName(String filename, int size) {
		int ind = filename.lastIndexOf(".");
		return filename.substring(0, ind) + size + filename.substring(ind);
	}

	private void put(String filename, IdpAsset asset) {
		ArrayList<IdpAsset> list = loadingAssets.get(filename);
		if (list == null) {
			list = new ArrayList<IdpAsset>();
			loadingAssets.put(filename, list);
		}
		list.add(asset);
	}


	private static class PixmapAndTexture {
		public Gdx2DPixmap pixmap;
		public Texture texture;

		public PixmapAndTexture(Gdx2DPixmap pixmap) {
			if (pixmap == null)
				throw new NullPointerException("pixmap cannot be null");
			this.pixmap = pixmap;
		}
	}
}
