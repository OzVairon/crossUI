package org.ozv.crossUI.graphics.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.idp.engine.ui.graphics.actors.Text;
import com.idp.engine.ui.graphics.base.Rect;

import org.ozv.crossUI.StartTrackApp;

import java.util.ArrayList;

/**
 * Container that holds labels and layouts them to equal width.
 *
 * Created by ozvairon on 28.08.16.
 */
public class StatsLabels extends Group {

	private final int sp = StartTrackApp.dp2px(8);  // small gap
	private final int mp = StartTrackApp.dp2px(12); // medium gap
	private final int lp = StartTrackApp.dp2px(16); // large gap

	private final ArrayList<StatsLabel> labels;
	private final Rect separator;


	public StatsLabels() {
		labels = new ArrayList<StatsLabel>();

		setWidth(Gdx.graphics.getWidth() - 2 * lp);
		separator = new Rect();

		separator.setWidth(this.getWidth());
		separator.setHeight(StartTrackApp.dp2px(1));
		separator.setBackgroundColor(Color.valueOf("dadada"));

		setHeight(StartTrackApp.dp2px(48));
		addActor(separator);
	}


	public void addLabel(String number, String text) {
		StatsLabel l = new StatsLabel(number, text);
		labels.add(l);
		addActor(l);
		layout();
	}

	private void layout() {
		float x = 0;
		float w = getWidth() / labels.size();
		for (StatsLabel l : labels) {
			l.setWidth(w);
			l.setX(x);
			x += w;
		}
	}

	private class StatsLabel extends Group {

		private final Text number;
		private final Text text;

		private StatsLabel(String n, String s) {
			number = new Text(n, StartTrackApp.getResources().getLabelStyle("stats_counter"));
			text = new Text(s, StartTrackApp.getResources().getLabelStyle("tab"));
            text.getStyle().fontColor = Color.valueOf("666666");
            text.setStyle(text.getStyle());

			number.setAlignment(Align.center);
			text.setAlignment(Align.center);

			addActor(number);
			addActor(text);

			this.layout();
		}

		@Override
		protected void sizeChanged() {
			text.setWidth(getWidth());
			number.setWidth(getWidth());
			this.layout();
		}

		public void layout() {
			number.setX((getWidth() - number.getWidth()) / 2);
			text.setX((getWidth() - text.getWidth()) / 2);

			number.setY(sp);
			text.setY(sp + number.getHeight());
		}
	}
}
