package org.ozv.crossUI.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.actors.IdpTextField;
import com.idp.engine.ui.graphics.actors.ImageActor;
import com.idp.engine.ui.graphics.actors.Text;
import com.idp.engine.ui.graphics.base.Loader;
import com.idp.engine.ui.screens.AppScreen;

import org.ozv.crossUI.StartTrackApp;
import org.ozv.crossUI.api.StartTrackApi;
import org.ozv.crossUI.graphics.starttrack_widgets.base.VLayout;

/**
 * Screen with sign in form.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class LoginScreen extends AppScreen {

	private VLayout layout;
	private TextField email;
    private Text signIn;
    private Loader loader;
    private Text message;

	public LoginScreen() {
		super();

	}


	@Override
	protected void init() {
		super.init();



		getNavbar().setVisible(false);
		getMainLayer().content.setY(0);
		getMainLayer().content.setHeight(Gdx.graphics.getHeight());
		getMainLayer().setBackgroundColor(App.Colors.WIDGET_WHITE);

		layout = new VLayout();
		layout.setAlign(VLayout.Align.Center);
		layout.setWidth(getMainLayer().getWidth());
		layout.setPadding(0);
		layout.paddingTop = App.dp2px(48);

		layout.setGap(App.dp2px(18));
		addWidget(layout);

		float textFieldXpadding = StartTrackApp.dp2px(24);
		float underlineExtention = StartTrackApp.dp2px(12);
		float textFieldWidth = Gdx.graphics.getWidth() - textFieldXpadding * 2;

		float signInWidth = StartTrackApp.dp2px(160);
		float signInHeight = StartTrackApp.dp2px(36);
        float messageHeight = StartTrackApp.dp2px(20);

		TextFieldStyle textFieldStyle = StartTrackApp.getResources().getTextFieldStyle("text_field");
		LabelStyle titleStyle = StartTrackApp.getResources().getLabelStyle("logo");
		LabelStyle signInStyle = StartTrackApp.getResources().getLabelStyle("navbar");
		signInStyle.fontColor = App.Colors.MAIN;

		ImageActor logo = new ImageActor(App.getResources().getIcon("logo-mobile"));
		logo.setHeight(App.dp2px(72));
		logo.setWidth(
				logo.getSprite().getRegionWidth() * logo.getHeight() / logo.getSprite().getRegionHeight()
		);

		logo.setColor(App.Colors.MAIN);
		layout.addActor(logo);


		Text title = new Text("StartTrack experts", titleStyle);
		title.setAlignment(Align.center);
		title.setSize(Gdx.graphics.getWidth(), App.dp2px(30));
		layout.addActor(title);


		message = new Text("", StartTrackApp.getResources().getLabelStyle("label"));
		message.setAlignment(Align.center);
		message.setSize(getMainLayer().getWidth(), messageHeight);
		layout.addActor(message);

		this.email = new TextField("", textFieldStyle);
		email.setMessageText("Код доступа");
		IdpTextField emailWrapper = new IdpTextField(email);
		emailWrapper.setSize(textFieldWidth, textFieldStyle.font.getCapHeight() * 2);
		emailWrapper.setUnderlineLeft(underlineExtention);
		emailWrapper.setUnderlineRight(underlineExtention);
        emailWrapper.setUnderlineColor(App.Colors.MAIN);
		layout.addActor(emailWrapper);



		signIn = new Text("ВОЙТИ", signInStyle);
		signIn.setAlignment(Align.center);
		signIn.setSize(signInWidth, signInHeight);
		layout.addActor(signIn);

        loader = new Loader(StartTrackApp.getResources().getIcon("loader"));
        loader.setWidth(signInWidth);
        loader.setHeight(signInHeight);
        loader.setPosition(signIn.getX(), signIn.getY());
        loader.setVisible(false);
        addWidget(loader);

		getMainLayer().addCaptureListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				stage.setKeyboardFocus(null);
				return false;
			}
		});

		signIn.addListener(new ActorGestureListener() {
			public void tap(InputEvent event, float x, float y, int count, int button) {

                loader.start();
				showLoader();
                message.setText("");
                Gdx.input.setOnscreenKeyboardVisible(false);

				StartTrackApi.auth(email.getText().toUpperCase(), new StartTrackApi.AuthListener() {
                    public void loaded(String privateToken) {
						try {
							StartTrackApp.getInstance().logIn(privateToken);
						} catch (Exception ex) {
							failed(ex);
						}
					}

                    @Override
                    public void failed(Throwable t) {
                        if (t.getMessage().equals("{\"code\":[\"This field may not be blank.\"]}")) {
                            message.setText("Введите код");
                        } else
						if (t.getMessage().equals("{\"detail\":\"Invalid token.\"}")) {
							message.setText("Ошибка авторизации");
						} else
                        if (t.getMessage().equals("{\"non_field_errors\":[\"Unable to log in with provided credentials.\"]}")) {
                            message.setText("Неверный код доступа");
                        } else
                        if (t.getMessage().equals("{\"non_field_errors\":[\"User account is disabled.\"]}")) {
                            message.setText("Доступ экспертов запрещен");
                        } else
						if (t.getMessage().equals("{\"detail\":\"User inactive or deleted.\"}")) {
							message.setText("Доступ экспертов запрещен");
						} else {
                            message.setText("Нет подключения к интернету");
                        }

                        loader.stop();
						showSignInButton();
                        stage.setKeyboardFocus(email);
                    }
                });
			}
		});
	}

	private void showSignInButton() {
		layout.removeActor(loader);
		layout.addActor(signIn);
	}

	private void showLoader() {
		layout.removeActor(signIn);
		layout.addActor(loader);
	}

	@Override
	public void show() {
		super.show();
		StartTrackApp.getInstance().logIn();
		Gdx.input.setCatchBackKey(false);
	}


    class LoginError {
        String[] non_field_errors;
    }

}
