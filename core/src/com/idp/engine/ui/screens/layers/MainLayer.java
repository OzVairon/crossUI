/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.screens.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.idp.engine.ui.graphics.base.Navbar;

/**
 * Basic layer for all elements.
 *
 *
 */
public class MainLayer extends Layer {

	public final Navbar navbar;
	public Group content;


	public MainLayer() {
		this.navbar = new Navbar();
		this.content = new Group();
		content.setName("content");
		content.setY(navbar.getHeight());
		content.setSize(getWidth(), Gdx.graphics.getHeight() - navbar.getHeight());
		addActor(navbar);
		addActor(content);
	}
}
