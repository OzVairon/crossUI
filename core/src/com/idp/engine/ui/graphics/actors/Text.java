package com.idp.engine.ui.graphics.actors;

/**
 * UI element to display text.
 *
 * Created by ozvairon on 01.08.16.
 */
public class Text extends com.badlogic.gdx.scenes.scene2d.ui.Label {

	public Text(String text, LabelStyle s) {
		super(text, s);
	}


	@Override
	public void setText(CharSequence newText) {
		super.setText(newText);
		layout();
	}

	@Override
	public void setWrap(boolean wrap) {
		super.setWrap(wrap);
		layout();
		setHeight(getGlyphLayout().height);
	}
}
