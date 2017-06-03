package com.idp.engine.ui.graphics.actors.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Container that layouts its children one under another
 Properties:
   gap - vertical space between child elements
   paddingTop - space before the first element
   paddingBottom - space after the last element
   paddingLeft - x-offset of each element

 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class HorizontalLayout extends Layout{


	public int gap = sp;

	
	private boolean justified;


	public void layout() {
		float w = paddingLeft - gap;
		float h = maxH();
		for (Actor a : this.getChildren()) {
			a.setX(w + gap);
			a.setY(paddingTop + (h - a.getHeight()) / 2);
			w += a.getWidth() + gap;
		}
		
		setHeight(h + paddingTop + paddingBottom);
		
		if (justified)
			justify();
    }
	
	private void justify() {
		float w = 0;
		for (Actor a : this.getChildren()) {
			w += a.getWidth();
		}

		if (w <= getWidth() - paddingLeft - paddingRight) {
			float space = (getWidth() - paddingLeft - paddingRight - w) /
					(this.getChildren().size - 1);
			w = paddingLeft - space;
			for (Actor a : this.getChildren()) {
				a.setX(w + space);
				w += a.getWidth() + space;
			}
		}
	}
	
	public boolean isJustified() {
		return justified;
	}

	public void setJustified(boolean justified) {
		this.justified = justified;
		layout();
	}

	
	private float maxH() {
		float h = 0;
		for (Actor a : this.getChildren()) {
			h = Math.max(h, a.getHeight());
		}
		return h;
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		layout();
	}

    public void setGap(int gap) {
        this.gap = gap;
        layout();
    }

    public void setPadding(int p) {
        paddingBottom = p;
        paddingLeft = p;
        paddingRight = p;
        paddingTop = p;
        layout();
    }


	@Override
	protected void init() {

	}
}
