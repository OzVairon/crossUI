package com.idp.engine.ui.graphics.actors;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Widget;

/**
 * UI element to display text.
 *
 * Created by ozvairon on 01.08.16.
 */
public class Text extends Widget {

	private boolean busy = false;
	private Label label;

	public Text() {
		this("Простой текст");
	}

	public Text(String text) {
		this(text, App.getResources().getLabelStyle("text"));
	}

	public Text(String text, Label.LabelStyle s) {
		label = new Label(text, s);
		setSize(label.getWidth(), label.getHeight());
		addActor(label);
	}

	public void setText(CharSequence newText) {
		label.setText(newText);
		label.layout();
	}

	public String getText() {
		return String.valueOf(this.label.getText());
	}

	@Override
	protected void sizeChanged() {
		label.setSize(getWidth(), getHeight());
		label.layout();
	}

	public Label.LabelStyle getStyle() {
		return label.getStyle();
	}

	public void setStyle(Label.LabelStyle s) {
		label.setStyle(s);
	}

	public void layout() {
		if (!busy) {
			busy = true;
			label.layout();
			busy = false;
		}
	}

	public GlyphLayout getGlyphLayout() {
		return label.getGlyphLayout();
	}

	public void setAlignment(int aligment) {
		label.setAlignment(aligment);
	}

	public void setWrap(boolean wrap) {
		label.setWrap(wrap);
		label.layout();
		setHeight(label.getGlyphLayout().height);
	}

	@Override
	protected void init() {

	}
}



//package com.idp.engine.ui.graphics.actors;
//
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.idp.engine.ui.graphics.base.Widget;
//
///**
// * UI element to display text.
// *
// * Created by ozvairon on 01.08.16.
// */
//public class Text extends Widget<Label> {
//
//	private boolean busy = false;
//
//	private Label label;
//
//	public Text(Label l) {
//		super(l);
//	}
//
//	public Text(String text, Label.LabelStyle s) {
//		super(new Label(text, s));
//	}
//
//	public void setTitle(CharSequence newText) {
//		data.setTitle(newText);
//		data.layout();
//	}
//
//	@Override
//	protected void sizeChanged() {
//		data.setSize(getWidth(), getHeight());
//		data.layout();
//	}
//
//	public Label.LabelStyle getStyle() {
//		return data.getStyle();
//	}
//
//	public void setStyle(Label.LabelStyle s) {
//		data.setStyle(s);
//	}
//
//	public void layout() {
//		if (!busy) {
//			busy = true;
//			data.layout();
//			busy = false;
//		}
//	}
//
//	public GlyphLayout getGlyphLayout() {
//		return data.getGlyphLayout();
//	}
//
//	public void setAlignment(int aligment) {
//		data.setAlignment(aligment);
//	}
//
//	public void setWrap(boolean wrap) {
//		data.setWrap(wrap);
//		data.layout();
//		setHeight(data.getGlyphLayout().height);
//	}
//
//	@Override
//	protected void init() {
//		setSize(data.getWidth(), data.getHeight());
//		addActor(data);
//	}
//}
