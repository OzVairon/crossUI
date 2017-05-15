/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.idp.engine.resources.assets;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

/**
 * Pixmap of one pixel that supports coloring.
 * Can be drawn as:
 * <ol>
 *   <li> Texture {@link #getTexture()}</li>
 *   <li> Drawable {@link #buildDrawable()}</li>
 *   <li> Actor {@link #buildActor()}</li>
 * </ol>
 * Internal resources are managed via {@link IdpAssetManager}.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class IdpColorPixmap implements Disposable {

	private Color color;
	private Pixmap pixmap;
	private Texture tex;
	private boolean disposed;


	/**
	 * Pixmap colored with 0x00000000.
	 */
	public IdpColorPixmap() {
		this(new Color(0, 0, 0, 0));
	}

	/**
	 * Pixmap colored with the given color.
	 */
	public IdpColorPixmap(Color color) {
		this.disposed = true;
		this.color = new Color(color);
		setColor(color);
	}


	/**
	 * @return shared texture with this pixmap.
	 */
	public Texture getTexture() {
		if (disposed)
			setColor(color);
		return tex;
	}

	/**
	 * @return new instance of drawable that draws this pixmap
	 */
	public Drawable buildDrawable() {
		return new TextureRegionDrawable(new TextureRegion(getTexture()));
	}

	/**
	 * @return new instance of actor that draws this pixmap
	 */
	public Actor buildActor() {
		return new Actor() {
			public void draw(Batch batch, float parentAlpha) {
				batch.setColor(getColor());
				batch.getColor().a *= parentAlpha;
				batch.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
			}
		};
	}

	/**
	 * Paints whole pixmap with given color.
	 */
	public void setColor(Color color) {
		if (disposed)
			create();
		this.color.set(color);
		pixmap.setColor(this.color);
		pixmap.fill();
		tex.draw(pixmap, 0, 0);
	}

	/**
	 * @return pixmap's color
	 */
	public Color getColor() {
		return color;
	}

	@Override
	public void dispose() {
		if (!disposed) {
			pixmap.dispose();
			tex.dispose();
			pixmap = null;
			tex = null;
			disposed = true;
		}
	}

	private void create() {
		this.pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		this.tex = new Texture(pixmap);
		this.disposed = false;
		IdpAssetManager.getInstance().managePixmap(this);
	}
}
