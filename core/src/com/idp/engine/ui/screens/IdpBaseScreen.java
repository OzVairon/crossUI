package com.idp.engine.ui.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.idp.engine.base.Idp;

/**
 * Base class for screens in IdpGame and App.
 * Contains and manages its {@link Stage}.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class IdpBaseScreen extends ScreenAdapter {

	protected Stage stage;
	protected InputProcessor backKeyProcessor;
	protected boolean ydown = true;

    private String name = "IDPScreen";


	/**
	 * Creates screen with y-down stage.
	 * @see #IdpEmptyScreen(boolean)
	 */
	public IdpBaseScreen() {
		this(true);
	}

	/**
	 * @param ydown whether y axis should point down or not
	 */
	public IdpBaseScreen(boolean ydown) {
		this.stage = createStage(ydown);
	}


	@Override
	public void show() {
		Idp.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
	}

	/**
	 * @return screen stage
	 */
	public Stage getStage() {
		return stage;
	}

    protected Stage createStage(boolean ydown) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(ydown, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ScalingViewport view = new ScalingViewport(
                Scaling.fit,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                camera
        );

        Stage stage = new Stage(view);
        stage.getBatch().setProjectionMatrix(camera.combined);
        return stage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
