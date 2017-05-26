package com.idp.engine.ui.screens.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.idp.engine.App;

import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.dialogs.GDXProgressDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;

/**
 * Layer that holds all popup elements.
 *
 * Created by ozvairon on 02.08.16.
 */
public class PopupLayer extends Layer {

    public void getConfirmationDialog(String titleString, String message, ClickListener confirm, ClickListener cancel) {
        Dialogs.showConfirmDialog(titleString, message, confirm, cancel);
    }

    public void getProgressDialog(String title, String message) {
        Dialogs.showProgressDialog(title, message);
    }

    public void getAlertDialog(String title, String message) {
        Dialogs.showAlertDialog(title, message);
    }

    public void dismissProgressDialog() {
        Dialogs.dismissProgressDialog();
    }
}

class Dialogs {

    static GDXProgressDialog progress;

    public static void showConfirmDialog(String titleString, String message, final ClickListener confirm, final ClickListener cancel) {

        final GDXButtonDialog bDialog = App.getInstance().getDialogs().newDialog(GDXButtonDialog.class);
        bDialog.setTitle(titleString);
        bDialog.setMessage(message);

        bDialog.addButton("Отмена");
        bDialog.addButton("ОК");

        bDialog.setClickListener(new ButtonClickListener() {
            @Override
            public void click(int button) {
                switch (button) {
                    case 0: {
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

    public static void showProgressDialog(String titleString, String message) {

        GDXProgressDialog progressDialog = App.getInstance().getDialogs().newDialog(GDXProgressDialog.class);

        progressDialog.setTitle(titleString);
        progressDialog.setMessage(message);

        progressDialog.build().show();
        progress = progressDialog;
    }

    public static void showAlertDialog(String titleString, String message) {

        final GDXButtonDialog bDialog = App.getInstance().getDialogs().newDialog(GDXButtonDialog.class);
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
    }

    public static void dismissProgressDialog() {
        if (progress != null) {
            progress.dismiss();
        }
    }
}
