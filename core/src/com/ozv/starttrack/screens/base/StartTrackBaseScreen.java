package com.ozv.starttrack.screens.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ozv.starttrack.StartTrackApp;
import com.idp.engine.ui.graphics.base.Navbar;
import com.idp.engine.ui.graphics.base.Rect;
import com.idp.engine.ui.graphics.actors.Text;
import com.idp.engine.ui.screens.NetScreen;

/**
 * Start screen of Startrack app.
 *
 * @param <T>
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public abstract class StartTrackBaseScreen<T> extends NetScreen<T> {

	protected com.ozv.starttrack.graphics.starttrack_widgets.base.VLayout exitButton;
	
	
	public StartTrackBaseScreen(String title) {
		super(title);
	}

    @Override
    protected void init() {
        super.init();
        getNavbar().setColor(StartTrackApp.ColorPallete.MAIN);
        getNavbar().setBorder(0);

        Rect optIcon = new Navbar.NavButton("exit");
        optIcon.setBackgroundColor(Color.CLEAR);
        optIcon.setColor(StartTrackApp.ColorPallete.TEXT_NAVBAR);
        optIcon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logOut();
            }
        });
        getNavbar().getRightIcons().clearChildren();
        getNavbar().getRightIcons().addActor(optIcon);
        optIcon.toFront();

    }

    @Override
	public void show() {

		super.show();
	}

	@Override
	protected void dataFailed(Throwable t, String error) {
		errorMessage = "Ошибка, попробуйте позже";
		super.dataFailed(t, error);
        initialized = true;
	}

	@Override
	protected void initErrorWidget() {
		Text emptyList = new Text(errorMessage, StartTrackApp.getResources().getLabelStyle("text"));
		emptyList.getStyle().fontColor = Color.valueOf("666666");
		emptyList.setAlignment(Align.center);
		emptyList.setSize(Gdx.graphics.getWidth(), StartTrackApp.dp2px(20));
		listView.getContent().addActor(new Actor());  // adds gap
		listView.getContent().addActor(emptyList);
		listView.getContentWrapper().setBottomOverScroll(0);
	}

	
	protected void logOut() {

        getConfirmationDialog("Вы точно хотите выйти?", "", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StartTrackApp.getInstance().logOut();
            }
        });
	}
	
}
