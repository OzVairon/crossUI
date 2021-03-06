package com.idp.engine.ui.graphics.actors;

import com.centergame.starttrack.StartTrackApp;
import com.idp.engine.ui.graphics.base.Rect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * Checkbox element.
 *
 * Created by ozvairon on 31.08.16.
 */
public class CheckBox extends Rect {

	private boolean checked;
	private Text text;
	private Rect box;
	private com.idp.engine.ui.graphics.actors.ImageActor checker;


	public CheckBox() {
		this("");
	}

	public CheckBox(String text) {
		this(text, false);
	}


	public CheckBox(String text, final boolean checked) {
		this.text = new Text(text, StartTrackApp.getResources().getLabelStyle("h2"));
		checker = new com.idp.engine.ui.graphics.actors.ImageActor(StartTrackApp.getResources().getIcon("checker"));
		checker.setSize(StartTrackApp.dp2px(14), StartTrackApp.dp2px(14));
		checker.setColor(Color.valueOf("666666"));

		box = new Rect();
		box.setSize(StartTrackApp.dp2px(18), StartTrackApp.dp2px(18));
		box.setBorder(StartTrackApp.dp2px(1));

		box.addActor(checker);
		checker.setPosition(StartTrackApp.dp2px(2), StartTrackApp.dp2px(2));

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
		setChecked(checked);
		setHeight(StartTrackApp.dp2px(48));
	}

	public void setChecked(boolean c) {
		this.checked = c;
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
		return checked;
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
}
