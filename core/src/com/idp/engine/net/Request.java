/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;

/**
 * Performs http request and manages its state.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public final class Request {

	public static enum State {
		CREATED, BUILT, LOADING, LOADED, FAILED, CANCELED
	}

	private State state;

	private HttpRequest request;
	private HttpRequestBuilder requestBuilder;
	private HttpResponseListener listener;


	/**
	 * Creates requests state manager with new {@link HttpRequestBuilder}.
	 */
	public Request() {
		this.requestBuilder = new HttpRequestBuilder().newRequest().method("GET");
		this.state = State.CREATED;
	}

	/**
	 * Creates requests state manager with new {@link HttpRequestBuilder} and sets url.
	 */
	public Request(String url) {
		this();
		requestBuilder.url(url);
	}

	/**
	 * Creates requests state manager with existing {@link HttpRequestBuilder}.
	 */
	public Request(HttpRequestBuilder builder) {
		if (builder == null)
			throw new NullPointerException();

		this.requestBuilder = builder;
		this.state = State.CREATED;
	}


	/**
	 * Builds the request using internal {@link HttpRequestBuilder}.
	 * Changes state to BUILT.
	 * @throws IllegalStateException if called NOT in CREATED state
	 */
	public void buildRequest() {
		if (state != State.CREATED) {
			throw new IllegalStateException();
		}
		state = State.BUILT;
		this.request = requestBuilder.build();
	}

	/**
	 * @return request url
	 * @throws IllegalStateException if called in CREATED state
	 */
	public String getUrl() {
		if (state == State.CREATED) {
			throw new IllegalStateException();
		}
		return request.getUrl();
	}

	/**
	 * Performs http request. Builds it if its not built yet.
	 * @throws IllegalStateException if called NOT in CREATED or BUILT state
	 */
	public void load() {
		if (state != State.CREATED && state != State.BUILT) {
			System.out.println(state);
			throw new IllegalStateException();
		}

		if (state == State.CREATED)
			this.request = requestBuilder.build();

		state = State.LOADING;
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				state = State.LOADED;
				if (listener != null) {
					listener.handleHttpResponse(httpResponse);
				}
			}

			@Override
			public void failed(Throwable t) {
				state = State.FAILED;
				if (listener != null) {
					listener.failed(t);
				}
			}

			@Override
			public void cancelled() {
				state = State.CANCELED;
				if (listener != null) {
					listener.cancelled();
				}
			}
		});
	}

	/**
	 * Cancels http request.
	 * State will be changed to CANCELED after {@link HttpResponseListener#cancelled()} is called.
	 */
	public void cancel() {
		if (state == State.LOADING) {
			Gdx.net.cancelHttpRequest(request);
		}
	}

	/**
	 * Resets state to CREATED and internal {@link HttpRequestBuilder} to its default,
	 *    sets method GET.
	 * @throws IllegalStateException if called in LOADING state
	 */
	public void reset() {
		if (state == State.LOADING)
			throw new IllegalStateException();
		state = State.CREATED;
		requestBuilder.newRequest().method("GET");
		this.request = null;
	}

	/**
	 * @return managers current state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @return internal request builder
	 */
	public HttpRequestBuilder getRequestBuilder() {
		return requestBuilder;
	}

	/**
	 * Sets listener for the response.
	 * @return this
	 * @throws IllegalStateException if called NOT in CREATED or BUILD state
	 */
	public Request listener(HttpResponseListener listener) {
		if (state != State.CREATED && state != State.BUILT) {
			throw new IllegalStateException();
		}
		this.listener = listener;
		return this;
	}

	/**
	 * @return response listener
	 */
	public HttpResponseListener getListener() {
		return listener;
	}
}
