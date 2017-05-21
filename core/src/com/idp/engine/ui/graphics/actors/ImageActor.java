package com.idp.engine.ui.graphics.actors;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * A basic ui element with a texture.
 * Properties:
 *   sprite - a texture region that will be rendered
 *
 *
 */
public class ImageActor extends Group {

	private TextureRegion sprite;


	public ImageActor() {

	}

	public ImageActor(TextureRegion sprite) {
		this.sprite = sprite;
	}


	public void setSprite(TextureRegion sprite) {
		this.sprite = sprite;
	}

	public TextureRegion getSprite() {
		return sprite;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawRegion(sprite, batch, parentAlpha);
	}

	protected void drawRegion(TextureRegion region, Batch batch, float parentAlpha) {
		if (region != null) {
			Color c = getColor();
			batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
			batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}
	}
}

