package com.idp.engine.resources;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.XmlReader;
import com.idp.engine.App;
import com.idp.engine.base.Idp;
import com.idp.engine.resources.assets.IdpColorPixmap;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

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
		fonts.put(key, new FontAndColor(man.loadFont("fonts/" + path, App.dp2px(size)), color));
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

    public void loadFonts() {

        LinkedList<FontInfo> list = new LinkedList<FontInfo>();
        XmlReader xr = new XmlReader();
        try {
            XmlReader.Element elist = xr.parse(Idp.files.internal("appconfig"));

            for(XmlReader.Element e : elist.getChildByName("fonts").getChildrenByName("font")){
                list.add(new FontInfo(
                        e.getAttribute("name"),
                        e.getAttribute("path"),
                        e.getAttribute("size"),
                        e.getAttribute("color")
                ));
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for (FontInfo f : list) {
            f.load();
        }


    }

    class FontInfo {
        String name;
        String path;
        String size;
        String color;

        public FontInfo(String name, String path, String size, String color) {
            this.name = name;
            this.path = path;
            this.size = size;
            this.color = color;
        }

        public void load() {
            loadFont(this.name, this.path, Integer.parseInt(size), Color.valueOf(color));
        }

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
