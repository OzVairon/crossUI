package com.idp.engine.ui.graphics.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Rect;
import com.idp.engine.ui.graphics.base.Widget;

/**
 * Checkbox element.
 *
 * Created by ozvairon on 31.08.16.
 */
public class CheckBox extends Widget<Boolean> {

	private Text text;
	private Rect box;
	private ImageActor checker;


	public CheckBox() {
		this("");
	}

	public CheckBox(String text) {
		this(text, false);
	}


	public CheckBox(String text, final boolean checked) {
		super(checked);
		this.text = new Text(text, App.getResources().getLabelStyle("h2"));
		checker = new ImageActor(App.getResources().getIcon("checker"));

	}

	public void setChecked(boolean c) {
		this.data = c;
		checker.setVisible(c);
		if (c) {
			box.setBorderColor(Color.valueOf("666666"));
		}
		else {
			box.setBorderColor(Color.valueOf("999999"));
		}
		afterChange();
	}

	public boolean isChecked() {
		return data;
	}

	private void layout() {
		text.setY((getHeight() - text.getHeight()) / 2);
		box.setY((getHeight() - box.getHeight()) / 2);
		box.setX(getWidth() - box.getWidth());
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		layout();
	}

	public void afterChange() {

	}

	@Override
	protected void init() {
		checker.setSize(App.dp2px(14), App.dp2px(14));
		checker.setColor(Color.valueOf("666666"));

		box = new Rect();
		box.setSize(App.dp2px(18), App.dp2px(18));
		box.setBorder(App.dp2px(1));

		box.addActor(checker);
		checker.setPosition(App.dp2px(2), App.dp2px(2));

		addListener(new ActorGestureListener() {
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				super.tap(event, x, y, count, button);
				setChecked(isChecked());
			}
		});
		box.setBackgroundColor(Color.WHITE);
		addActor(this.text);
		addActor(box);
		setChecked(data);
		setHeight(App.dp2px(48));
	}
}
