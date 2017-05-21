/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.net;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.idp.engine.resources.assets.IdpAssetManager;
import com.idp.engine.resources.assets.IdpColorPixmap;

/**
 * Image that loads itself from the Internet.
 * Fields:
 *   loadingTex - displayed while http request is in progress
 *   loadedTex  - displayed as soon as it gets downloaded
 *   failedTex  - displayed if network request was failed
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class NetworkImage extends Group {

	private TextureRegion loadingTex;
	private TextureRegion failedTex;
	private TextureRegion loadedTex;
	private TextureRegion texToDraw;

	private final Request manager;


	public NetworkImage() {
		this(null);
	}

	/**
	 * Creates network image and sets the url where the image can be downloaded.
	 * No requests are made here.
	 */
	public NetworkImage(String url) {
		this.manager = new Request(url);
		this.loadingTex =
				new TextureRegion(new IdpColorPixmap(Color.valueOf("999999")).getTexture());
		this.texToDraw = loadingTex;
	}


	/**
	 * Returns manager that will perform http request to download an image.
	 * Use to configure request before its started or to obtain request state.
	 */
	public Request getManager() {
		return manager;
	}

	public TextureRegion getLoadingTex() {
		return loadingTex;
	}

	public void setLoadingTex(TextureRegion loadingTex) {
		if (manager.getState() == Request.State.LOADING)
			texToDraw = loadingTex;
		this.loadingTex = loadingTex;
	}

	public TextureRegion getFailedTex() {
		return failedTex;
	}

	public void setFailedTex(TextureRegion failedTex) {
		if (manager.getState() == Request.State.FAILED)
			texToDraw = failedTex;
		this.failedTex = failedTex;
	}

	public TextureRegion getLoadedTex() {
		return loadedTex;
	}

	@Override
	protected void sizeChanged() {
		if (texToDraw == null)
			return;

		Texture tex = texToDraw.getTexture();
		if (tex == null)
			return;

		TextureRegion region = new TextureRegion(tex);

		float aspect = getWidth() / getHeight();
		float texAspect = tex.getWidth() * 1f / tex.getHeight();
		if (aspect > texAspect) {
			int h = (int)(tex.getHeight() * (texAspect / aspect));
			region.setRegionY((tex.getHeight() - h) / 2); // important to set pos before size
			region.setRegionWidth(tex.getWidth());
			region.setRegionHeight(h);
		}
		else {
			int w = (int)(tex.getWidth() / (texAspect / aspect));
			region.setRegionX((tex.getWidth() - w) / 2);
			region.setRegionHeight(tex.getHeight());
			region.setRegionWidth(w);
		}
		region.flip(false, true); // important to flip after all
		texToDraw = region;
	}

	/**
	 * Starts http request that will load the image.
	 * No further configuration of the request will be possible.
	 */
	public void load() {

		manager.buildRequest();
		String url = manager.getUrl();

		Texture tex = IdpAssetManager.getInstance().getTextureFromCache(url);
		if (tex != null) {
			tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			loadedTex = new TextureRegion(tex);
			texToDraw = loadedTex;
			sizeChanged();
			return;
		}

		manager.listener(new ImageListener(url) {

			@Override
			public void loaded(Texture tex) {
				tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
				loadedTex = new TextureRegion(tex);
				texToDraw = loadedTex;
				sizeChanged();
			}

			@Override
			public void failed(Throwable t) {
                texToDraw = failedTex;
			}

			@Override
			public void cancelled() {
				texToDraw = failedTex;
			}
		}).load();
	}

	@Override
	public void drawChildren(Batch batch, float parentAlpha) {
		if (manager.getState() == Request.State.CREATED)
			load();
		drawRegion(batch, parentAlpha, texToDraw);
		super.drawChildren(batch, parentAlpha);
	}

	private void drawRegion(Batch batch, float parentAlpha, TextureRegion region) {
		if (region == null)
			return;

		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
		batch.draw(region, 0, 0, getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
}
