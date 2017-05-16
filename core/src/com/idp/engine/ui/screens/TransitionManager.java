package com.idp.engine.ui.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.ozv.starttrack.StartTrackApp;
import com.ozv.starttrack.screens.base.StartTrackBaseScreen;
import com.idp.engine.base.Idp;

/**
 * Screen that is responsible of managing screen transitions. Holy crap.
 *
 * Created by ozvairon on 29.07.16.
 */
public class TransitionManager extends IdpAppScreen {

	public static enum TransitionType {
		FADE, SLIDE_LEFT_RIGHT, SLIDE_RIGHT_LEFT, SLIDE_UP_DOWN, BLINK, SLIDE_DOWN_UP
	}

    public TransitionManager() {
        super();
        this.clearScene();
    }

	private Screen inScreen;
	private IdpAppScreen outScreen;
	private boolean nextontop;


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
	public void fadeScreens(TransitionType type, StartTrackBaseScreen<?> nextScreen, float duration) {

		inScreen = StartTrackApp.getInstance().getScreen();
		if (inScreen instanceof TransitionManager) {
			inScreen = this.outScreen;
		}
		else {
            fadeScreens(type, (StartTrackBaseScreen<?>) inScreen, nextScreen, duration);
		}
	}

	private void fadeScreens(TransitionType type, final StartTrackBaseScreen<?> current, final IdpAppScreen next,
							 final float duration) {

		this.inScreen = current;
		this.outScreen = next;

//		stage.addActor(StartTrackApp.getInstance().getFpsLabel());

//		next.getStage().getRoot().setColor(Color.WHITE);
//		current.getStage().getRoot().setColor(Color.WHITE);

		StartTrackApp.getInstance().setScreen(this);

		switch (type) {
			case SLIDE_LEFT_RIGHT:
				nextontop = false;

				//next.getStage().getRoot().debugAll();

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
				//current.getStage().getRoot().debugAll();
				current.getStage().addAction(
						Actions.sequence(
								Actions.moveTo(
										current.getStage().getWidth() * 1, 0,
										duration, Interpolation.pow2Out
								),
								Actions.run(new Runnable() {
									public void run() {
										StartTrackApp.getInstance().setScreen(outScreen);
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
									StartTrackApp.getInstance().setScreen(outScreen);
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
										StartTrackApp.getInstance().setScreen(next);
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
										StartTrackApp.getInstance().setScreen(next);
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
										StartTrackApp.getInstance().setScreen(outScreen);
									}
								}),
								Actions.color(Color.WHITE)
						)
				);
				break;

			case BLINK:
				StartTrackApp.getInstance().setScreen(outScreen);
				break;
		}
		Idp.input.setInputProcessor(next.getStage());
	}
}
