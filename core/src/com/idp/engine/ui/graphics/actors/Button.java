package com.idp.engine.ui.graphics.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Widget;

/**
 * Button with a text.
 *
 * Created by ozvairon on 31.08.16.
 */
public class Button extends Widget<Text> {




	public Button() {
		this("button");
	}


	public Button(String t) {
		super(new Text(t, App.getResources().getLabelStyle("h1")));
        this.data.setAlignment(Align.center);
        this.addActor(data);
	}

	public void setText(String text) {
		this.data.setText(text);
		this.data.layout();
	}

	public void setTextStyle(Label.LabelStyle style) {
		data.setStyle(style);
		data.layout();
		setHeight(data.getHeight() * 2);
	}

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
		data.setSize(this.getWidth(), this.getHeight());
    }

	@Override
	protected void init() {

	}
}
