/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.resources.assets;

/**
 * IdpAsset is managed by {@link IdpAssetManager}.
 * @param <T> type of the underlying asset
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public final class IdpAsset<T> {

	private T asset;


	/**
	 * Used by {@link IdpAssetManager} when asset starts loading.
	 * Do not use it manually.
	 */
	public IdpAsset() {

	}


	/**
	 * Checks whether asset is loaded or not.
	 *
	 * @return true if loaded, false otherwise
	 */
	public boolean isLoaded() {
		return asset != null;
	}

	/**
	 * Returns underlying asset.
	 * Throws NullPointerException if it is not loaded yet.
	 *
	 * @return underlying asset
	 */
	public T getAsset() {
		if (asset == null)
			throw new NullPointerException("getAsset(): Asset not loaded yet");
		return asset;
	}

	/**
	 * Used by {@link IdpAssetManager} when asset gets loaded.
	 * Do not use it manually.
	 *
	 * @param asset newly loaded asset
	 */
	void setAsset(T asset) {
		this.asset = asset;
	}

}
