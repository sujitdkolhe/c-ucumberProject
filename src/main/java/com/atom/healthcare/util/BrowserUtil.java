package com.atom.healthcare.util;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;


import io.github.bonigarcia.wdm.WebDriverManager;

import com.atom.healthcare.Pojo.Constants;
import com.atom.healthcare.util.BrowserTypeUtil.BrowserType;


public class BrowserUtil {
			public static void setupBrowser(BrowserType bname) {
				switch (bname) {
				case chrome:
					WebDriverManager.chromedriver().setup();
					Constants.driver = new ChromeDriver();
					Constants.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
					break;
				case firefox:
					WebDriverManager.firefoxdriver().setup();
					Constants.driver = new FirefoxDriver();
					break;
				case iexplore:
					WebDriverManager.iedriver().setup();
					Constants.driver = new InternetExplorerDriver();
					break;
				case opera:
					WebDriverManager.operadriver().setup();
					Constants.driver = new OperaDriver();
					break;
				default:
					System.err.println("Invalid browser name : " + bname);
				}

			}

			public static void certHndl(WebDriver driver, String sURL) throws Exception {

				if (TestConfig.getBrowserType() == BrowserType.iexplore) {
					Constants.driver.get(sURL);
					if (Constants.driver.getTitle().contains("Certificate Error")) {
						Constants.driver.navigate().to("javascript:document.getElementById('overridelink').click()");
					}
				} else if (TestConfig.getBrowserType() == BrowserType.opera) {
					try {
						Constants.driver.get(sURL);
					} catch (Exception e) {
						Constants.driver.switchTo().activeElement().sendKeys(Keys.TAB);
						Thread.sleep(2000);
						Constants.driver.switchTo().activeElement().sendKeys(Keys.ENTER);
						Thread.sleep(10000);
					}
				} else {
					Constants.driver.get(sURL);
				}
			}

	public static void clearBrowserCache(WebDriver driver) throws Exception {
		if (TestConfig.getBrowserType() == BrowserType.iexplore) {
			clearBrowsingDataIE();
		} else if (TestConfig.getBrowserType() == BrowserType.firefox) {
			clearBrowsingDataFireFox();
		} else if (TestConfig.getBrowserType() == BrowserType.safari) {
			clearBrowsingDataSafari();
		} else if (TestConfig.getBrowserType() == BrowserType.chrome) {
			clearBrowsingDataChrome();
		} else if (TestConfig.getBrowserType() == BrowserType.opera) {
			clearBrowsingDataOpera();
		}
		Constants.driver.manage().deleteAllCookies();
	}

	private static void clearBrowsingDataIE() throws Exception {
		Runtime rt = Runtime.getRuntime();

		// Clear temporary Internet files
		Process proc8 = rt.exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 8");
		proc8.waitFor();
		// Clear Cookies
		Process proc2 = rt.exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 2");
		proc2.waitFor();
		// Clear History
		Process proc1 = rt.exec("RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 1");
		proc1.waitFor();
	}
	private static void clearBrowsingDataSafari() throws Exception {
		Map<String, String> map = System.getenv();
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.equals("windows") || osName.equals("windows 7")) {
			String tempFilePath1 = map.get("LOCALAPPDATA") + "\\Apple Computer\\Safari\\";
			String tempFilePath2 = map.get("HOMEPATH")
					+ "\\AppData\\LocalLow\\Apple Computer\\Safari\\";
			String tempFilePath3 = map.get("APPDATA")
					+ "\\AppData\\Roaming\\Apple Computer\\Safari\\";

			File tempFilesDir1 = new File(tempFilePath1);
			FileUtil.deleteDirectory(tempFilesDir1);

			File tempFilesDir2 = new File(tempFilePath2);
			FileUtil.deleteDirectory(tempFilesDir2);
			/*
			 * if( tempFilesDir2.exists() ) { File[] files2 =
			 * tempFilesDir2.listFiles(); for(int i=0; i<files2.length; i++) {
			 * if(files2[i].isDirectory()) { deleteDirectory(files2[i]); } else
			 * { files2[i].delete(); } } }
			 */
			File tempFilesDir3 = new File(tempFilePath3);
			FileUtil.deleteDirectory(tempFilesDir3);

		} else if (osName.equals("windows xp")) {
			String tempFilePath = map.get("HOMEPATH")
					+ "\\Local Settings\\Apple Computer\\Safari\\";
			File tempFilesDir = new File(tempFilePath);
			FileUtil.deleteDirectory(tempFilesDir);
		}
	}

	private static void clearBrowsingDataChrome() throws Exception {
		Map<String, String> map = System.getenv();
		String tempFilePath = "";
		String osName = System.getProperty("os.name").toLowerCase();

		if (osName.equals("windows") || osName.equals("windows 11")) { // Windows 7 set the path to the temp files for Win 7
																		 
			tempFilePath = map.get("LOCALAPPDATA") + "\\Chrome\\";  				 
		} else if (osName == "windows xp") {
			tempFilePath = map.get("HOMEPATH")
					+ "\\Local Settings\\Application Data\\Google\\Chrome\\"; // set the path to the temp files for WinXP		
		}
		
		File tempFilesDir = new File(tempFilePath);
		FileUtil.deleteDirectory(tempFilesDir);
		/*
		 * File[] files = tempFilesDir.listFiles(); for (File file : files) { if
		 * (!file.delete()) // Delete each file {
		 * System.out.println("Failed to delete "+file); // Failed to delete
		 * file } }
		 */
	}

	private static void clearBrowsingDataFireFox() throws Exception {
		String tempFilePath = "";
		Map<String, String> map = System.getenv();
		String osName = System.getProperty("os.name").toLowerCase();

		if (osName.equals("windows") || osName.equals("windows 7")) {
			tempFilePath = map.get("LOCALAPPDATA") + "\\Mozilla\\Firefox\\Profiles\\";
		} else if (osName.equals("windows xp")) {
			tempFilePath = map.get("HOMEPATH")
					+ "\\Local Settings\\Application Data\\Mozilla\\Firefox\\Profiles\\";
		}
		File tempFilesDir = new File(tempFilePath);
		FileUtil.deleteDirectory(tempFilesDir);
	}
	private static void clearBrowsingDataOpera() throws Exception {
		Map<String, String> map = System.getenv();
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.equals("windows") || osName.equals("windows 7")) {
			String tempFilePath1 = map.get("LOCALAPPDATA") + "\\Opera\\";
			String tempFilePath2 = map.get("HOMEPATH") + "\\AppData\\LocalLow\\Opera\\";
			String tempFilePath3 = map.get("APPDATA") + "\\Opera\\";

			File tempFilesDir1 = new File(tempFilePath1);
			FileUtil.deleteDirectory(tempFilesDir1);
			/*
			 * if( tempFilesDir1.exists() ) { File[] files1 =
			 * tempFilesDir1.listFiles(); for(int i=0; i<files1.length; i++) {
			 * if(files1[i].isDirectory()) { deleteDirectory(files1[i]); } else
			 * { files1[i].delete(); } } }
			 */
			File tempFilesDir2 = new File(tempFilePath2);
			FileUtil.deleteDirectory(tempFilesDir2);

			File tempFilesDir3 = new File(tempFilePath3);
			FileUtil.deleteDirectory(tempFilesDir3);

		} else if (osName.equals("windows xp")) {
			String tempFilePath = map.get("HOMEPATH") + "\\Local Settings\\Opera\\";
			File tempFilesDir = new File(tempFilePath);
			FileUtil.deleteDirectory(tempFilesDir);

		}
	}
}
