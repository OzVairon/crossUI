/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.graphics.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.idp.engine.base.AppUtils;
import com.idp.engine.base.IdpGame;
import com.idp.engine.resources.assets.IdpAsset;
import com.idp.engine.ui.screens.IdpBaseScreen;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Your best friend in debugging.
 *
 *
 */
public final class IdpLogger extends Actor {

	public  static       boolean DISABLE = false;
	private static final int BUFFER_SIZE = 100;

	private final ArrayList<String> lines;
	private int lineLength;
	private int lineHeight;
	private Texture blackRectangle;
	private BitmapFont font;

	public boolean stop = false;
	public boolean logExceptions = true;
	public boolean logStackTrace = true;
	public int     maxLines = 8;

	private IdpAsset<BitmapFont> fontAsset;


	/**
	 * IdpLogger with no font.
	 * Use {@link IdpLogger#setFont(BitmapFont)} or {@link IdpLogger#setFont(IdpAsset)} to set the font.
	 */
	public IdpLogger() {
		this.lines = new ArrayList<String>(BUFFER_SIZE);
		this.lineLength = 80;
	}

	/**
	 * IdpLogger that will use given font to render its log.
	 * @param font
	 */
	public IdpLogger(BitmapFont font) {
		this();
		setFont(font);
	}

	/**
	 * IdpLogger that will use given font as soon as font gets loaded.
	 * @param fontAsset
	 */
	public IdpLogger(IdpAsset<BitmapFont> fontAsset) {
		this();
		setFont(fontAsset);
	}


	/**
	 * Adds the logger to the game's current screen.
	 * Screen is obtained via {@link IdpGame#getScreen()}.
	 */
	public void show() {
		IdpBaseScreen scr = AppUtils.game.getScreen();
		if (scr == null)
			throw new NullPointerException("Cannot show logger: screen is null");
		Stage stage = scr.getStage();
		stage.getRoot().getChildren().removeValue(this, true);
		stage.addActor(this);
	}

	/**
	 * Removes the logger from the stage.
	 */
	public void hide() {
		this.remove();
	}

	/**
	 * Sets font that will be used to render the log.
	 * @param font
	 */
	public void setFont(BitmapFont font) {
		this.font = font;
		if (font != null) {

			this.font.setColor(Color.LIGHT_GRAY);
			this.font.setUseIntegerPositions(true);
			this.lineLength = (int)(Gdx.graphics.getWidth() / font.getSpaceWidth());
			this.lineHeight = (int)(font.getCapHeight() - font.getDescent() + 2);

			if (blackRectangle != null)
				blackRectangle.dispose();
			this.blackRectangle = new Texture(1, 1, Pixmap.Format.RGB565);
		}
	}

	/**
	 * Sets the font that will be used to render the log.
	 * Font can be not loaded yet - in that case it will be used as soon as it gets loaded.
	 * @param fontAsset
	 */
	public void setFont(IdpAsset<BitmapFont> fontAsset) {
		this.fontAsset = fontAsset;
	}

	/**
	 * Writes empty string to the log.
	 */
	public void log() {
		log("");
	}

	/**
	 * Writes given string to the log.
	 * @param s string to add
	 */
	public void log(String s) {
		if (!stop) {

			boolean cut = false;
			while (s.length() >= lineLength) {
				add(s.substring(0, lineLength));
				s = s.substring(lineLength);
				cut = true;
			}

			if (s.length() != 0 || !cut) {
				add(s);
			}
		}
	}

	/**
	 * Writes exception with all its stacktrace to the log.
	 * @param ex exception to log
	 */
	public void log(Exception ex) {
		if (logExceptions) {
			log(ex.toString());
			for (StackTraceElement el : ex.getStackTrace()) {
				logStackTraceElement("  at " + el.toString());
			}
		}
	}

	/**
	 * Calls {@link IdpLogger#log(StackTraceElement[], int) with skip == 0.
	 * @param stack
	 */
	public void log(StackTraceElement[] stack) {
		log(stack, 0);
	}

	/**
	 * Logs stacktrace, skipping first {@code skip} elements.
	 * @param stack stacktrace
	 * @param skip how many elements to skip
	 */
	public void log(StackTraceElement[] stack, int skip) {
		if (logStackTrace) {
			for (int i = skip; i < stack.length; i++) {
				logStackTraceElement(stack[i].toString());
			}
		}
	}

	/**
	 * deletes all records in the log.
	 */
	public void clearLog() {
		lines.clear();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		if (DISABLE) {
			return;
		}

		if (font == null) {
			if (fontAsset != null && fontAsset.isLoaded()) {
				setFont(fontAsset.getAsset());
			}
			else {
				return;
			}
		}

		if (fontAsset != null && fontAsset.isLoaded() && fontAsset.getAsset() != font) {
			setFont(fontAsset.getAsset());
		}

		if (maxLines > 0 && font != null) {

			batch.setColor(1.0f, 1.0f, 1.0f, 0.7f);
			batch.draw(
					blackRectangle,
					getX(), getY(),
					Gdx.graphics.getWidth() * 0.5f, lineHeight * Math.min(maxLines, lines.size())
			);

			Iterator<String> it = lines.iterator();
			for (int i = 0; i < lines.size() - maxLines; i++) {
				it.next();
			}

			font.setColor(Color.LIGHT_GRAY);
			font.getData().setScale(1);
			int i = 0;
			while (it.hasNext()) {
				font.draw(batch, it.next(), getX(), i++ * lineHeight + 2);
			}
		}
	}

	private void logStackTraceElement(String s) {
		s = s.replace("com.idp.", "");
		s = s.replace("com.badlogic.", "");
		if (s.contains("("))
			s = s.substring(0, s.indexOf("("));
		log(s);
	}

	private void add(String s) {
		if (lines.size() == BUFFER_SIZE) {
			lines.remove(0);
		}
		lines.add(s);
	}
}
