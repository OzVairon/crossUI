/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.graphics.actors.listview;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;


/**
 * ListView slider widget.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class ListView extends Group {

	protected final ContentWrapper wrapper;
	protected final Group content;

	private final Rectangle bounds;
	private final Rectangle scissors;

	private float deceleration;
	private float scrollBackDuration;

	private ScrollBar scrollBar;


	public ListView() {
		this(null, null);
	}


	/**
	 * Creates list view and initializes its loaders with given loaders.
	 */
	public ListView(Actor topLoader, Actor bottomLoader) {
		this.scrollBar = new ScrollBar(this);
		this.wrapper = new ContentWrapper(this, topLoader, bottomLoader);
		this.content = wrapper.getContent();

		this.bounds = new Rectangle();
		this.scissors = new Rectangle();

		this.deceleration = 9000;
		this.scrollBackDuration = 0.5f;

		this.addActor(wrapper);
		this.addActor(scrollBar);
		this.addListener(new ListViewListener(this));
	}


	/**
	 * Called when listview receives touch up event with fully visible top loader.
	 */
    public void requestRefresh() {

	}

	/**
	 * Called when listview receives touch up event with fully visible bottom loader.
	 */
	public void requestLoad() {

	}


	@Override
	public void drawChildren(Batch batch, float parentAlpha) {
		ScissorStack.calculateScissors(
				getStage().getCamera(),
				batch.getTransformMatrix(),
				bounds,
				scissors
		);
		batch.flush();
		boolean pushed = ScissorStack.pushScissors(scissors);
		super.drawChildren(batch, parentAlpha);
		if (pushed) {
			batch.flush();
			ScissorStack.popScissors();
		}
	}

	@Override
	protected void sizeChanged() {
		if (wrapper.isHorizontal()) {
			wrapper.setHeight(getHeight());
		}
		else {
			wrapper.setWidth(getWidth());
		}
		wrapper.sizeChanged();
		bounds.setSize(getWidth(), getHeight());
	}

	/**
	 * @return listview's content
	 */
	public Group getContent() {
		return wrapper.getContent();
	}

	/**
	 * @return wrapper that holds content and two loaders
	 */
	public ContentWrapper getContentWrapper() {
		return wrapper;
	}

	/**
	 * @return listview's scrollbar
	 */
	public ScrollBar getScrollBar() {
		return scrollBar;
	}

	/**
	 * @see #setDeceleration(float)
	 */
	public float getDeceleration() {
		return deceleration;
	}

	/**
	 * Sets the deceleration factor. The greater this value the faster fling scroll will finish.
	 * @param deceleration deceleration factor
	 */
	public void setDeceleration(float deceleration) {
		if (deceleration < 0) {
			throw new IllegalArgumentException(
					"deceleration must be >= 0. actual: " + deceleration);
		}
		this.deceleration = deceleration;
	}

	/**
	 * @see #setScrollBackDuration(float)
	 */
	public float getScrollBackDuration() {
		return scrollBackDuration;
	}

	/**
	 * Sets duration of the scroll animation that takes place when the user left listview in an overscrolled state.
	 * @param scrollBackDuration duration in seconds
	 */
	public void setScrollBackDuration(float scrollBackDuration) {
		if (scrollBackDuration < 0) {
			throw new IllegalArgumentException(
					"scrollBackDuration must be >= 0. actual: " + scrollBackDuration);
		}
		this.scrollBackDuration = scrollBackDuration;
	}

	/**
	 * Calls {@link ContentWrapper#updateContent()}.
	 */
    public void updateContent() {
        wrapper.updateContent();
    }


	/**
	 * Listener that carries about ListView scrolling.
	 */
	public static class ListViewListener extends ActorGestureListener {

		private final ListView listView;
		private final ContentWrapper items;


		public ListViewListener(ListView listView) {
			if (listView == null)
				throw new NullPointerException("listView is null");

			ContentWrapper items = listView.getContentWrapper();
			if (items == null)
				throw new NullPointerException("items are null");

			this.listView = listView;
			this.items = items;
		}


		@Override
		public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
			if (event.getPointer() != 0)
				return;
			items.scroll(deltaX, deltaY);
			listView.getScrollBar().fadeIn();
		}

		@Override
		public void fling(InputEvent event, float velocityX, float velocityY, int button) {

			if (event.getPointer() != 0)
				return;

			if (items.isOverScroll())
				return;

			float velocity = items.isHorizontal() ? velocityX : velocityY;
			if (Math.abs(velocity) < 0.01)
				return;

			float sign = Math.signum(velocity);
			float s = sign * velocity * velocity / 2 / listView.getDeceleration();
			float offset = items.getOffset() + s;

			offset = Math.max(items.getMinOffset(), Math.min(offset, items.getMaxOffset()));
			float a = velocity * velocity / 2 / (items.getOffset() - offset);
			items.scrollTo(offset, Math.abs(velocity / a));
		}

		@Override
		public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
			if (event.getPointer() != 0)
				return;
			items.stopScrolling();
		}

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			if (event.getPointer() != 0)
				return;

			if (items.getOffset() > items.getMaxOffset()) {
				items.scrollToStart(listView.getScrollBackDuration());
				if (Math.abs(items.getMaxOffset() + items.getTopOverScroll() - items.getOffset()) < 1) {
					if (items.isTopLoaderEnabled())
						listView.requestRefresh();
				}
			}
			else if (items.getOffset() < items.getMinOffset()) {
				items.scrollToEnd(listView.getScrollBackDuration());
				if (Math.abs(items.getMinOffset() - items.getBottomOverScroll() - items.getOffset()) < 1) {
					if (items.isBottomLoaderEnabled())
						listView.requestLoad();
				}
			}

			if (items.isScrolling()) {
				listView.getScrollBar().fadeOut();
			}
		}
	}
}
