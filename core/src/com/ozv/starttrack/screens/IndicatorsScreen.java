package com.ozv.starttrack.screens;

import com.ozv.starttrack.StartTrackApp;
import com.ozv.starttrack.graphics.starttrack_widgets.IndicatorWidget;
import com.ozv.starttrack.api.model.Indicator;
import com.ozv.starttrack.api.model.Participant;
import com.ozv.starttrack.screens.base.StartTrackBaseScreen;
import com.idp.engine.App;

/**
 * Start screen of Startrack app.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class IndicatorsScreen extends StartTrackBaseScreen<Participant> {

	public IndicatorsScreen(Participant p) {
		super(p.first_name + " " + p.last_name);
		this.data = p;
	}


	@Override
	protected void initWidgets() {
		for (final Indicator i : StartTrackApp.getState().gameModule.indicators) {
			IndicatorWidget rect = new IndicatorWidget(i);
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
		StartTrackApp.saveState();
	}

	@Override
	public void hide() {
		StartTrackApp.saveState();
	}
	
}
