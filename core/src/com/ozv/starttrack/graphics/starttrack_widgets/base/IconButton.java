package com.ozv.starttrack.graphics.starttrack_widgets.base;

import com.badlogic.gdx.graphics.Color;
import com.idp.engine.ui.graphics.actors.ImageActor;
import com.idp.engine.ui.graphics.base.Rect;

/**
 * Button with icon.
 *
 * Created by ozvairon on 04.09.16.
 */
public class IconButton extends Rect {

	private ImageActor icon;
	private float padding = 12;

    public IconButton(String iconname) {
        this(iconname, 48);
    }
	
	public IconButton(String iconname, float sizeDp) {
		icon = new ImageActor(com.ozv.starttrack.StartTrackApp.getResources().getIcon(iconname));
		setSize(com.ozv.starttrack.StartTrackApp.dp2px(sizeDp), com.ozv.starttrack.StartTrackApp.dp2px(sizeDp));
		icon.setColor(Color.BLACK);
		this.padding = com.ozv.starttrack.StartTrackApp.dp2px(12);
		addActor(icon);

		layout();
	}
	

	public void layout() {
		icon.setWidth(getWidth() - padding * 2);
		icon.setHeight(getHeight() - padding * 2);
		icon.setX((getWidth() - icon.getWidth()) / 2);
		icon.setY((getHeight() - icon.getHeight()) / 2);
	}

	public void setPadding(float padding) {
		this.padding = padding;
		layout();
	}

	public ImageActor getIcon() {
		return icon;
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		layout();
	}
}
