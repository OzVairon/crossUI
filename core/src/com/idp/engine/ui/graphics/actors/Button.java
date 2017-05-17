package com.idp.engine.ui.graphics.actors;

import com.badlogic.gdx.utils.Align;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Rect;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Button with a text.
 *
 * Created by ozvairon on 31.08.16.
 */
public class Button extends Rect {

	private Text text;


	public Button() {
		this("button");
	}


	public Button(String t) {
		this.text = new Text(t, App.getResources().getLabelStyle("h1"));
        this.text.setAlignment(Align.center);
        this.addActor(text);
	}

	public void setText(String text) {
		this.text.setText(text);
		this.text.layout();
	}

	public void setTextStyle(Label.LabelStyle style) {
		text.setStyle(style);
		text.layout();
		setHeight(text.getHeight() * 2);
	}

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        text.setSize(this.getWidth(), this.getHeight());
    }
}
