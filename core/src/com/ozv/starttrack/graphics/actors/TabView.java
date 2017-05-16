/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozv.starttrack.graphics.actors;

import com.idp.engine.ui.graphics.actors.Text;
import com.idp.engine.ui.graphics.base.Rect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.idp.engine.resources.assets.IdpColorPixmap;
import java.util.ArrayList;

/**
 * UI Element containing two views, only one can be shown simultaneously.
 * Click on corresponding header makes one view shown and another hidden.
 * Properties:
 *   headerHeight - height of the tabview's header
 *   separatorHeight - height of the line that separates headers from content
 *   separatorPadding - offset between separator and left and right bounds of the tabview
 *   underlineHeight - thickness of the line that is placed under the selected tab's header
 *   selectedTextColor - color of text in the selected tab's header
 *   unselectedTextColor - color of text in unselected tabs headers
 *   separatorColor - color of the separator
 *   underlineColor - color of the line that is placed under the selected tab's header
 *
 * @author idp
 */
public class TabView extends Rect {

	private final ArrayList<Text> headers;
	private final ArrayList<Group> contents;
	private final Actor separator;
	private final Actor underline;
	private int tabIndex;

	private float headerHeight;
	private float separatorHeight;
	private float separatorPadding;
	private float underlineHeight;

	private Color selectedTextColor;
	private Color unselectedTextColor;
	private Color separatorColor;
	private Color underlineColor;


	/**
	 * @param names tab headers
	 */
	public TabView(String[] names) {

		this.headerHeight = com.ozv.starttrack.StartTrackApp.dp2px(32);
		this.separatorHeight = com.ozv.starttrack.StartTrackApp.dp2px(1);
		this.separatorPadding = com.ozv.starttrack.StartTrackApp.dp2px(12);
		this.underlineHeight = com.ozv.starttrack.StartTrackApp.dp2px(2);

		this.selectedTextColor = Color.valueOf("202020");
		this.unselectedTextColor = Color.valueOf("666666");
		this.separatorColor = Color.LIGHT_GRAY;
		this.underlineColor = Color.DARK_GRAY;

		this.tabIndex = -1;
		this.headers = new ArrayList<Text>();
		this.contents = new ArrayList<Group>();

		int i = 0;
		for (String name : names) {
			final int ii = i;
			LabelStyle s = com.ozv.starttrack.StartTrackApp.getResources().getLabelStyle("tabs_header");
			s.fontColor = unselectedTextColor;
			Text t = new Text(name.toUpperCase(), s);
			t.setAlignment(Align.center);
			t.addListener(new ActorGestureListener() {
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    selectTab(ii);
                }
            });
			headers.add(t);
			addActor(t);

			Group g = new Group() {
                @Override
                public void addActor(Actor actor) {
                    super.addActor(actor);
                    this.setHeight(actor.getHeight());
                    actor.setPosition(0, 0);
                }
            };
			g.setVisible(false);
			contents.add(g);
			addActor(g);

			i++;
		}

		this.separator = new IdpColorPixmap(separatorColor).buildActor();
		addActor(separator);

		this.underline = new IdpColorPixmap(underlineColor).buildActor();
		addActor(underline);

