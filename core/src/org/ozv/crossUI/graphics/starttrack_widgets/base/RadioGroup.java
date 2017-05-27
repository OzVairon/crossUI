/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ozv.crossUI.graphics.starttrack_widgets.base;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.idp.engine.ui.graphics.actors.ImageActor;
import com.idp.engine.ui.graphics.actors.layouts.HLayout;

import org.ozv.crossUI.StartTrackApp;
import org.ozv.crossUI.api.model.Report;

/**
 * StartTrackWidget representing a module.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class RadioGroup extends HLayout {
	
	private RadioButton selected;


	public RadioGroup(int min, int max, final Report report) {
		
		paddingTop = 0;
		paddingBottom = 0;
		paddingLeft  = 0;
		paddingRight = 0;
		
		for (int i = min; i <= max; i++) {
			final int ii = i;
			final RadioButton rb = new RadioButton(i + "") {
				@Override
				public void onSelected() {
					report.team_grade = ii;
				}

				@Override
				public void onUnselected() {
					report.team_grade = null;
				}
			};
			
			rb.addListener(new ActorGestureListener() {
				@Override
				public void tap(InputEvent event, float x, float y, int count, int button) {
					if (rb == selected)
						return;
					if (selected != null)
						selected.unselect();
					rb.select();
					selected = rb;
				}
			});
			
			if (report.team_grade != null && i == report.team_grade) {
				rb.select();
				selected = rb;
			}
			addActor(rb);
		}
		
		final RadioButton rb = new RadioButton(new ImageActor(
				StartTrackApp.getResources().getIcon("cancel")));
		rb.addListener(new ActorGestureListener() {
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (selected != null)
					selected.unselect();
				selected = null;
				report.team_grade = null;
			}

		});
		addActor(rb);
		
		setJustified(true);
	}
	
}
