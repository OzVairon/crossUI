package com.ozv.crossui.screens;

import com.ozv.crossui.api.model.Participant;
import com.idp.engine.App;

/**
 * Start screen of Startrack app.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class IndicatorsScreen extends com.ozv.crossui.screens.base.StartTrackBaseScreen<Participant> {

	public IndicatorsScreen(com.ozv.crossui.api.model.Participant p) {
		super(p.first_name + " " + p.last_name);
		this.data = p;
	}


	@Override
	protected void initWidgets() {
		for (final com.ozv.crossui.api.model.Indicator i : com.ozv.crossui.StartTrackApp.getState().gameModule.indicators) {
			com.ozv.crossui.graphics.starttrack_widgets.IndicatorWidget rect = new com.ozv.crossui.graphics.starttrack_widgets.IndicatorWidget(i);
			listView.getContent().addActor(rect);
		}
		listView.getContentWrapper().setSpace(App.dp2px(8));
	}


	@Override
	public void show() {
		getNavbar().setColor(App.Colors.MAIN);
		super.show();
	}
	
	@Override
	protected void loadData() {
		dataLoaded(this.data);
	}

	@Override
	public void pause() {
		com.ozv.crossui.StartTrackApp.saveState();
	}

	@Override
	public void hide() {
		com.ozv.crossui.StartTrackApp.saveState();
	}
	
}
