package org.ozv.crossUI.screens;

import com.idp.engine.App;

import org.ozv.crossUI.TestApp;
import org.ozv.crossUI.api.model.Indicator;
import org.ozv.crossUI.api.model.Participant;
import org.ozv.crossUI.graphics.starttrack_widgets.IndicatorWidget;
import org.ozv.crossUI.screens.base.StartTrackBaseScreen;

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
		for (final Indicator i : TestApp.getState().gameModule.indicators) {
			IndicatorWidget rect = new IndicatorWidget(i);
			listView.getContent().addActor(rect);
		}
		listView.getContentWrapper().setSpace(App.dp2px(8));
	}
	
	@Override
	protected void loadData() {
		dataLoaded(this.data);
	}

	@Override
	public void pause() {
		TestApp.saveState();
	}

	@Override
	public void hide() {
		TestApp.saveState();
	}
	
}
