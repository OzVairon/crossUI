/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.idp.engine.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Basic listener for requests with JSON response.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public abstract class IdpJsonListener implements Net.HttpResponseListener {

	private String response;


	@Override
	public void handleHttpResponse(final Net.HttpResponse httpResponse) {

		try {
			this.response = new String(httpResponse.getResult(), "utf-8");
		}
		catch (UnsupportedEncodingException ex) {
			failed(ex);
		}

		int statusCode = httpResponse.getStatus().getStatusCode();
		if (statusCode < 200 || statusCode >= 300) {
			if (statusCode == -1) {
				failed(new GdxRuntimeException("request timed out"));
			}
			else {
				System.out.println(response);
				//failed(new GdxRuntimeException("request failed with code " + statusCode));
                failed(new GdxRuntimeException(response));
			}
			return;
		}
		try {
			final Map<String, List<String>> headers = httpResponse.getHeaders();
			Gdx.app.postRunnable(new Runnable () {
				public void run() {
					loaded(response, headers);
				}
			});
		}
		catch (Exception ex) {
			failed(ex);
		}
	}

	/**
	 * Called from the rendering thread when json gets downloaded.
	 *
	 * @param json response
	 * @param headers http headers
	 */
	public abstract void loaded(String json, Map<String, List<String>> headers);

	/**
	 * Called if the {@link HttpRequest} failed because an exception when processing the HTTP request,
	 * could be a timeout or any other reason (not an HTTP error).
	 * <p>
	 * Called from request thread. Use {@link com.badlogic.gdx.Application#postRunnable}
	 * if you want to do something more than print error.
	 * @param t If the HTTP request failed because an Exception, t encapsulates it to give more information.
	 */
	@Override
	public void failed(Throwable t) {
		Gdx.app.error(IdpJsonListener.class.getName(), getClass().getName(), t);
	}

	@Override
	public void cancelled() {

	}

	/**
	 * Parses the response and extracts error info.
	 * @return error info from response or null
	 */
	public String getError() {
		try {
			JsonObject o = new JsonParser().parse(response).getAsJsonObject();
			return o.get("error").getAsString();
		}
		catch (Exception ex) {
			return null;
		}
	}
}
