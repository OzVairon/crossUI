package com.ozv.crossui;

import android.os.Bundle;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(initializeForView(new CrossUIApp(), cfg));
		setContentView(layout);
	}
}
