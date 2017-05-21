/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idp.engine.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import java.io.*;

/**
 * Additional helpers for file manipulations.
 *
 * @author dhabensky <dhabensky@idp-crew.com>
 */
public final class IdpFiles {

	public boolean LOG_EXCEPTIONS = false;
	public final Gson gson = new Gson();


	/**
	 * Writes given object to a local file.
	 * @param path path to the local file
	 * @param obj object to write
	 */
	public void writeLocal(String path, Object obj) {
		writeFile(local(path), obj);
	}

	/**
	 * Writes given string to a local file.
	 * @param path path to the local file
	 * @param s string to write
	 */
	public void writeLocalString(String path, String s) {
		writeFileString(local(path), s);
	}

	/**
	 * Converts given object to json and writes json string to a local file.
	 * @param path path to the local file
	 * @param object object to write
	 */
	public void writeLocalJson(String path, Object object) {
		writeFileJson(local(path), object);
	}

	/**
	 * Reads object from a local file.
	 * @param path path to the file
	 * @return object that was read from the file
	 */
	public Object readLocal(String path) {
		return readFile(local(path));
	}

	/**
	 * Reads string from a local file.
	 * @param path path to the file
	 * @return string that was read from the file
	 */
	public String readLocalString(String path) {
		return readFileString(local(path));
	}

	/**
	 * Reads json string from a local file and converts it to an object of the requested class.
	 * @param path path to the file
	 * @param clazz requested class of the object
	 * @return object of class T that that was read from the file
	 */
	public <T> T readLocalJson(String path, Class<T> clazz) {
		return readFileJson(local(path), clazz);
	}

	/**
	 * Reads object from an internal file.
	 * @param path path to the file
	 * @return object that was read from the file
	 */
	public Object readInternal(String path) {
		return readFile(internal(path));
	}

	/**
	 * Reads string from an internal file.
	 * @param path path to the file
	 * @return string that was read from the file
	 */
	public String readInternalString(String path) {
		return readFileString(internal(path));
	}

	/**
	 * Reads json string from an internal file and converts it to an object of the requested class.
	 * @param path path to the file
	 * @param clazz requested class of the object
	 * @return object of class T that that was read from the file
	 */
	public <T> T readInternalJson(String path, Class<T> clazz) {
		return readFileJson(internal(path), clazz);
	}

	/**
	 * @return internal {@link FileHandle} corresponding to path {@code path}.
	 */
	public FileHandle internal(String path) {

		if (Gdx.app == null) { // desktop, without Gdx initialization
			if (path.startsWith("../android/assets/"))
				return new FileHandle(path);
			return new FileHandle("../android/assets/" + path);
		}
		else { // with Gdx, platform does not matter
			return Gdx.files.internal(path);
		}
	}

	/**
	 * @return local {@link FileHandle} corresponding to path {@code path}.
	 */
	public FileHandle local(String path) {

		if (Gdx.app == null) { // desktop, without Gdx initialization
			if (path.startsWith("../android/assets/"))
				return new FileHandle(path);
			return new FileHandle("../android/assets/" + path);
		}
		else { // with Gdx, platform does not matter
			return Gdx.files.local(path);
		}
	}

	/**
	 * Writes given object to a file.
	 * @param fileHandle file to write to
	 * @param object object to write
	 */
	public void writeFile(FileHandle fileHandle, Object object) {

		OutputStream fileStream = null;
		ObjectOutputStream objStream = null;

		try {
			fileStream = fileHandle.write(false);
			objStream = new ObjectOutputStream(fileStream);
			objStream.writeObject(object);
		}
		catch (Exception ex) {
			if (LOG_EXCEPTIONS)
				AppUtils.logger.log(ex);
			throw new RuntimeException("cannot save to file: " + fileHandle.path(), ex);
		}
		finally {
			try {
				if (objStream != null)
					objStream.close();
				if (fileStream != null)
					fileStream.close();
			}
			catch (IOException ex) {
				if (LOG_EXCEPTIONS)
					AppUtils.logger.log(ex);
			}
		}
	}

	/**
	 * Writes given string to a file.
	 * @param fileHandle file to write to
	 * @param s string to write
	 */
	public void writeFileString(FileHandle fileHandle, String s) {
		try {
			fileHandle.writeString(s, false);
		}
		catch (Exception ex) {
			if (LOG_EXCEPTIONS)
				AppUtils.logger.log(ex);
			throw new RuntimeException("cannot save to file: " + fileHandle.path(), ex);
		}
	}

	/**
	 * Converts given object to json and writes json string to a file.
	 * @param fileHandle file to write to
	 * @param object object to write
	 */
	public void writeFileJson(FileHandle fileHandle, Object object) {
		writeFileString(fileHandle, gson.toJson(object));
	}

	/**
	 * Reads an object from a file.
	 * @param fileHandle file to read from
	 * @return object that was read from the file
	 */
	public Object readFile(FileHandle fileHandle) {

		InputStream fileStream = null;
		ObjectInputStream objStream = null;
		Object obj = null;

		try {
			fileStream = fileHandle.read();
			objStream = new ObjectInputStream(fileStream);
			obj = objStream.readObject();
		}
		catch (Exception ex) {
			if (LOG_EXCEPTIONS)
				AppUtils.logger.log(ex);
			throw new RuntimeException("cannot load from file: " + fileHandle.path(), ex);
		}
		finally {
			try {
				if (objStream != null)
					objStream.close();
				if (fileStream != null)
					fileStream.close();
			}
			catch (IOException ex) {
				if (LOG_EXCEPTIONS)
					AppUtils.logger.log(ex);
			}
		}

		return obj;
	}

	/**
	 * Reads a string from a file.
	 * @param fileHandle file to read from
	 * @return string that was read from the file
	 */
	public String readFileString(FileHandle fileHandle) {
		try {
			return fileHandle.readString();
		}
		catch (Exception ex) {
			if (LOG_EXCEPTIONS)
				AppUtils.logger.log(ex);
			throw new RuntimeException("cannot load from file: " + fileHandle.path(), ex);
		}
	}

	/**
	 * Reads json string from an internal file and converts it to an object of the requested class.
	 * @param fileHandle file to read from
	 * @param clazz requested class of the object
	 * @return object of class T that that was read from the file
	 */
	public <T> T readFileJson(FileHandle fileHandle, Class<T> clazz) {
		return gson.fromJson(readFileString(fileHandle), clazz);
	}
}
