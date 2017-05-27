/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.screens.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.idp.engine.ui.graphics.actors.layouts.Layout;
import com.idp.engine.ui.graphics.actors.layouts.VLayout;
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
		setContentLayout(new VLayout());
		addActor(content);
	}

	void setContentLayout(Layout layout) {

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
}
