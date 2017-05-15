/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.idp.engine.ui.graphics.actors.listview;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.idp.engine.resources.assets.IdpColorPixmap;

/**
 * Scrollbar element for ListView.
 * Can be horizontal or vertical - orientation is automatically picked from the {@link ListView}.
 * Properties:
 *   thickness - thickness of the scrollbar in pixels
 *   offset - offset of the left(top) side of scrollbar from the right(bottom) side of the {@link ListView}.
 *   texture - visual representation of a scrollbar
 *   fadeDuration - duration of a fade animation
 *   fadeDelay - delay from the end of a scroll action or a touch up event to the start of a fadeout action
 *   normalAlpha - alpha that applied to the texture when the scrollbar is shown
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class ScrollBar extends Actor {

	private final ListView listView;

	private float thickness;
	private float offset;
	private final IdpColorPixmap tex;

	private float fadeDuration;
	private float fadeDelay;
	private float normalAlpha;


	public ScrollBar(ListView parent) {
		if (parent == null)
			throw new NullPointerException();
		this.listView = parent;

		this.thickness = 2;
		this.offset = 4;
		this.tex = new IdpColorPixmap();
		tex.setColor(Color.WHITE);
		this.fadeDuration = 0.2f;
		this.fadeDelay = 0.5f;
		this.normalAlpha = 0.8f;
		setColor(new Color(0.4f, 0.4f, 0.4f, normalAlpha));
	}


	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		this.offset = offset;
	}

	public Texture getTexture() {
		return tex.getTexture();
	}

	public float getFadeDuration() {
		return fadeDuration;
	}

	public void setFadeDuration(float fadeDuration) {
		this.fadeDuration = fadeDuration;
	}

	public float getFadeDelay() {
		return fadeDelay;
	}

	public void setFadeDelay(float fadeDelay) {
		this.fadeDelay = fadeDelay;
	}

	public float getNormalAlpha() {
		return normalAlpha;
	}

	public void setNormalAlpha(float normalAlpha) {
		this.normalAlpha = normalAlpha;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		com.idp.engine.ui.graphics.actors.listview.ContentWrapper content = listView.getContentWrapper();

		if (content.isNotFull() || content.getSize() <= 0)
			return;


		float contentSize = content.getSize();
		if (content.isTopOverScrollFixed())
			contentSize -= content.getTopOverScroll();
		if (content.isBottomOverScrollFixed())
			contentSize -= content.getBottomOverScroll();

		float size = (content.isHorizontal() ? listView.getWidth() : listView.getHeight());
		float length = size * size / contentSize;
		float f = -content.getClampedOffset();
		float pos = f / ((contentSize - size)) * (size - length);


		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);

		if (content.isHorizontal()) {
			batch.draw(tex.getTexture(), pos, listView.getHeight() - offset - thickness, length, thickness);
		}
		else {
			batch.draw(tex.getTexture(), listView.getWidth() - offset - thickness, pos, thickness, length);
		}
	}

	/**
	 * Shows scroll bar.
	 * Alpha goes from 0 to normalAlpha.
	 */
	public void fadeIn() {
		if (state == FadeState.IN)
			return;
		state = FadeState.IN;
		clearActions();
		addAction(Actions.sequence(
				Actions.alpha(normalAlpha, fadeDuration, Interpolation.pow2Out),
				setFadeState(FadeState.NULL)
		));
	}

	/**
	 * Hides scroll bar.
	 * Alpha goes from normalAlpha to 0.
	 */
	public void fadeOut() {
		if (state == FadeState.OUT)
			return;
		if (state != FadeState.IN)
			clearActions();
		state = FadeState.OUT;
		addAction(Actions.sequence(
				Actions.delay(fadeDelay),
				Actions.alpha(0, fadeDuration, Interpolation.pow2Out),
				setFadeState(FadeState.NULL)
		));
	}

	/**
	 * Creates (but not adds it to the scrollbar) action that will fade out scrollbar after {@code fadeDelay} seconds.
	 * @return newly created delayed fadeout action
	 */
	Action delayedFadeOut() {
		return Actions.addAction(
				Actions.sequence(
						Actions.delay(fadeDelay),
						setFadeState(FadeState.OUT),
						Actions.alpha(0, fadeDuration, Interpolation.pow2Out),
						setFadeState(FadeState.NULL)
				), this
		);
	}

	private static enum FadeState {
		IN, OUT, NULL
	}
	private FadeState state;

	private Action setFadeState(final FadeState state) {
		return new Action() {
			public boolean act(float delta) {
				ScrollBar.this.state = state;
				return true;
			}
		};
	}
}