		setBackgroundColor(Color.valueOf("fafafa"));
		setBorder(com.ozv.starttrack.StartTrackApp.dp2px(1), 0, com.ozv.starttrack.StartTrackApp.dp2px(1), 0);
		setBorderColor(Color.LIGHT_GRAY);
        layout();
	}


	/**
	 * Selects tab with given index.
	 */
	public void selectTab(int index) {
		if (index == tabIndex)
			return;

		if (tabIndex >= 0 && tabIndex < contents.size()) {
			contents.get(tabIndex).setVisible(false);
			onTabUnselected(tabIndex, headers.get(tabIndex), contents.get(tabIndex));
		}

		else {
			Text header = headers.get(index);
			underline.setPosition(header.getX() + header.getWidth() / 2, headerHeight);
		}

		contents.get(index).setVisible(true);
		tabIndex = index;
		onTabSelected(index, headers.get(index), contents.get(index));
        layout();
	}

	/**
	 * Called when tab gets selected, after {@link #onTabUnselected}.
	 * @param index index of selected tab
	 * @param header tab's header
	 * @param content tab's content
	 */
	public void onTabSelected(int index, Text header, Group content) {
		header.getStyle().fontColor = selectedTextColor;
		header.setStyle(header.getStyle());

		underline.addAction(Actions.parallel(
                Actions.moveTo(
//						header.getX() + header.getWidth() / 2 - header.getGlyphLayout().width / 2 - underlinePadding,
                        header.getX(),
                        headerHeight, 0.4f, Interpolation.pow2Out
                ),
                Actions.sizeTo(
                        //header.getGlyphLayout().width + 2 * underlinePadding,
                        header.getWidth(),
                        underline.getHeight(), 0.4f, Interpolation.pow2Out
                )
        ));
	}

	/**
	 * Called when tab gets unselected, before {@link #onTabSelected}.
	 * @param index index of the unselected tab
	 * @param header tab's header
	 * @param content tab's content
	 */
	public void onTabUnselected(int index, Text header, Group content) {
		header.getStyle().fontColor = unselectedTextColor;
		header.setStyle(header.getStyle());
	}

	/**
	 * @param index tab index
	 * @return header for the tab having the given tab index
	 */
	public Text getHeader(int index) {
		return headers.get(index);
	}

	/**
	 * @param index tab index
	 * @return content for the tab having the given tab index
	 */
	public Group getContent(int index) {
		return contents.get(index);
	}

	/**
	 * @return index of the current tab
	 */
	public int getTabIndex() {
		return tabIndex;
	}

	public float getHeaderHeight() {
		return headerHeight;
	}

	public void setHeaderHeight(float headerHeight) {
		this.headerHeight = headerHeight;
	}

	public float getSeparatorHeight() {
		return separatorHeight;
	}

	public void setSeparatorHeight(float separatorHeight) {
		this.separatorHeight = separatorHeight;
	}

	public float getSeparatorPadding() {
		return separatorPadding;
	}

	public void setSeparatorPadding(float separatorPadding) {
		this.separatorPadding = separatorPadding;
	}

	public float getUnderlineHeight() {
		return underlineHeight;
	}

	public void setUnderlineHeight(float underlineHeight) {
		this.underlineHeight = underlineHeight;
	}

	public Color getSelectedTextColor() {
		return selectedTextColor;
	}

	public void setSelectedTextColor(Color selectedTextColor) {
		this.selectedTextColor = selectedTextColor;
	}

	public Color getUnselectedTextColor() {
		return unselectedTextColor;
	}

	public void setUnselectedTextColor(Color unselectedTextColor) {
		this.unselectedTextColor = unselectedTextColor;
	}

	public Color getSeparatorColor() {
		return separatorColor;
	}

	public void setSeparatorColor(Color separatorColor) {
		this.separatorColor = separatorColor;
	}

	public Color getUnderlineColor() {
		return underlineColor;
	}

	public void setUnderlineColor(Color underlineColor) {
		this.underlineColor = underlineColor;
	}

	@Override
	protected void sizeChanged() {
		int length = headers.size();
		int i = 0;
		for (Text header : headers) {
			header.setWidth(getWidth() / length);
			header.setX(getWidth() / length * i);
			header.setHeight(headerHeight);
			Group g = contents.get(i);
			g.setWidth(getWidth());
			g.setY(headerHeight + separatorHeight);
			//g.setHeight(getHeight() - g.getY());
			i++;
		}
		separator.setSize(getWidth() - separatorPadding * 2, separatorHeight);
		separator.setPosition(separatorPadding, headerHeight);

		underline.setHeight(underlineHeight);
	}

    private void layout() {
        float h = headerHeight + separatorHeight;
        if (!contents.isEmpty() && tabIndex >=0) {
            h += getContent(tabIndex).getHeight();
        }
        setHeight(h);
    }
}
