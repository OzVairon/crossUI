package com.idp.engine.ui.screens;

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
        System.out.print("\nPush; previous: " + currentScreen.getName() + "; ");
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
        System.out.println("next: " + currentScreen.getName() + "; ");

        changeScreen(currentScreen, TransitionManager.TransitionType.SLIDE_RIGHT_LEFT);
    }

    /**
     * Removes one screen from the screen stack.
     */
    public void popScreen() {
        System.out.print("\nPop; previous: " + currentScreen.getName() + "; ");
        this.currentScreen = screenStack.pop();
        System.out.println(": popped" + currentScreen.getName() + "");
        changeScreen(currentScreen, TransitionManager.TransitionType.SLIDE_LEFT_RIGHT);
    }

    public void setScreen(IdpAppScreen screen) {
        System.out.println("SetScreen: " + screen.getName());
        currentScreen = screen;
        App.getInstance().setScreen(screen);
    }

    /**
     * performs screen transition from current screen to another.
     *
     * @param screen screen that will be shown after transition
     * @param type transition type
     */
    private void changeScreen(IdpAppScreen screen, TransitionManager.TransitionType type) {
        printScreenStackTrace();
        if (currentScreen == null) {
            setScreen(screen);
        } else {
            if (currentScreen == transitionManager) return;
            transitionManager.fadeScreens(type, screen, 0.4f);
        }
    }

    public void setStartScreen(IdpAppScreen screen) {
        this.startScreen = screen;
    }

    public void start() {
        screenStack.clear();
        currentScreen = startScreen;
        setScreen(startScreen);
    }

    public IdpAppScreen getCurrentScreen() {
        return currentScreen;
    }

    public void printScreenStackTrace() {
        System.out.println("SCREEN STACK");
        for (IdpAppScreen sc : screenStack) {
            System.out.println(sc.getName());
        }
    }
}
