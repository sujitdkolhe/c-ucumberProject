//Copyright 2013-2021 NXGN Management, LLC. All Rights Reserved.
package com.atom.healthcare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.atom.healthcare.util.BrowserTypeUtil.BrowserType;
import com.atom.healthcare.util.EnvironmentTypeUtil.EnvironmentType;
import com.atom.healthcare.util.OSTypeUtil.OSType;

public class TestConfig {

	private static final String DEFAULT_PROP_FILENAME = "testConfig.properties";
	public static Properties properties = new Properties();
	private static String testRoot, testResultScreenshots, testConfigFile ;
	static {

		try {
			testRoot = System.getProperty("testRoot");
			if (testRoot == null)
				testRoot = System.getProperty("user.dir"); // setting default testRoot

			FileInputStream in = null;
			if (testConfigFile == null) {
				in = new FileInputStream(testRoot + File.separator + DEFAULT_PROP_FILENAME);
			} else {
				in = new FileInputStream(testRoot + File.separator + testConfigFile);
			}
			properties.load(in);
			in.close();

		} catch (IOException e) {
			System.err.println("Failed to read: " + testConfigFile);
		}
	}

	/**
	 * Parameter in testConfig.properties or System env
	 */
	public static BrowserType getBrowserType() {
		String browser = System.getProperty("selenium.browser");
		if (browser != null)
			return BrowserTypeUtil.getBrowserType(browser);
		else
			return BrowserTypeUtil.getBrowserType(String.valueOf(properties.getProperty("selenium.browser")).trim());
	}

	/**
	 * Optional parameter in testConfig.properties default value is false
	 */
	public static boolean isClearBrowserCache() {
		String clearBrowserCache = String.valueOf(properties.getProperty("clear.browser.cache")).trim();
		return clearBrowserCache.equalsIgnoreCase("true") ? true : false;
	}

	/**
	 * Optional parameter in testConfig.properties or System env
	 */
	public static boolean isCaptureScreenshot() {
		String env_captureScreenshot = System.getProperty("capture.screenshot");
		String prop_captureScreenshot = String.valueOf(properties.getProperty("capture.screenshot"));
		if (env_captureScreenshot != null)
			return (env_captureScreenshot.trim().equalsIgnoreCase("true")) ? true : false;
		else if (prop_captureScreenshot != null)
			return (prop_captureScreenshot.trim().equalsIgnoreCase("true")) ? true : false;
		else
			return false;
	}

	public static String getTestResultScreenshot() {
		return testResultScreenshots;
	}

	public static String dumpTestConfigInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("<pre>" + "\n");
		sb.append("testRoot: " + testRoot + "\n");
		sb.append("testConfigFile: " + DEFAULT_PROP_FILENAME + "\n");
		sb.append("client os.type: " + getTestClientOSType() + "\n");
		sb.append("clear.browser.cache: " + isClearBrowserCache() + "\n");
		sb.append("capture.screenshot: " + isCaptureScreenshot() + "\n");
		sb.append("isProxyEnabled: " + isProxyEnabled() + "\n");
		sb.append("logger.level: " + getLoggerLevel() + "\n");
		sb.append("</pre>");
		return sb.toString();
	}
	
	public static OSType getTestClientOSType() {
		String osType = System.getProperty("os.name");
		return OSTypeUtil.getTestClientOSType(osType);
	}

	public static boolean isProxyEnabled() {
		String proxy = String.valueOf(properties.getProperty("proxy.enable")).trim();
		return proxy.equalsIgnoreCase("true") ? true : false;
	}

	public static String getLoggerLevel() {
		String env_level = System.getProperty("logger.level");
		String prop_level = String.valueOf(properties.getProperty("logger.level"));
		if (env_level != null)
			return env_level.trim();
		else if (prop_level != null)
			return prop_level.trim();
		else
			return null;
	}

	public static String getUserDefinedProperty(String propertyName) {
		String env_propertyName = System.getProperty(propertyName);
		if (env_propertyName == null) {
			String prop_propertyName = String.valueOf(properties.getProperty(propertyName));
			if (prop_propertyName == null)
				return null;
			else
				return prop_propertyName;
		} else {
			return env_propertyName;
		}
	}

	public static String getRetryAttempts() {
		String env_retry = System.getProperty("retry.attempts");
		String prop_retry = String.valueOf(properties.getProperty("retry.attempts"));
		if (env_retry != null)
			return env_retry.trim();
		else if (!prop_retry.equals("null"))
			return prop_retry.trim();
		else
			return null;
	}

	 /**
     * Desc:- Method will get the value of "test.environment" from testConfig.properties
     */
    public static EnvironmentType getEnvironmentType() {
        String env = TestConfig.getUserDefinedProperty("test.environment");
        if (env != null) {
            return EnvironmentTypeUtil.getEnvironmentType(env);
        } else {
            return EnvironmentTypeUtil
                    .getEnvironmentType(String.valueOf(properties.getProperty("test.environment")).trim());
        }
    }
   
    public static void printCookies(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println("Printing Cookies -------");
        for (Cookie c : cookies) {
            System.out.println(c.toString());
        }
        System.out.println("--------------------------");
    }
    
    /**
	 * Print name of calling method. NOTE: This only works if you call the method
	 * directly from a another method with no other method in between. The index of
	 * [2] represents the position of the calling method in the trace In this case,
	 * that would be the method that directly calls this method.
	 */
	public static void PrintMethodName() {
		System.out.println("###>>> --- METHOD: " + Thread.currentThread().getStackTrace()[2].getClassName() + "."
				+ Thread.currentThread().getStackTrace()[2].getMethodName());
	}
}