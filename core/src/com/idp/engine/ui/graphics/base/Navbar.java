/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.graphics.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.Image;
import com.idp.engine.ui.graphics.actors.Text;

/**
 * Navigation bar element.
 * Contains text, supports dropdown, also may contain userIcons to the left or to the right of the text.
 *
 *
 */
public class Navbar extends Rect {

	private final Text text;
	private final Group textGroup;
	private final Group leftIcons;
	private final Group rightIcons;

	private Color contentColor;
	private final float iconSize;



	public Navbar() {

		setName("navbar");
		this.contentColor = App.Colors.TEXT_NAVBAR;
		this.iconSize = App.dp2px(40);
		float h = App.dp2px(56);

		setSize(Gdx.graphics.getWidth(), h);
		setBackgroundColor(Color.CLEAR);
		float padleft = App.dp2px(8);
		float padright = App.dp2px(8);


		textGroup = new Group();
		textGroup.setSize(Gdx.graphics.getWidth() - App.dp2px(168), App.dp2px(24));


		this.text = new Text("", App.getResources().getLabelStyle("navbar"));
		text.setWidth(Gdx.graphics.getWidth() - App.dp2px(168));
		text.setHeight(App.dp2px(24));
		text.setAlignment(Align.center);


		textGroup.addActor(text);
		text.setY((textGroup.getHeight() - text.getHeight()) / 2);
		text.setX((textGroup.getWidth() - text.getWidth()) / 2);


		this.leftIcons = new Group();
		leftIcons.setSize(iconSize, iconSize);
		leftIcons.setX(padleft);
		leftIcons.setY((h - iconSize) / 2);


		this.rightIcons = new Group();
		rightIcons.setSize(iconSize, iconSize);
		rightIcons.setX(getWidth() - padright - iconSize);
		rightIcons.setY((h - iconSize) / 2);

		addActor(leftIcons);
		addActor(textGroup);
		addActor(rightIcons);

		textGroup.setY((getHeight() - text.getHeight()) / 2);
		textGroup.setX((getWidth() - textGroup.getWidth()) / 2);
	}

	public void setTitle(String name) {
		text.setText(name);
	}

	public Group getTextGroup() {
		return textGroup;
	}

	public void setContentColor(Color contentColor) {
		this.contentColor = contentColor;
		text.getStyle().fontColor = contentColor;
		for (Actor a : leftIcons.getChildren()) {
			a.setColor(contentColor);
		}
		for (Actor a : rightIcons.getChildren()) {
			a.setColor(contentColor);
		}
	}

	public Color getContentColor() {
		return contentColor;
	}

	public float getIconSize() {
		return iconSize;
	}

	public Group getLeftIcons() {
		return leftIcons;
	}

	public Group getRightIcons() {
		return rightIcons;
	}

	public static class NavButton extends Rect {

		private final Image icon;
		private final float padding;

		public NavButton(String name) {
			this.icon = new Image(App.getResources().getIcon(name));
			this.padding = App.dp2px(12);
			this.icon.setColor(App.Colors.TEXT_NAVBAR);
			addActor(icon);
			setSize(App.dp2px(40), App.dp2px(40));

		}

		private void layout() {
			icon.setSize(getWidth() - padding * 2, getHeight() - padding * 2);
			icon.setX((getWidth() - icon.getWidth()) / 2);
			icon.setY((getHeight() - icon.getHeight()) / 2);
		}

		@Override
		protected void sizeChanged() {
			super.sizeChanged();
			layout();
		}

		@Override
		public void setColor(Color color) {
			super.setColor(color);
			icon.setColor(color);
		}

	}
}
