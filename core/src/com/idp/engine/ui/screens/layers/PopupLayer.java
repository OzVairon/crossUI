package com.idp.engine.ui.screens.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ozv.starttrack.StartTrackApp;

import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.dialogs.GDXProgressDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

/**
 * Layer that holds all popup elements.
 *
 * Created by ozvairon on 02.08.16.
 */
public class PopupLayer extends Layer {

	private Actor popup;
	private Action outAction;


	public PopupLayer() {
        debug();
		setBackgroundColor(Color.valueOf("00000000"));
		setVisible(false);
		setTouchable(Touchable.disabled);
	}

    public void getConfirmationDialog(String titleString, String message, ClickListener confirm) {
        getConfirmationDialog(titleString, message, confirm, null);
    }

    public void getConfirmationDialog(String titleString, String message, ClickListener confirm, ClickListener cancel) {
        Dialogs.showConfirmDialog(titleString, message, confirm, cancel);
    }

    public GDXProgressDialog getProgressDialog(String title, String message) {
        return Dialogs.showProgressDialog(title, message);
    }

    public GDXButtonDialog getAlertDialog(String title, String message) {
        return Dialogs.showAlertDialog(title, message);
    }


	public void pop(Actor a, PopAnimation type) {
		if (a == null)
			return;

		setVisible(true);
		setTouchable(Touchable.enabled);
		popup = a;
        a.setVisible(false);
        addActor(popup);


        a.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.visible(true),
                Actions.delay(0.1f),
                Actions.alpha(1, 0.2f, Interpolation.pow2In)
        ));
        popup.setX((getWidth() - popup.getWidth()) / 2);
        popup.setY((getHeight() - popup.getHeight()) / 2);
        outAction = Actions.sequence(
                Actions.alpha(0, 0.2f, Interpolation.pow2In)
        );

	}

	public void removePop() {
		if (popup == null)
			return;

		popup.addAction(Actions.sequence(
				outAction,
				Actions.run(new Runnable() {
					@Override
					public void run() {
						popup = null;
						clearLayer();
					}
				})
		));
	}

	private void clearLayer() {
		clearChildren();
		setVisible(false);
		setTouchable(Touchable.disabled);
	}

	public enum PopAnimation {
		Blink;
	}
}

class Dialogs {

    public static void showConfirmDialog(String titleString, String message, final ClickListener confirm, final ClickListener cancel) {

        final GDXButtonDialog bDialog = StartTrackApp.getInstance().getDialogs().newDialog(GDXButtonDialog.class);
        bDialog.setTitle(titleString);
        bDialog.setMessage(message);

        bDialog.addButton("Отмена");
        bDialog.addButton("ОК");

        bDialog.setClickListener(new ButtonClickListener() {
            @Override
            public void click(int button) {
                switch (button) {
                    case 0: {
                        System.out.println("cancel");
                        if (cancel != null)
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    cancel.clicked(null, 0, 0);
                                }
                            });

                        bDialog.dismiss();
                        break;
                    }
                    case 1: {
                        System.out.println("OK");
                        if (confirm != null)
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    confirm.clicked(null, 0, 0);
                                }
                            });
                        bDialog.dismiss();
                        break;
                    }
                }
            }
        });

        bDialog.build().show();
    }

    public static GDXProgressDialog showProgressDialog(String titleString, String message) {

        GDXProgressDialog progressDialog = StartTrackApp.getInstance().getDialogs().newDialog(GDXProgressDialog.class);

        progressDialog.setTitle(titleString);
        progressDialog.setMessage(message);

        progressDialog.build().show();

        return progressDialog;
    }

    public static GDXButtonDialog showAlertDialog(String titleString, String message) {

        final GDXButtonDialog bDialog = StartTrackApp.getInstance().getDialogs().newDialog(GDXButtonDialog.class);
        bDialog.setTitle(titleString);
        bDialog.setMessage(message);

        bDialog.addButton("ОК");

        bDialog.setClickListener(new ButtonClickListener() {
            @Override
            public void click(int button) {
                switch (button) {
                    case 0: {
                        bDialog.dismiss();
                        break;
                    }
                }
            }
        });

        bDialog.build().show();
        return bDialog;
    }
}
