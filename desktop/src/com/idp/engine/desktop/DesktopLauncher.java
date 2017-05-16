package com.idp.engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.ozv.starttrack.StartTrackApp;
import com.idp.engine.base.IdpFiles;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;


public class DesktopLauncher {

	public static void main(String[] args) {

		pack(new String[] {
			"icons"
		});

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new StartTrackApp(), config);
	}

	private static void pack(String[] dirNames) {
		try {

			boolean forcePack = false;
//			forcePack = true;

			String srcPath = new File("../assets_raw").getCanonicalPath();
			String dstPath = new File("").getCanonicalPath();

			for (String dirName : dirNames) {

				File srcDir = new File(srcPath + "/" + dirName);
				File dstAtlas = new File(dstPath + "/" + dirName + ".atlas");

				boolean repack = forcePack || !dstAtlas.exists() || updateConfig(srcDir);

				if (repack) {
					System.out.println("Packing '" + srcPath + "/" + dirName + "' to '" + dstPath + "'");
					Files.copy(
							new File(srcPath + "/pack.json").toPath(),
							new File(srcPath + "/" + dirName + "/pack.json").toPath(),
							StandardCopyOption.REPLACE_EXISTING
					);
					TexturePacker.process(srcPath + "/" + dirName, dstPath, dirName);
					Files.delete(new File(srcPath + "/" + dirName + "/pack.json").toPath());
				}
			}
			System.out.println("Packing finished");
		}
		catch (IOException ex) {
			System.out.println(ex);
		}
	}

	private static long getDirLastModified(File dir) throws IOException {
		long moded = 0;
		for (File file : dir.listFiles())
			moded = Math.max(moded, getFileLastModified(file));
		return moded;
	}

	private static long getFileLastModified(File file) throws IOException {
		if (file.isDirectory())
			return getDirLastModified(file);
		FileTime time = (FileTime)Files.getAttribute(file.getAbsoluteFile().toPath(), "basic:creationTime");
		long moded = Math.max(time.toMillis(), file.lastModified());
		return moded;
	}

	private static ModifiedConfig crtConfig(File dir) throws IOException {
		ModifiedConfig config = new ModifiedConfig();
		for (File file : dir.listFiles()) {
			if (file.getName().equals("modified.json"))
				continue;
			config.items.add(new Modified(file.getName(), getFileLastModified(file)));
		}
		return config;
	}

	private static boolean updateConfig(File dir) throws IOException {
		ModifiedConfig existing = null;
		try {
			existing = new IdpFiles().readFileJson(
					new FileHandle(dir).child("modified.json"), ModifiedConfig.class);
		}
		catch (Exception ex) {

		}
		ModifiedConfig newConfig = crtConfig(dir);
		if (!newConfig.equals(existing)) {
			new IdpFiles().writeFileJson(
					new FileHandle(dir).child("modified.json"), newConfig);
			return true;
		}
		return false;
	}

	private static class ModifiedConfig {
		public ArrayList<Modified> items = new ArrayList<Modified>();

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (!(obj instanceof ModifiedConfig))
				return false;
			ModifiedConfig other = (ModifiedConfig) obj;

			if (other.items.size() != items.size())
				return false;
			for (int i = 0; i < items.size(); i++) {
				Modified m1 = items.get(i);
				Modified m2 = other.items.get(i);
				if (!(m1.name.equals(m2.name) && m1.time == m2.time))
					return false;
			}
			return true;
		}
	}

	private static class Modified {
		public String name;
		public long time;
		public Modified(String name, long time) {
			this.name = name;
			this.time = time;
		}
	}
}
