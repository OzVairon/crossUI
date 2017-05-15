package com.idp.engine.resources;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.centergame.starttrack.StartTrackApp;
import com.idp.engine.App;
import com.idp.engine.resources.assets.IdpColorPixmap;

import java.util.HashMap;

/**
 * Provides access for applications resources.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public class Resources {
    
    protected static class FontAndColor {
        public com.idp.engine.resources.assets.IdpAsset<BitmapFont> font;
        public Color color;
        public FontAndColor(com.idp.engine.resources.assets.IdpAsset<BitmapFont> font, Color color) {
            this.font = font;
            this.color = color;
        }
    }
    
	protected final com.idp.engine.resources.assets.IdpAssetManager man;
    protected final HashMap<String, FontAndColor> fonts;
    protected com.idp.engine.resources.assets.IdpAsset<TextureAtlas> icons;


	/**
	 * Empty resources. Nothing is loaded in constructor.
	 */
	public Resources() {
		this.man = com.idp.engine.resources.assets.IdpAssetManager.getInstance();
        this.fonts = new HashMap<String, FontAndColor>();
	}
    
    /**
     * Queues BitmapFont for loading.
     * @param key to be used to access the resource once it is loaded
     * @param path where the font is located
     * @param size font size in px
     * @param color font color
     */
    public void loadFont(String key, String path, int size, Color color) {
		fonts.put(key, new FontAndColor(man.loadFont(path, App.dp2px(size)), color));
    }
    
    /**
     * Queues atlas of icons for loading.
     * Only single atlas might be loaded.
     * @param iconAtlasPath path to the icon atlas
     */
    public void loadIcons(String iconAtlasPath) {
        if (this.icons != null)
            throw new IllegalStateException("Another atlas is already loaded.");
		this.icons = man.loadAtlas(iconAtlasPath);
    }

	/**
	 * Waits until all queued resources get loaded.
	 */
	public void awaitLoad() {
		man.finishLoading();
        onLoaded();
	}
    
    /**
     * Called once all queued resources get loaded.
     */
    public void onLoaded() {
        fixFonts();
    }

	/**
	 * Returns {@link LabelStyle} associated with given name.
	 * @param styleName the name
	 * @return style for name styleName
	 */
    public LabelStyle getLabelStyle(String styleName) {
        LabelStyle style = new LabelStyle();
        try {
            FontAndColor fac = fonts.get(styleName);
            style.font = fac.font.getAsset();
            style.fontColor = fac.color;
        } catch(NullPointerException e) {
            System.out.println("cannot find: " + styleName);
        }
        return style;
    }

	/**
	 * Returns icon associated with the given name.
     * Requires icon atlas to be loaded.
	 * @param name icon name
	 * @return icon for the given name
	 */
    public TextureRegion getIcon(String name) {
		return icons.getAsset().findRegion(name);
    }


    /**
     * Returns {@link TextFieldStyle} associated with given name.
     *
     * @param styleName the name
     * @return style for name styleName
     */
    public TextField.TextFieldStyle getTextFieldStyle(String styleName) {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        Resources.FontAndColor fac = fonts.get(styleName);

        style.font = fac.font.getAsset();
        style.fontColor = fac.color;
        style.messageFontColor = Color.valueOf("666666");
        style.cursor = new IdpColorPixmap(Color.valueOf("1e78e6")).buildDrawable();
        style.selection = new IdpColorPixmap(Color.valueOf("a8d1ff")).buildDrawable();
        return style;
    }

    public void enqueueAll() {

        loadFont("navbar",              "fonts/SF-UI-Display-Medium.ttf",  18, StartTrackApp.ColorPallete.TEXT_NAVBAR);
        loadFont("label",               "fonts/SF-UI-Display-Regular.ttf", 14, Color.valueOf("666666"));
        loadFont("debug_info",          "fonts/SF-UI-Display-Regular.ttf", 14, Color.valueOf("ffffff"));
        loadFont("h1",                  "fonts/SF-UI-Display-Regular.ttf", 16, Color.BLACK);
        loadFont("h1-bold",             "fonts/SF-UI-Display-Bold.ttf",    16, Color.BLACK);
        loadFont("number",              "fonts/SF-UI-Display-Bold.ttf",    18, StartTrackApp.ColorPallete.TEXT_NUMBER);
        loadFont("text",                "fonts/SF-UI-Text-Regular.ttf",    14, Color.valueOf("666666"));
        loadFont("text_field",          "fonts/SF-UI-Display-Light.ttf",   16, Color.BLACK);
        loadFont("logo",                "fonts/SF-UI-Display-Bold.ttf",   25, Color.valueOf("006fc0"));
        loadFont("header",              "fonts/SF-UI-Text-Bold.ttf",       12, Color.valueOf("666666"));
        loadFont("participant-number",  "fonts/SF-UI-Text-Bold.ttf",       20, Color.valueOf("666666"));
        loadFont("popup-title",         "fonts/SF-UI-Text-Bold.ttf",       14, Color.valueOf("000000"));
        loadFont("popup-buttons",       "fonts/SF-UI-Text-Bold.ttf",       16, Color.valueOf("000000"));
        loadFont("popup-text",       "fonts/SF-UI-Text-Regular.ttf",    14, Color.valueOf("666666"));

        loadIcons("icons.atlas");
    }

    private void fixFonts() {

        // font that would be used in TextField needs some fixes...
        if (Gdx.app.getType() != Application.ApplicationType.iOS) {
            BitmapFont font = fonts.get("text_field").font.getAsset();
            font.getData().ascent = font.getCapHeight() / 3;    // coeff got by trial and error
        }

        // fixing line height for font that is used for multiline text
        fonts.get("text").font.getAsset().getData().setLineHeight(App.dp2px(18));
    }

}
