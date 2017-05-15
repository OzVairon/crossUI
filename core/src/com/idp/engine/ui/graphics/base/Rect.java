package com.idp.engine.ui.graphics.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import com.idp.engine.resources.assets.IdpColorPixmap;

/**
 * Rectangle element with borders.
 * All borders have identical color, but it can differ from background color.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class Rect extends Group implements Disposable {

	private IdpColorPixmap background;
	private IdpColorPixmap borders;

	private float top = 0;
	private float bottom = 0;
	private float right = 0;
	private float left = 0;


	public Rect() {
		this.background = new IdpColorPixmap();
		this.borders = new IdpColorPixmap();
	}

	public Rect(float w, float h) {
		this();
		setSize(w, h);
	}

	public Rect(float x, float y, float w, float h) {
		this();
		setBounds(x, y, w, h);
	}

	public Rect(float w, float h, Color bc) {
		this(w, h);
		setBackgroundColor(bc);
	}


	public Color getBackgroundColor() {
		return background.getColor();
	}

	public void setBackgroundColor(Color color) {
		background.setColor(color);
	}

	public Color getBorderColor() {
		return borders.getColor();
	}

	public void setBorderColor(Color color) {
		borders.setColor(color);
	}

	@Override
	public void drawChildren(Batch batch, float alpha) {

		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a * alpha);
//		batch.draw(borders.getTexture(), -left, -top, getWidth() + left + right,
//				getHeight() + top + bottom);
		
		batch.draw(borders.getTexture(), -left,       0, left,       getHeight());
		batch.draw(borders.getTexture(), getWidth(),  0, right,      getHeight());
		batch.draw(borders.getTexture(), -left,        -top, getWidth() + left + right, top);
		batch.draw(borders.getTexture(), -left, getHeight(), getWidth() + left + right, bottom);
		
		batch.draw(background.getTexture(), 0, 0, getWidth(), getHeight());

		super.drawChildren(batch, alpha);
	}

	public void setBorder(float top, float right, float bottom, float left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public void setBorder(float w, float h) {
		setBorder(w, h, w, h);
	}

	public void setBorder(float size) {
		setBorder(size, size, size, size);
	}

	@Override
	public void dispose() {
		borders.dispose();
		background.dispose();
	}
}
