package org.ozv.crossUI.screens.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.Text;
import com.idp.engine.ui.graphics.actors.layouts.VLayout;
import com.idp.engine.ui.graphics.base.Navbar;
import com.idp.engine.ui.graphics.base.Rect;
import com.idp.engine.ui.screens.NetScreen;

import org.ozv.crossUI.StartTrackApp;

/**
 * Start screen of Startrack app.
 *
 * @param <T>
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public abstract class StartTrackBaseScreen<T> extends NetScreen<T> {

	protected VLayout exitButton;
	
	
	public StartTrackBaseScreen(String title) {
		super(title);
	}

    @Override
    protected void init() {
        super.init();
        getNavbar().setColor(App.Colors.MAIN);
        getNavbar().setBorder(0);

        Rect optIcon = new Navbar.NavButton("exit");
        optIcon.setBackgroundColor(Color.CLEAR);
        optIcon.setColor(App.Colors.TEXT_NAVBAR);
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

        getPopupLayer().getConfirmationDialog("Вы точно хотите выйти?", "", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StartTrackApp.getInstance().logOut();
            }
        });
	}
	
}
