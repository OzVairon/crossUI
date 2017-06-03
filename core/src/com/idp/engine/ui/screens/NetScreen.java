/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.idp.engine.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Loader;
import com.idp.engine.ui.graphics.actors.Text;
import com.idp.engine.ui.graphics.actors.listview.ListView;
import com.idp.engine.net.Request;

/**
 * Base screen for all screens that need to load some data to display.
 *
 * @param <T> type of data that the screen loads and displays
 *
 *
 */
public abstract class NetScreen<T> extends AppScreen {
    
    
    public abstract class NetworkListView extends ListView {

        protected Request requestManager;

        public NetworkListView() {

        }

        public NetworkListView(Actor topLoader, Actor bottomLoader) {
            super(topLoader, bottomLoader);
        }


        public boolean isLoading() {
            return requestManager != null && requestManager.getState() == Request.State.LOADING;
        }
    }
    
    

	protected NetworkListView listView;
	protected boolean initialized;
	protected Loader loader;
	protected T data;
	protected String errorMessage;


	public NetScreen() {
		this("");
	}

	public NetScreen(String name) {
		super(name);
		this.errorMessage = "Ошибка, попробуйте позже";
	}


	@Override
	public void show() {
		super.show();
		if (!initialized) {
			if (listView != null)
				listView.getContent().clearChildren();
			else
				showLoader();
			loadData();
		}
	}

	/**
	 * Here should be started http request to awaitLoad data.
	 * As data is loaded, listener should parse response and call {@link NetScreen#dataLoaded}.
	 */
	protected abstract void loadData();

	/**
	 * Called when data loaded successfully.
	 * @param data what was loaded
	 */
    protected void dataLoaded(T data) {
		this.data = data;
		hideLoader();
		initListView();
		initWidgets();
		listView.updateContent();
		this.initialized = true;
	}

	/**
	 * ListView should be initialized here.
	 */
	protected void initListView() {

		if (listView == null) {

//			Loader topLoader = new Loader(App.getResources().getIcon("loader"));
//			topLoader.setSize(Gdx.graphics.getWidth(), app.dp2px(48));

			this.listView = new NetworkListView(null, null) {
                @Override
				public void requestRefresh() {
					if (isLoading())
						return;

					initialized = false;
					loadData();
					getContentWrapper().setTopOverScrollFixed(true);
					getContentWrapper().scrollToStart(getScrollBackDuration());
				}
			};
			listView.setWidth(Gdx.graphics.getWidth());
			listView.setHeight(getMainLayer().content.getHeight() - listView.getY());
			listView.getContentWrapper().setOverScroll(App.dp2px(48));
			listView.getContentWrapper().setSpace(App.dp2px(16));
			addActor(listView);
		}
		else {
			listView.getContent().clearChildren();
			listView.getContentWrapper().setTopOverScrollFixed(false);
		}
		
		listView.getContentWrapper().setSpace(0);
		listView.getContentWrapper().setOverScroll(App.dp2px(16));
	}

	/**
	 * Initializes widgets and adds them to listView.
	 * Called after {@link NetScreen#initListView()}.
	 */
	protected abstract void initWidgets();

	/**
	 * HttpResponseListener that listens for HttpRequest started in {@link NetScreen#loadData()}
	 * should call this method if request was failed or if loaded data has unparsable format.
	 * @param t cause
	 * @param error human readable description of error
	 */
    protected void dataFailed(Throwable t, String error) {
		hideLoader();
		initListView();
		initErrorWidget();
		listView.updateContent();
	}

	/**
	 * Called from {@link NetScreen#dataFailed()} to display error message.
	 */
	protected void initErrorWidget() {
		Text emptyList = new Text(errorMessage, App.getResources().getLabelStyle("h2"));
		emptyList.getStyle().fontColor = Color.valueOf("666666");
		emptyList.setAlignment(Align.center);
		emptyList.setSize(Gdx.graphics.getWidth(), App.dp2px(20));
		listView.getContent().addActor(new Actor());  // adds gap
		listView.getContent().addActor(emptyList);
		listView.getContentWrapper().setBottomOverScroll(0);
	}

	/**
	 * Initializes loader that is shown while data is downloading.
	 */
	protected void initLoader() {

		this.loader = new Loader(App.getResources().getIcon("loader"));
		loader.setPosition(Gdx.graphics.getWidth() / 2 - loader.getWidth() / 2,
				App.dp2px(24) + loader.getHeight() / 2
		);
	}

	/**
	 * Adds loaded to the stage.
	 */
	protected void showLoader() {
		if (loader == null)
			initLoader();
		loader.start();

		addActor(loader);
	}

	/**
	 * Removes loader from the stage.
	 */
	protected void hideLoader() {
		loader.stop();
		loader.remove();
	}
}
