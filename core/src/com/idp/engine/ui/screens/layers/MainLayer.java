/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.screens.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.idp.engine.ui.graphics.actors.layouts.AbsoluteLayout;
import com.idp.engine.ui.graphics.actors.layouts.HorizontalLayout;
import com.idp.engine.ui.graphics.actors.layouts.Layout;
import com.idp.engine.ui.graphics.actors.layouts.VerticalLayout;
import com.idp.engine.ui.graphics.base.Navbar;

/**
 * Basic layer for all elements.
 *
 *
 */
public class MainLayer extends Layer {

	public final Navbar navbar;
	public Layout content;


	public MainLayer() {
		this.navbar = new Navbar();
		addActor(navbar);
		setContentLayout(LayoutType.Vertical);
		addActor(content);
	}

	public void setContentLayout(LayoutType type) {
		Layout layout;
		switch (type) {
			case Absolute: {
				layout = new AbsoluteLayout();
				break;
			}
			case Vertical: {
				layout = new VerticalLayout();
				break;
			}
			case Horizontal: {
				layout = new HorizontalLayout();
				break;
			} default: {
				return;
			}
		}

		layout.clear();
		if (this.content != null) {
			for (Actor a : content.getChildren()) {
				layout.addActor(a);
			}
		}
		content = layout;
		content.setFixHeight(true);
		content.setFixWidth(true);
		content.setPadding(0);
		content.setName("content");
		content.setY(navbar.getHeight());
		content.setSize(getWidth(), Gdx.graphics.getHeight() - navbar.getHeight());
	}



	public enum LayoutType {
		Vertical, Horizontal, Absolute
	}
}
