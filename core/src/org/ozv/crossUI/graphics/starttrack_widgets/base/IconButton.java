package org.ozv.crossUI.graphics.starttrack_widgets.base;

import com.badlogic.gdx.graphics.Color;
import com.idp.engine.ui.graphics.actors.Image;
import com.idp.engine.ui.graphics.base.Rect;
import org.ozv.crossUI.TestApp;

/**
 * Button with icon.
 *
 * Created by ozvairon on 04.09.16.
 */
public class IconButton extends Rect {

	private Image icon;
	private float padding = 12;

    public IconButton(String iconname) {
        this(iconname, 48);
    }
	
	public IconButton(String iconname, float sizeDp) {
		icon = new Image(TestApp.getResources().getIcon(iconname));
		setSize(TestApp.dp2px(sizeDp), TestApp.dp2px(sizeDp));
		icon.setColor(Color.BLACK);
		this.padding = TestApp.dp2px(12);
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

	public Image getIcon() {
		return icon;
	}

	@Override
	protected void sizeChanged() {
		super.sizeChanged();
		layout();
	}
}
