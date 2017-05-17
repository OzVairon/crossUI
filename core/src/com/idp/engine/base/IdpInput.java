/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.base;

import com.badlogic.gdx.*;

/**
 * Helpers for input processing that wrap {@link Input}.
 * Internally holds an {@link InputMultiplexer} with two {@link InputProcessor}'s.
 * One for usual input processing in scene and other for processing backScreen key.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public final class IdpInput {

	private final InputMultiplexer multiplexer;
	private InputProcessor inputProcessor;
	private InputProcessor backKeyProcessor;


	public IdpInput() {
		this.multiplexer = new InputMultiplexer();
	}


	/**
	 * Sets scene input processor. Removes previous if one was.
	 * @param processor new input processor
	 */
	public void setInputProcessor(InputProcessor processor) {

		if (inputProcessor != null) {
			multiplexer.removeProcessor(inputProcessor);
			inputProcessor = null;
		}

		if (processor != null) {
			multiplexer.addProcessor(0, processor);
			inputProcessor = processor;
		}
	}

	/**
	 * @return scene input processor
	 */
	public InputProcessor getInputProcessor() {
		return inputProcessor;
	}

	/**
	 * Sets backScreen key processor, removes previous if one was.
	 * It always becomes a second though always receives events after scene input processor.
	 * @param processor new backScreen key processor
	 */
	public void setBackKeyProcessor(InputProcessor processor) {

		if (backKeyProcessor != null) {
			multiplexer.removeProcessor(backKeyProcessor);
			backKeyProcessor = null;
		}

		if (processor != null) {
			multiplexer.addProcessor(processor);
			backKeyProcessor = processor;
		}
	}

	/**
	 * @return backScreen key processor
	 */
	public InputProcessor getBackKeyProcessor() {
		return backKeyProcessor;
	}

	/**
	 * @see Input#setCatchBackKey(boolean)
	 */
	public void setCatchBackKey(boolean catchBack) {
		boolean oldCatchBack = Gdx.input.isCatchBackKey();
		if (catchBack == oldCatchBack)
			return;

		Gdx.input.setCatchBackKey(catchBack);

		if (backKeyProcessor != null) {
			if (catchBack && !oldCatchBack) {
				setBackKeyProcessor(backKeyProcessor);
			}
			else if (!catchBack) {
				InputProcessor i = backKeyProcessor;
				setBackKeyProcessor(null);
				this.backKeyProcessor = i;
			}
		}
	}

	/**
	 * @see Input#isCatchBackKey()
	 */
	public boolean isCatchBackKey() {
		return Gdx.input.isCatchBackKey();
	}

	/**
	 * @see Input#setOnscreenKeyboardVisible(boolean)
	 */
	public void setOnscreenKeyboardVisible(boolean visible) {
		Gdx.input.setOnscreenKeyboardVisible(visible);
	}

	/**
	 * Calls {@link Input#setInputProcessor(InputProcessor) with internal input multiplexer.
	 */
	public void startProcessing() {
		Gdx.input.setInputProcessor(multiplexer);
		Gdx.input.setCatchMenuKey(false);
	}
}
