/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.ui.graphics.base;

/**
 * Base Widget.
 *
 * @param <T>
 * @author idp
 */
public abstract class Widget<T> extends Rect {

	protected T data;

	public Widget(T data) {
		//setBackgroundColor(Color.CLEAR);
		this.data = data;
		init();
	}
	
	public T getData() {
		return data;
	}
	
	protected abstract void init();

	
}
