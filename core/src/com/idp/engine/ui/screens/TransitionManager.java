package com.idp.engine.ui.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.idp.engine.App;
import com.idp.engine.base.AppUtils;

/**
 * Screen that is responsible of managing screen transitions.
 *
 * Created by ozvairon on 29.07.16.
 */
public class TransitionManager extends AppScreen {

	public static enum TransitionType {
		FADE, SLIDE_LEFT_RIGHT, SLIDE_RIGHT_LEFT, SLIDE_UP_DOWN, BLINK, SLIDE_DOWN_UP
	}

    public TransitionManager() {
        super();
        this.clearScene();
		this.setName("Transition");
    }

	private AppScreen inScreen;
	private AppScreen outScreen;
	private boolean nextontop;

	@Override
	protected void init() {
		stage.getRoot().setTouchable(Touchable.disabled);
	}

	@Override
	public void render(float delta) {
		if (nextontop) {
			inScreen.render(delta);
			outScreen.render(delta);
		}
		else {
			outScreen.render(delta);
			inScreen.render(delta);
		}
		stage.draw();
	}

	/**
	 * Performs animated screen transition.
	 * @param type type of the transition
	 * @param nextScreen screen to show after the transition is ended
	 * @param duration transition duration
	 */
	public void fadeScreens(TransitionType type, AppScreen nextScreen, float duration) {

		inScreen = (AppScreen) App.getInstance().getScreen();


		if (inScreen instanceof TransitionManager) {
			//inScreen = this.outScreen;
			return;
		}
		else {
            fadeScreens(type, inScreen, nextScreen, duration);
		}
	}

	private void fadeScreens(TransitionType type, final AppScreen current, final AppScreen next,
							 final float duration) {

		this.inScreen = current;
		this.outScreen = next;

		App.getInstance().setScreen(this);

		switch (type) {
			case SLIDE_LEFT_RIGHT:
				nextontop = false;

				next.getStage().getRoot().setPosition(-next.getStage().getWidth() / 4, 0);
				next.getStage().addAction(
                        Actions.parallel(
                                Actions.moveTo(0, 0, duration, Interpolation.pow2Out),
                                Actions.run(new Runnable() {
                                    public void run() {
                                        next.fadeIn(duration);
                                    }
                                })


                        )
                );

				current.getStage().addAction(
						Actions.sequence(
								Actions.moveTo(
										current.getStage().getWidth() * 1, 0,
										duration, Interpolation.pow2Out
								),
								Actions.run(new Runnable() {
									public void run() {
										App.showScreen(outScreen);
									}
								})
						)
				);
				break;

			case SLIDE_RIGHT_LEFT:
				nextontop = true;
				next.getStage().getRoot().setPosition(next.getStage().getWidth(), 0);
				next.getStage().addAction(Actions.moveTo(0, 0, duration, Interpolation.pow2Out));
				current.getStage().addAction(
						Actions.sequence(
								Actions.parallel(
										Actions.moveTo(
												-current.getStage().getWidth() / 8, 0,
												duration, Interpolation.pow2Out
										),
										Actions.run(new Runnable() {
											public void run() {
												current.fadeOut(duration);
											}
										}
								)
							),
							Actions.run(new Runnable() {
								public void run() {
									App.getInstance().setScreen(outScreen);
								}
							})
						)
				);
				break;

			case SLIDE_UP_DOWN:
				next.getStage().getRoot().setPosition(0, -next.getStage().getHeight());
				next.getStage().addAction(Actions.moveTo(0, 0, duration, Interpolation.exp10Out));
				current.getStage().addAction(
						Actions.sequence(
								Actions.moveTo(
										0, current.getStage().getHeight(),
										duration, Interpolation.exp10Out
								),
								Actions.run(new Runnable() {
									public void run() {
										App.getInstance().setScreen(next);
									}
								})
						)
				);
				break;

			case SLIDE_DOWN_UP:
				next.getStage().getRoot().setPosition(0, next.getStage().getHeight());
				next.getStage().addAction(Actions.moveTo(0, 0, duration, Interpolation.exp10Out));
				current.getStage().addAction(
						Actions.sequence(
								Actions.moveTo(
										0, -current.getStage().getHeight(),
										duration, Interpolation.exp10Out
								),
								Actions.run(new Runnable() {
									public void run() {
										App.getInstance().setScreen(next);
									}
								})
						)
				);
				break;

			case FADE:
				next.getStage().addAction(Actions.sequence(Actions.fadeIn(duration)));
				current.getStage().addAction(
						Actions.sequence(
								Actions.fadeOut(duration),
								Actions.run(new Runnable() {
									public void run() {
										App.getInstance().setScreen(outScreen);
									}
								}),
								Actions.color(Color.WHITE)
						)
				);
				break;

			case BLINK:
				App.getInstance().setScreen(outScreen);
				break;
		}

		AppUtils.input.setInputProcessor(next.getStage());
	}
}
