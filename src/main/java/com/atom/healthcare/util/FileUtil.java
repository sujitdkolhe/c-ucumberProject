package com.atom.healthcare.util;


import java.io.File;

public class FileUtil {

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			// include a test for root directory
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static boolean createDirectory(String path, boolean recreate) {
		return createDirectory(new File(path), recreate);
	}

	public static boolean createDirectory(File path, boolean recreate) {
		if (path.exists() && !recreate)
		{	
			// do nothing
			return true;
		}
		else if (path.exists() && recreate)
		{
			//delete path, and recreate
			deleteDirectory(path);
			path.mkdirs();
			path.setExecutable(true);
			return true;
		}	
		else
		{
			//path not exists, so create the path
			path.mkdirs();
			path.setExecutable(true);
			return true;
		}
	}
}
