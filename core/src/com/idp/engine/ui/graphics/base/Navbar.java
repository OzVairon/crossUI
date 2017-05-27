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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.ImageActor;
import com.idp.engine.ui.graphics.actors.Text;

/**
 * Navigation bar element.
 * Contains text, supports dropdown, also may contain icons to the left or to the right of the text.
 *
 *
 */
public class Navbar extends Rect {

	private final com.idp.engine.ui.graphics.actors.Text text;
	private final Group textGroup;
	private final Group leftIcons;
	private final Group rightIcons;

	private Color contentColor;
	private final float iconSize;
	private boolean hasDropDown;

	private Rect dropdownMenu;


	public Navbar() {


		setName("navbar");
		this.contentColor = App.Colors.TEXT_NAVBAR;
		this.iconSize = App.dp2px(40);
		float h = App.dp2px(56);

		setSize(Gdx.graphics.getWidth(), h);
		setBorder(0, 0, 0, 0);
		setBorderColor(Color.BLACK);
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

	public void setText(String name) {
		text.setText(name);
		if (hasDropDown) {
			float x = text.getX() + (text.getWidth() + text.getGlyphLayout().width) * 0.5f +
							App.dp2px(8);
			textGroup.findActor("chevron").setX(x);
		}
	}

	public Group getTextGroup() {
		return textGroup;
	}

	public void setDropdownMenu(Rect menu) {
		dropdownMenu = menu;
		if (menu != null) {
			menu.setVisible(false);
			if (!hasDropDown)
				addChevron();
			this.hasDropDown = true;
		}
		else {
			if (hasDropDown)
				removeChevron();
			this.hasDropDown = false;
		}
	}

	public void openMenu() {
		getParent().addActorBefore(this, dropdownMenu);
		dropdownMenu.clearActions();
		dropdownMenu.setY(getHeight() - dropdownMenu.getHeight());
		dropdownMenu.setVisible(true);
		dropdownMenu.setTouchable(Touchable.enabled);
		dropdownMenu.addAction(Actions.moveTo(0, getHeight(), 0.2f));
	}

	public void closeMenu() {
		dropdownMenu.clearActions();
		dropdownMenu.addAction(Actions.sequence(Actions.moveTo(0, getHeight() - dropdownMenu.getHeight(), 0.2f),
				Actions.visible(false),
				Actions.touchable(Touchable.disabled),
				Actions.removeActor(dropdownMenu)
		));
	}

	public void flipChevron() {
		com.idp.engine.ui.graphics.actors.ImageActor i = textGroup.findActor("chevron");
		if (i != null)
			i.getSprite().flip(false, true);
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

	public Group getLeftIcons() {
		return leftIcons;
	}

	public Group getRightIcons() {
		return rightIcons;
	}

	public float getIconSize() {
		return iconSize;
	}

	private void addChevron() {
		ImageActor i = new ImageActor(App.getResources().getIcon("chevron"));
		i.setColor(Color.BLACK);
		i.setSize(App.dp2px(12), App.dp2px(12));
		i.setName("chevron");
		textGroup.addActor(i);
		i.setX(text.getX() + (text.getWidth() + text.getGlyphLayout().width) * 0.5f + App.dp2px(
				8));
		i.setY((textGroup.getHeight() - i.getHeight()) / 2);
	}

	private void removeChevron() {
		if (textGroup.findActor("chevron") != null) {
			textGroup.removeActor(textGroup.findActor("chevron"));
		}
	}

	public static class NavButton extends Rect {

		private final com.idp.engine.ui.graphics.actors.ImageActor icon;
		private final float padding;

		public NavButton(String name) {
			this.icon = new ImageActor(App.getResources().getIcon(name));
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
