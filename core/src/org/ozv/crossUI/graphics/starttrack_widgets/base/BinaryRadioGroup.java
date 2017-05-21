/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ozv.crossUI.graphics.starttrack_widgets.base;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.layouts.HLayout;
import com.idp.engine.ui.graphics.actors.ImageActor;

import org.ozv.crossUI.StartTrackApp;
import org.ozv.crossUI.api.model.Grade;

/**
 * Widget representing a module.
 *
 * @author idp
 */
public class BinaryRadioGroup extends HLayout {
	
	private RadioButton selected;


	public BinaryRadioGroup(final Grade grade) {
		
		gap = lp;
		paddingTop = 0;
		paddingBottom = 0;
		paddingLeft  = 0;
		paddingRight = 0;
		
		addRadioButton("accept", 1, grade);
		addRadioButton("cross",  0, grade);
		
//		setJustified(true);
	}
	
	
	private void addRadioButton(String icon, final int value, final Grade grade) {
		
		final ImageActor a = new ImageActor(StartTrackApp.getResources().getIcon(icon));
		final RadioButton rb = new RadioButton(a) {
			@Override
			public void onSelected() {
				getIcon().setColor(value == 1 ? App.Colors.getColorByName("ICON_TICK") : App.Colors.getColorByName("ICON_CANCEL"));
				grade.grade = value;
			}

			@Override
			public void onUnselected() {
				getIcon().setColor(App.Colors.WIDGET_BORDER);
				grade.grade = null;
			}
		};
		rb.getIcon().setColor(App.Colors.WIDGET_BORDER);

		rb.addListener(new ActorGestureListener() {
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (rb == selected) {
					rb.unselect();
					selected = null;
					return;
				}
				
				if (selected != null)
					selected.unselect();
				rb.select();
				selected = rb;
			}
		});
		
		if (grade.grade != null && grade.grade == value) {
			rb.select();
			selected = rb;
		}

		addActor(rb);
	}
	
}
