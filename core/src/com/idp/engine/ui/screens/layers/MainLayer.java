/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.screens.layers;

import com.badlogic.gdx.Gdx;
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
		setContentLayout(LayoutType.Absolute);
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
//			for (Actor a : content.getChildren()) {
//				layout.addActor(a);
//			}
			this.removeActor(this.content);
		}

		this.addActor(layout);

		content = layout;
		content.setFixHeight(true);
		content.setFixWidth(true);
		content.setPadding(8);
		content.setName("content");
		content.setY(navbar.getHeight());
		content.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - navbar.getHeight());
	}

	public Layout getContent() {
		return content;
	}

	public enum LayoutType {
		Vertical, Horizontal, Absolute
	}
}
