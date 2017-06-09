package org.ozv.crossUI.screens.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.idp.engine.ui.graphics.actors.Text;
import com.idp.engine.ui.graphics.actors.layouts.VerticalLayout;
import com.idp.engine.ui.graphics.base.Navbar;
import com.idp.engine.ui.graphics.base.Rect;
import com.idp.engine.ui.screens.NetScreen;

import org.ozv.crossUI.TestApp;

/**
 * Start screen of Startrack app.
 *
 * @param <T>
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public abstract class StartTrackBaseScreen<T> extends NetScreen<T> {

	protected VerticalLayout exitButton;
	
	
	public StartTrackBaseScreen(String title) {
		super(title);
	}

    @Override
    public void init() {
        super.init();
        Rect optIcon = new Navbar.NavButton("exit");
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
	protected void dataFailed(Throwable t, String error) {
		errorMessage = "Ошибка, попробуйте позже";
		super.dataFailed(t, error);
        initialized = true;
	}

	@Override
	protected void initErrorWidget() {
		Text emptyList = new Text(errorMessage, TestApp.getResources().getLabelStyle("text"));
		emptyList.getStyle().fontColor = Color.valueOf("666666");
		emptyList.setAlignment(Align.center);
		emptyList.setSize(Gdx.graphics.getWidth(), TestApp.dp2px(20));
		listView.getContent().addActor(new Actor());  // adds gap
		listView.getContent().addActor(emptyList);
		listView.getContentWrapper().setBottomOverScroll(0);
	}

	
	protected void logOut() {

        getPopupLayer().showConfirmationDialog("Вы точно хотите выйти?", "", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TestApp.getInstance().logOut();
            }
        });
	}
	
}
