package com.idp.engine.ui.graphics.actors.layouts;

import com.idp.engine.App;
import com.idp.engine.ui.graphics.base.Widget;


/**
 * Created by ozvairon on 27.05.17.
 */

public abstract class Layout extends Widget{

    protected final int sp = App.dp2px(8);   // small gap
    protected final int mp = App.dp2px(12);  // medium gap
    protected final int lp = App.dp2px(16);  // large gap

    protected boolean fixWidth = false;
    protected boolean fixHeight = false;

    public int paddingLeft   = mp;
    public int paddingRight  = mp;
    public int paddingTop    = mp;
    public int paddingBottom = mp;



    @Override
    protected void init() {

    }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
        layout();
    }

    public void setPadding(int p) {
        paddingBottom = p;
        paddingLeft = p;
        paddingRight = p;
        paddingTop = p;
        layout();
    }

    public void setPadding(int top, int right, int bottom, int left) {
        paddingBottom = bottom;
        paddingLeft = left;
        paddingRight = right;
        paddingTop = top;
        layout();
    }

    public abstract void layout();

    public void setFixHeight(boolean fix) {
        this.fixHeight = fix;
    }
    public void setFixWidth(boolean fix) {
        this.fixWidth = fix;
    }



}
