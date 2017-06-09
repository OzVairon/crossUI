package org.ozv.crossUI.screens;

import com.idp.engine.ui.graphics.actors.Button;
import com.idp.engine.ui.graphics.actors.CheckBox;
import com.idp.engine.ui.graphics.actors.FieldText;
import com.idp.engine.ui.screens.AppScreen;
import com.idp.engine.ui.screens.layers.MainLayer;

public class TestScreen extends AppScreen {

	public TestScreen() {
		super("Тестовый экран");
	}

	@Override
	public void init() {
		super.init();
        //add your code here

		getMainLayer().setContentLayout(MainLayer.LayoutType.Vertical);

		Button b = new Button();
		addActor(b);

		CheckBox c = new CheckBox();
		addActor(c);

		FieldText f = new FieldText();
		addActor(f);


//		getMainLayer().setContentLayout(MainLayer.LayoutType.Vertical);
//		getMainLayer().getContent().setPadding(App.dp2px(8));
//
//
//		Text t1 = new Text();
//		t1.debug();
//		addActor(t1);
//
//		CheckBox c = new CheckBox("Флажок");
//		c.debug();
//		addActor(c);
//
//		Button b1 = new Button();
//		b1.debug();
//		addActor(b1);
//
//		FieldText tf = new FieldText();
//		tf.debug();
//		addActor(tf);
//		tf.setPlaceholder("Введите текст");
//
//		Button b2 = new Button();
//		b2.debug();
//		addActor(b2);
//
//		Text t2 = new Text();
//		t2.debug();
//		addActor(t2);


//		ListView lw = new ListView();
//
//		lw.setSize(getMainLayer().content.getWidth(), getMainLayer().content.getHeight()/2);
//		lw.debug();
//		lw.setBackgroundColor(Color.valueOf("FF9999"));
//		lw.getContentWrapper().setHorisontal(true);
//
//		HorizontalLayout l = new HorizontalLayout();
//		l.setBackgroundColor(Color.YELLOW);
//		for (int i = 0; i < 20; i++) {
//			Text t = new Text("Text " + i);
//			l.addActor(t);
//		}
//
//		lw.addActor(l);
//		addActor(lw);

	}

	@Override
	public void show() {
		super.show();
	}
}