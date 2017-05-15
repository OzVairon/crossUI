package com.centergame.starttrack.graphics.starttrack_widgets.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.centergame.starttrack.StartTrackApp;

/**
 * Container that layouts its children one under another
 Properties:
   gap - vertical space between child elements
   paddingTop - space before the first element
   paddingBottom - space after the last element
   paddingLeft - x-offset of each element

 Created by ozvairon on 14.08.16.
 */
public class VLayout extends Group {
	
	private final int sp = StartTrackApp.dp2px(8);   // small
	private final int mp = StartTrackApp.dp2px(12);  // medium
	private final int lp = StartTrackApp.dp2px(16);  // large

	public enum Align {
		Left, Center, Right;
	}

	public Align align = Align.Left;
	public int gap = sp;
	public int paddingLeft   = mp;
	public int paddingRight  = mp;
	public int paddingTop    = mp;
	public int paddingBottom = mp;


	public void setAlign(Align align) {
		this.align = align;
	}

	public void layout() {
		float h = paddingTop - gap;
		for (Actor a : this.getChildren()) {
			a.setY(h + gap);
			if (align == Align.Left)
				a.setX(paddingLeft);
			if (align == Align.Center)
				a.setX(( getWidth() - a.getWidth() ) / 2);
			if (align == Align.Right)
				a.setX(getWidth()-a.getWidth() - paddingRight);
			h += a.getHeight() + gap;
		}
		setHeight(h + paddingBottom);
    }

	@Override
	protected void childrenChanged() {
		super.childrenChanged();
		layout();
	}
	
	@Override
	protected void drawChildren(Batch batch, float parentAlpha) {
		for (Actor a : getChildren()) {
			Vector2 pos = new Vector2(a.getX(), a.getY());
			pos = localToStageCoordinates(pos);
			if (pos.y + a.getHeight() < 0)
				a.setVisible(false);
			else if (pos.y > Gdx.graphics.getHeight())
				a.setVisible(false);
			else
				a.setVisible(true);


		}
		super.drawChildren(batch, parentAlpha);
	}

	@Override
	protected void setParent(Group parent) {
		super.setParent(parent);
		if (parent != null)
			setWidth(parent.getWidth());
	}

    public void setPadding(int p) {
        paddingBottom = p;
        paddingLeft = p;
        paddingRight = p;
        paddingTop = p;
        layout();
    }

    public void setGap(int gap) {
        this.gap = gap;
        layout();
    }

}
