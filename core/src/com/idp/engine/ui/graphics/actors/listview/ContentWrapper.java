/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.graphics.actors.listview;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.idp.engine.ui.graphics.base.Loader;

import java.util.ArrayList;

/**
 * Wrapper class for items in the {@link ListView}.
 * Contains two loaders and the content of listview.
 * Loaders get shown if the user overscrolls the list.
 * If the user lefts (touch up event) the list maximally overscrolled, load events are fired.
 * @see ListView#requestLoad()
 * @see ListView#requestRefresh()
 * Properties:
 *   space - spacing between content's elements
 *   top/bottomLoader - loader on the top/bottom of the list.
 *     Null value automatically disables loader, not null - automatically enables it
 *   top/bottomLoaderEnabled - enables/disables loaders on top/bottom.
 *     Disabled loaders are still visible but prevent firing load events
 *   top/bottomOverscroll - how much user can overscroll the list on top/bottom
 *   top/bottomOverscrollFixed - if true, list will NOT scroll back to its normal (not overscrolled)
 *     position on touch up. Otherwise it will
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class ContentWrapper extends Group {

    private ArrayList<Actor> actors;
	private ListView listView;

	private float space;
	private Actor topLoader;
	private Actor bottomLoader;
	private final Group content;

	private float topOverScroll;
	private float bottomOverScroll;
	private boolean topOverScrollFixed;
	private boolean bottomOverScrollFixed;
	private boolean topLoaderEnabled;
	private boolean bottomLoaderEnabled;
	private Action scrollAction;

	private boolean horizontal;


	/**
	 * Creates content wrapper with null loaders.
	 */
	public ContentWrapper(ListView parent) {
		this(parent, null, null);
	}

	/**
	 * Creates content wrapper with given loaders.
	 */
	public ContentWrapper(ListView parent, Actor topLoader, Actor bottomLoader) {
		if (parent == null)
			throw new NullPointerException();
        this.actors = new ArrayList<Actor>();
		this.listView = parent;

		this.topLoader = topLoader;
		this.bottomLoader = bottomLoader;
		this.content = new Group() {
			protected void childrenChanged() {
				updateContent();
			}

            @Override
            public void addActor(Actor actor) {
                super.addActor(actor);
                actors.add(actor);
            }

			@Override
			public void clearChildren() {
				super.clearChildren();
				actors.clear();
			}

//			@Override
//			protected void drawChildren(Batch batch, float parentAlpha) {
//				int i = 0;
//				for (Actor a : getChildren()) {
//					Vector2 pos = new Vector2(a.getX(), a.getY());
//					pos = localToStageCoordinates(pos);
//					if (pos.y + a.getHeight() < 0)
//						a.setVisible(false);
//					else if (pos.y > Gdx.graphics.getHeight())
//						a.setVisible(false);
//					else
//						a.setVisible(true);
//					System.out.println(i + ": " + a.isVisible() + " " + a.getClass().getSimpleName());
//					i++;
//				}
//				super.drawChildren(batch, parentAlpha);
//			}
        };

		setTopLoader(topLoader);
		addActor(content);
		setBottomLoader(bottomLoader);

		this.space = 8;
		this.topOverScroll = 48;
		this.bottomOverScroll = 48;
		this.horizontal = false;
	}


	public Actor getTopLoader() {
		return topLoader;
	}

	public void setTopLoader(Actor topLoader) {
		if (this.topLoader != null)
			this.topLoader.remove();

		this.topLoader = topLoader;

		if (topLoader != null) {
			addActor(topLoader);
			topLoader.setY(-topLoader.getHeight());
		}
		topLoaderEnabled = (topLoader != null);
	}

	public Actor getBottomLoader() {
		return bottomLoader;
	}

	public void setBottomLoader(Actor bottomLoader) {
		if (this.bottomLoader != null)
			this.bottomLoader.remove();

		this.bottomLoader = bottomLoader;

		if (bottomLoader != null) {
			addActor(bottomLoader);
			bottomLoader.setY(content.getHeight());
		}
		bottomLoaderEnabled = (bottomLoader != null);
	}

	public boolean isTopLoaderEnabled() {
		return topLoaderEnabled;
	}

	public void setTopLoaderEnabled(boolean topLoaderEnabled) {
		this.topLoaderEnabled = topLoaderEnabled;
	}

	public boolean isBottomLoaderEnabled() {
		return bottomLoaderEnabled;
	}

	public void setBottomLoaderEnabled(boolean bottomLoaderEnabled) {
		this.bottomLoaderEnabled = bottomLoaderEnabled;
	}

	public float getTopOverScroll() {
		return topOverScroll;
	}

	public void setTopOverScroll(float topOverScroll) {
		if (topOverScroll < 0)
			throw new IllegalArgumentException("topOverScroll must be >= 0. actual: " + topOverScroll);
		this.topOverScroll = topOverScroll;
	}

	public float getBottomOverScroll() {
		return bottomOverScroll;
	}

	public void setBottomOverScroll(float bottomOverScroll) {
		if (bottomOverScroll < 0)
			throw new IllegalArgumentException("bottomOverScroll must be >= 0. actual: " + bottomOverScroll);
		this.bottomOverScroll = bottomOverScroll;
	}

	/**
	 * Sets both top and bottom overscrolls.
	 */
	public void setOverScroll(float overScroll) {
		setTopOverScroll(overScroll);
		setBottomOverScroll(overScroll);
	}

	public boolean isTopOverScrollFixed() {
		return topOverScrollFixed;
	}

	public void setTopOverScrollFixed(boolean fixed) {
		if (topOverScrollFixed == fixed)
			return;

        if (topLoader != null) {
            if (fixed) {
                if (topLoader instanceof Loader) {
                    ((Loader) topLoader).start();
                }
            } else {
                if (topLoader instanceof Loader) {
                    ((Loader) topLoader).stop();
                }
            }
        }

		topOverScrollFixed = fixed;
		if (!fixed) {
			if (getOffset() > getMaxOffset()) {
				scrollToStart(listView.getScrollBackDuration());
			}
			if (getOffset() < getMinOffset()) {
				scrollToEnd(listView.getScrollBackDuration());
			}
		}
	}

	public boolean isBottomOverScrollFixed() {
		return bottomOverScrollFixed;
	}

	public void setBottomOverScrollFixed(boolean fixed) {
		if (bottomOverScrollFixed == fixed)
			return;

        if (bottomLoader != null) {
            if (fixed) {
                if (bottomLoader instanceof Loader) {
                    ((Loader) bottomLoader).start();
                }
            } else {
                if (bottomLoader instanceof Loader) {
                    ((Loader) bottomLoader).stop();
                }
            }
        }

		bottomOverScrollFixed = fixed;
		if (!fixed) {
			if (getOffset() > getMaxOffset()) {
				scrollToStart(listView.getScrollBackDuration());
			}
			if (getOffset() < getMinOffset()) {
				scrollToEnd(listView.getScrollBackDuration());
			}
		}
	}

	public float getSpace() {
		return space;
	}

	public void setSpace(float space) {
		this.space = space;
		updateContent();
	}

	/**
	 * @return listview's content
	 */
	public Group getContent() {
		return content;
	}

	/**
	 * Scrolls the list by given deltas.
	 * One of them will probably be ignored, as list supports scrolling only align one axis.
	 * @return how much list was really scrolled.
	 *   Value may differ from delta if the list cannot scroll that far that delta expects
	 */
	public float scroll(float deltaX, float deltaY) {
		float delta = horizontal ? deltaX : deltaY;
		float initial = getOffset();
		float target = MathUtils.clamp(initial + delta,
				getMinOffsetOverScrolled(), getMaxOffsetOverScrolled());
		setOffset(target);
		return target - initial;
	}

	/**
	 * @return whether the list is scrolling at the moment.
	 *   Here scrolling means inertial movement after fling gesture
	 */
	public boolean isScrolling() {
		return scrollAction != null;
	}

	/**
	 * Stops list movement.
	 * @see #isScrolling()
	 */
	public void stopScrolling() {
		if (scrollAction != null)
			getActions().removeValue(scrollAction, true);
		scrollAction = null;
	}

	/**
	 * Smoothly scrolls list to its topmost/leftmost position.
	 * @see #scrollTo(float, float)
	 * @param seconds animation duration
	 */
	public void scrollToStart(float seconds) {
		scrollTo(getMaxOffset(), seconds);
	}

	/**
	 * Smoothly scrolls list to its bottommost/rightmost position.
	 * @see #scrollTo(float, float)
	 * @param seconds animation duration
	 */
	public void scrollToEnd(float seconds) {
		scrollTo(getMinOffset(), seconds);
	}

	/**
	 * Smoothly scrolls list to the given position.
	 * @param offset position to scroll to.
	 *   Axis is determined via {@link #isHorizontal()} method call
	 * @param seconds animation duration
	 */
	public void scrollTo(float offset, float seconds) {
		Action a;
		if (horizontal) {
			a = Actions.moveTo(
					offset,
					0,
					seconds,
					Interpolation.pow2Out
			);
		}
		else {
			a = Actions.moveTo(
					0,
					offset,
					seconds,
					Interpolation.pow2Out
			);
		}
		setScrollAction(Actions.sequence(
				a,
				new Action() {
					public boolean act(float delta) {
						scrollAction = null;
						return true;
					}
				},
				listView.getScrollBar().delayedFadeOut()
		));
	}

	/**
	 * Layout listview's content and loaders.
	 */
	public void updateContent() {
		float y = 0;
		for (Actor a : actors) {
			a.setY(y);
			y += a.getHeight() + space;
		}
		y = Math.max(y - space, 0);
		content.setHeight(y);
		setHeight(y);

		if (bottomLoader != null)
			bottomLoader.setY(Math.max(y, getParent().getHeight()));
	}

	protected float getSize() {
		return horizontal ? getWidth() : getHeight();
	}

	protected boolean isNotFull() {
		if (horizontal) {
			return getWidth() < getParent().getWidth();
		}
		return getHeight() < getParent().getHeight();
	}

	private void setScrollAction(Action action) {
		stopScrolling();
		if (action != null) {
			scrollAction = action;
			addAction(action);
		}
	}

	protected boolean isOverScroll() {
		float offset = getOffset();
		return offset > getMaxOffset() || offset < getMinOffset();
	}

	protected float getOffset() {
		return horizontal ? getX() : getY();
	}

	private void setOffset(float value) {
		if (horizontal)
			setX(value);
		else
			setY(value);
	}

	protected float getClampedOffset() {
		return MathUtils.clamp(getOffset(), getMinOffset(), getMaxOffset());
	}

	protected float getMinOffset() {
		if (!bottomOverScrollFixed)
			return getMinOffsetOverScrolled() + bottomOverScroll;
		return getMinOffsetOverScrolled();
	}

	protected float getMinOffsetOverScrolled() {

		if (isNotFull())
			return -bottomOverScroll;

		if (horizontal) {
			return getParent().getWidth() - getWidth() - bottomOverScroll;
		}
		return getParent().getHeight() - getHeight() - bottomOverScroll;
	}

	protected float getMaxOffset() {
		if (topOverScrollFixed)
			return topOverScroll;
		return 0;
	}

	protected float getMaxOffsetOverScrolled() {
		return topOverScroll;
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		Vector2 coords = new Vector2(x, y);
		coords = localToParentCoordinates(coords);
		if (coords.x < 0 || coords.x > getParent().getWidth() ||
				coords.y < 0 || coords.y > getParent().getHeight()) {
			return null;
		}
		return super.hit(x, y, touchable);
	}

	@Override
	protected void sizeChanged() {
		if (bottomLoader != null)
			bottomLoader.setY(Math.max(getHeight(), getParent().getHeight()));
	}

	protected boolean isHorizontal() {
		return horizontal;
	}

	protected void setHorizontal(boolean horizontal) {
		throw new UnsupportedOperationException(
				"vertical mode is default, horizontal mode is not implemented yet");
//		this.horizontal = horizontal;
	}
}
