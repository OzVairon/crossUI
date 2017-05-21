/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.idp.engine.resources.assets.IdpAssetManager;
import java.io.InputStream;

/**
 * Convenient wrapper for loading images.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public abstract class ImageListener implements Net.HttpResponseListener {

	private String url;

	/**
	 * @param url where the image is located
	 */
	public ImageListener(String url) {
		if (url == null)
			throw new NullPointerException("url cannot be null");
		this.url = url;
	}


	@Override
	public void handleHttpResponse(final Net.HttpResponse httpResponse) {

		int statusCode = httpResponse.getStatus().getStatusCode();

		if (statusCode < 200 || statusCode >= 300) {
			failed(new GdxRuntimeException("failed with code " + statusCode));
			return;
		}

		try {
			InputStream stream = httpResponse.getResultAsStream();
			Gdx2DPixmap pixmap = new Gdx2DPixmap(stream, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
			IdpAssetManager.getInstance().cachePixmap(url, pixmap);
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					loaded(IdpAssetManager.getInstance().getTextureFromCache(url));
				}
			});
		}
		catch (Exception ex) {
			failed(ex);
		}
	}

	/**
	 * Called from the rendering thread when image is downloaded.
	 *
	 * @param tex downloaded image
	 */
	public abstract void loaded(Texture tex);

	/**
	 * Called if the {@link HttpRequest} failed because an exception when processing the HTTP
	 * request, could be a timeout or any other reason (not an HTTP error).
	 * <p>
	 * Called from request thread.
	 * Use {@link com.badlogic.gdx.Application#postRunnable}
	 * if you want to do something more than print error.
	 *
	 * @param t If the HTTP request failed because an Exception, t encapsulates it to give more
	 * information.
	 */
	@Override
	public void failed(Throwable t) {
		Gdx.app.error(ImageListener.class.getName(), getClass().getName(), t);
	}

	@Override
	public void cancelled() {

	}
}
