package com.idp.engine.ui.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Navbar;

import java.util.Stack;

/**
 * Created by ozvairon on 17.05.17.
 */

public class ScreenManager {

    private TransitionManager transitionManager;
    private AppScreen currentScreen;
    private Stack<AppScreen> screenStack;
    private AppScreen startScreen;

    public ScreenManager() {
        transitionManager = new TransitionManager();
        screenStack = new Stack<AppScreen>();
    }

    /**
     * Adds new screen to the screen stack.
     * @param s new screen
     */
    public void pushScreen(AppScreen s) {
        screenStack.push(currentScreen);

        Navbar.NavButton back = new Navbar.NavButton("back");
        //back.setBackgroundColor(Color.CLEAR);

        Navbar navbar = s.getNavbar();

        back.setColor(App.Colors.TEXT_NAVBAR);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popScreen();
            }
        });
        navbar.getLeftIcons().addActor(back);

        currentScreen = s;

        changeScreen(currentScreen, TransitionManager.TransitionType.SLIDE_RIGHT_LEFT);
    }

    /**
     * Removes one screen from the screen stack.
     */
    public void popScreen() {
        this.currentScreen = screenStack.pop();
        changeScreen(currentScreen, TransitionManager.TransitionType.SLIDE_LEFT_RIGHT);
    }

    public void setScreen(AppScreen screen) {
        currentScreen = screen;
        App.getInstance().setScreen(screen);
    }

    /**
     * performs screen transition from current screen to another.
     *
     * @param screen screen that will be shown after transition
     * @param type transition type
     */
    private void changeScreen(AppScreen screen, TransitionManager.TransitionType type) {
        if (currentScreen == null) {
            setScreen(screen);
        } else {
            if (currentScreen == transitionManager) return;
            transitionManager.fadeScreens(type, screen, 0.4f);
        }
    }

    public void setStartScreen(AppScreen screen) {
        this.startScreen = screen;
    }

    public void start() {
        screenStack.clear();
        currentScreen = startScreen;
        setScreen(startScreen);
    }

    public AppScreen getCurrentScreen() {
        return currentScreen;
    }

}
