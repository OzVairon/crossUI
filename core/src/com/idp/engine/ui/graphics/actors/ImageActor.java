package com.idp.engine.ui.graphics.actors;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.idp.engine.ui.graphics.base.Widget;

/**
 * A basic ui element with a texture.
 * Properties:
 *   sprite - a texture region that will be rendered
 *
 *
 */
public class ImageActor extends Widget<TextureRegion> {

	private TextureRegion sprite;




	public ImageActor(TextureRegion sprite) {
		super(sprite);
	}

	@Override
	protected void init() {

	}



	public void setSprite(TextureRegion sprite) {
		this.data = sprite;
	}

	public TextureRegion getSprite() {
		return data;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawRegion(data, batch, parentAlpha);
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

