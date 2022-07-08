package com.atom.healthcare.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Level;

import com.atom.healthcare.util.OSTypeUtil.OSType;



public class WindowProcessKiller {

	private static final String TASKLIST = "tasklist";
	private static final String KILL = "taskkill /F /T /IM ";

	public static boolean isProcessRuning(String serviceName) {
		if (TestConfig.getTestClientOSType() == OSType.windows) {
			Process p;
			try {
				p = Runtime.getRuntime().exec(TASKLIST);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				String line;

				while ((line = reader.readLine()) != null) {
					if (line.contains(serviceName)) {
						Log4jUtil.log(serviceName + " is running", Level.DEBUG);
						return true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log4jUtil.log(serviceName + " is not running", Level.DEBUG);
			return false;
		} else
		{
			Log4jUtil.log("Client OS is not window, cannot check running process: " + serviceName , Level.DEBUG);
			return false;
		}
	}

	public static void killProcess(String serviceName) {
		Log4jUtil.log("Trying to kill " + serviceName, Level.DEBUG);
		if (TestConfig.getTestClientOSType() == OSType.windows) {
			try {
				Runtime.getRuntime().exec(KILL + serviceName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			Log4jUtil.log("Client OS is not window, can not kill " + serviceName, Level.DEBUG);
	}
}
