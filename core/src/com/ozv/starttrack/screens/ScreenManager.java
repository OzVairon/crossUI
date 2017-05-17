package com.ozv.starttrack.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Navbar;
import com.idp.engine.ui.graphics.base.Rect;
import com.idp.engine.ui.screens.IdpAppScreen;
import com.idp.engine.ui.screens.TransitionManager;
import com.ozv.starttrack.screens.base.StartTrackBaseScreen;

import java.util.Stack;

/**
 * Created by ozvairon on 17.05.17.
 */

public class ScreenManager {

    private TransitionManager transitionManager;
    private IdpAppScreen currentScreen;
    private Stack<IdpAppScreen> screenStack;
    private IdpAppScreen startScreen;

    public ScreenManager() {
        transitionManager = new TransitionManager();
        screenStack = new Stack<IdpAppScreen>();
    }

    /**
     * Adds new screen to the screen stack.
     * @param s new screen
     */
    public void pushScreen(IdpAppScreen s) {
        screenStack.push(currentScreen);

        currentScreen = s;
        Navbar navbar = currentScreen.getNavbar();

        Rect back = new Navbar.NavButton("backScreen");
        back.setBackgroundColor(Color.CLEAR);
        back.setColor(App.Colors.TEXT_NAVBAR);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popScreen();
            }
        });
        navbar.getLeftIcons().addActor(back);

        changeScreen(currentScreen, TransitionManager.TransitionType.SLIDE_RIGHT_LEFT);
    }

    /**
     * Removes one screen from the screen stack.
     */
    public void popScreen() {
        this.currentScreen = screenStack.pop();
        changeScreen(currentScreen, TransitionManager.TransitionType.SLIDE_LEFT_RIGHT);
    }

    public void setScreen(Screen screen) {
        currentScreen = (IdpAppScreen) screen;
        App.getInstance().setScreen(screen);
    }

    /**
     * performs screen transition from current screen to another.
     *
     * @param screen screen that will be shown after transition
     * @param type transition type
     */
    private void changeScreen(IdpAppScreen screen, TransitionManager.TransitionType type) {
        if (currentScreen == null) {
            setScreen(screen);
        } else {
            if (currentScreen == transitionManager) return;
            transitionManager.fadeScreens(type, (StartTrackBaseScreen<?>) screen, 0.4f);
        }
    }

    public void setStartScreen(IdpAppScreen screen) {
        this.startScreen = screen;
    }

    public void start() {
        screenStack.empty();
        setScreen(startScreen);
    }


}
