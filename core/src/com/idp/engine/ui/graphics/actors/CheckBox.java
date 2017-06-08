package com.idp.engine.ui.graphics.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Rect;
import com.idp.engine.ui.graphics.base.Widget;

/**
 * Checkbox element.
 *
 * Created by ozvairon on 31.08.16.
 */
public class CheckBox extends Widget {

	private Text text;
	private Rect box;
	private Image checker;
	private boolean data;

	private Color colorChecked = Color.valueOf("666666");
	private Color colorUnChecked = Color.valueOf("999999");

	public CheckBox() {
		this("");
	}

	public CheckBox(String text) {
		this(text, false);
	}


	public CheckBox(String text, final boolean checked) {
		this.text = new Text(text, App.getResources().getLabelStyle("text"));
		checker = new Image(App.getResources().getIcon("accept"));
		data = checked;
		init();
	}

	public void setChecked(boolean c) {
		this.data = c;
		checker.setVisible(c);
		if (c) {
			box.setBorderColor(colorChecked);
		}
		else {
			box.setBorderColor(colorUnChecked);
		}
		afterChange();
	}

	public boolean isChecked() {
		return data;
	}

	private void layout() {
		text.setY((getHeight() - text.getHeight()) / 2);
		box.setY((getHeight() - box.getHeight()) / 2);
		box.setX(0);
		text.setX(box.getWidth() + App.dp2px(8));
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		layout();
	}

	public void afterChange() {	}

	@Override
	protected void init() {
		checker.setSize(App.dp2px(14), App.dp2px(14));
		checker.setColor(colorChecked);
		box = new Rect();
		box.setSize(App.dp2px(18), App.dp2px(18));
		box.setBorder(App.dp2px(1));

		box.addActor(checker);
		checker.setPosition(App.dp2px(2), App.dp2px(2));

		addListener(new ActorGestureListener() {
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				super.tap(event, x, y, count, button);
				setChecked(!isChecked());
			}
		});
		box.setBackgroundColor(Color.CLEAR);
		addActor(this.text);
		addActor(box);
		setChecked(data);
		setHeight(App.dp2px(48));
	}

	public void setColorChecked(Color c) {
		colorChecked = c;
		checker.setColor(colorChecked);
	}

	public void setColorUnChecked(Color c) {
		colorUnChecked = c;
	}

	public void setText(String text) {
		this.text.setText(text);
	}

	public void setTextStyle(Label.LabelStyle style) {
		this.text.setStyle(style);
	}

	public void setFlagColor(Color color) {
		checker.setColor(color);
	}
}
