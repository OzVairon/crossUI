/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.graphics.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.idp.engine.resources.assets.IdpColorPixmap;
import com.idp.engine.ui.graphics.base.Rect;

/**
 * Group that contains {@link TextField} and a line under it.
 * Line gets thick when textfield gets keyboard focus and thin when focus gets lost.
 * Animation seems like another line that is thicker appears
 * and its length is changing from zero to the thin line's length.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class IdpTextField extends Rect {

	private final TextField textField;
	private final Actor thinUnderline;
	private final Actor underline;
	private boolean focused;
    private final IdpColorPixmap tex;


	/**
	 * @param textField original textfield
	 */
	public IdpTextField(TextField textField) {
		if (textField == null)
			throw new NullPointerException("textField cannot be null");

		this.textField = textField;
		addActor(textField);

		this.tex = new IdpColorPixmap(Color.DARK_GRAY);

		this.thinUnderline = tex.buildActor();
		thinUnderline.setY(textField.getY() + textField.getHeight());
		thinUnderline.setHeight(1);
		thinUnderline.setWidth(textField.getWidth());
		addActor(thinUnderline);

		this.underline = tex.buildActor();
		underline.setY(textField.getY() + textField.getHeight());
		underline.setHeight(3);
		addActor(underline);
	}


	@Override
	public void act(float delta) {
		super.act(delta);

		boolean oldFocused = focused;
		focused = isFocused();
		if (!oldFocused && focused) {
			onFocus();
		}
		else if (oldFocused && !focused) {
			onBlur();
		}
	}

	/**
	 * Sets color of the line under the textfield.
	 * @param c new color
	 */
	public void setUnderlineColor(Color c) {
        tex.setColor(c);
	}

	/**
	 * @return color of the line under the textfield
	 */
	public Color getUnderlineColor() {
		return underline.getColor();
	}

	/**
	 * @param pixels thickness of a line under textfield when textfield is unfocused.
	 */
	public void setThinUnderlineThikness(float pixels) {
		thinUnderline.setHeight(pixels);
	}

	/**
	 * @return thickness of a line under textfield when textfield is unfocused.
	 */
	public float getThinUnderlineThickness() {
		return thinUnderline.getHeight();
	}

	/**
	 * @param pixels thickness of a line under textfield when textfield is focused.
	 */
	public void setUnderlineThickness(float pixels) {
		underline.setHeight(pixels);
	}

	/**
	 * @return thickness of a line under textfield when textfield is focused.
	 */
	public float getUnderlineThickness() {
		return underline.getHeight();
	}

	/**
	 * Sets left extend of the underline. Also affects underline width.
	 * Positive value means line will be longer than textfield,
	 * negative value means line will be shorter than textfield.
	 * @param pixels left extend of the underline in pixels
	 */
	public void setUnderlineLeft(float pixels) {
		thinUnderline.setX(-pixels);
		thinUnderline.setWidth(thinUnderline.getWidth() + pixels);

		underline.setX(-pixels);
		if (isFocused())
			underline.setWidth(underline.getWidth() + pixels);
	}

	/**
	 * @see #setUnderlineLeft(float)
	 * @return left extend of the underline in pixels.
	 */
	public float getUnderlineLeft() {
		return -underline.getX();
	}

	/**
	 * Sets left extend of the underline. Also affects underline width.
	 * Positive value means line will be longer than textfield,
	 * negative value means line will be shorter than textfield.
	 * @param pixels left extend of the underline in pixels
	 */
	public void setUnderlineRight(float pixels) {
		thinUnderline.setWidth(thinUnderline.getWidth() + pixels);
		if (isFocused())
			underline.setWidth(underline.getWidth() + pixels);
	}

	/**
	 * @see #setUnderlineRight(float)
	 * @return right extend of the underline in pixels
	 */
	public float getUnderlineRight() {
		return thinUnderline.getWidth() - getUnderlineLeft() - getWidth();
	}

	/**
	 * @return original {@link TextField}
	 */
	public TextField getTextField() {
		return textField;
	}

	/**
	 * @return whether the textfield has keyboard focus or not
	 */
	public boolean isFocused() {
		return getStage() != null && getStage().getKeyboardFocus() == textField;
	}

	@Override
	protected void sizeChanged() {
		textField.setSize(getWidth(), getHeight());
		thinUnderline.setWidth(getWidth());

		thinUnderline.setY(getHeight());
		underline.setY(getHeight());

		if (isFocused()) {
			underline.setWidth(getWidth());
		}
	}

	private void onFocus() {
		underline.addAction(Actions.sizeTo(
				textField.getWidth() + getUnderlineLeft() + getUnderlineRight(),
				underline.getHeight(), 0.4f, Interpolation.pow2Out));
	}

	private void onBlur() {
		underline.addAction(Actions.sizeTo(0, underline.getHeight(), 0.4f, Interpolation.pow2Out));
	}
}
