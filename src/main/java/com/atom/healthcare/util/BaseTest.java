package com.atom.healthcare.util;
import org.apache.log4j.LogManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.atom.healthcare.Pojo.Constants;

import org.apache.log4j.Level;
import io.cucumber.java.Scenario;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BaseTest {
	private int stepCounter;
	public static WebDriver driver;

	public void setUp(Scenario method) throws Exception {
		LogManager.getLogger("com.atom.healthcare.Utils").setLevel(Log4jUtil.getLogLevel());
		if (TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.chrome) {
			BrowserUtil.setupBrowser(BrowserTypeUtil.BrowserType.chrome);
		}else if (TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.iexplore) {
			BrowserUtil.setupBrowser(BrowserTypeUtil.BrowserType.iexplore);
		}else if (TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.firefox) {
		BrowserUtil.setupBrowser(BrowserTypeUtil.BrowserType.firefox);
		}else if(TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.opera) {
			BrowserUtil.setupBrowser(BrowserTypeUtil.BrowserType.opera);
		}else if(TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.htmlunit) {
			BrowserUtil.setupBrowser(BrowserTypeUtil.BrowserType.htmlunit);
		}else if(TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.safari) {
			BrowserUtil.setupBrowser(BrowserTypeUtil.BrowserType.safari);
		}else {
			log("Browser Type null");
		}
		Constants.driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
		if (TestConfig.isClearBrowserCache()) {
			BrowserUtil.clearBrowserCache(Constants.driver);
		}

		logTestEnvironmentInfo(method);

		log("Resetting step counter");
		stepCounter = 0;
	}

	public void beforeSuite() {
		log(TestConfig.dumpTestConfigInfo(), Level.INFO);
	}

	public void testSetup(Scenario method) throws Exception {
		if (TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.iexplore) {
			BrowserUtil.setupBrowser(BrowserTypeUtil.BrowserType.iexplore);
		}else {
			if (TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.firefox)
				BrowserUtil.setupBrowser(BrowserTypeUtil.BrowserType.firefox);
		}

		Constants.driver.manage().timeouts().implicitlyWait(20L, TimeUnit.SECONDS);
		if (TestConfig.isClearBrowserCache()) {
			BrowserUtil.clearBrowserCache(Constants.driver);
		}

		logTestEnvironmentInfo(method);

		log("Resetting step counter");
		stepCounter = 0;
	}

	public void tearDown(Scenario scenario) throws Exception {
		if (scenario.isFailed()) {
			String screenshotName = scenario.getName().replaceAll("", "_");
			byte[] sourcePath = ((TakesScreenshot) Constants.driver).getScreenshotAs(OutputType.BYTES);
			scenario.attach(sourcePath, "image/png", screenshotName);
		}

	}

	public void postTestCase(Scenario scenario) throws ClassNotFoundException {
		String testMethodName = scenario.getName();

		if (scenario.isFailed()) {
			try {
				if (TestConfig.isCaptureScreenshot()) {
					String filename = TestConfig.getTestResultScreenshot() + File.separator + "FAILED_" + "."
							+ testMethodName + ".html";
					BufferedWriter out = new BufferedWriter(new FileWriter(filename));
					out.write(Constants.driver.getPageSource());
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			Constants.driver.quit();

			if (TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.iexplore) {
				// Killing the ie Appointment.driver and all instances of internet explorer
				String processName = "IEAppointment.driverServer.exe";
				if (WindowProcessKiller.isProcessRuning(processName)) {
					WindowProcessKiller.killProcess(processName);
				}
				String ie_process = "iexplore.exe";
				if (WindowProcessKiller.isProcessRuning(ie_process)) {
					WindowProcessKiller.killProcess(ie_process);
				}
			}

		} catch (Exception e) {
			log("ERROR: Unable to close browser(s), no browser is currently open.");
		}

		log("TEST RESULT:");
		System.out.println(new Date() + " environment=" + TestConfig.getEnvironmentType() + " test=" + testMethodName
				+ " result=" + scenario.getStatus());
	}

	protected void logStep(String logText) {
		log("STEP " + ++stepCounter + ": " + logText);
	}

	protected void log(String message) {
		Log4jUtil.log(message);
	}

	protected void log(String message, Level level) {
		Log4jUtil.log(message, level);
	}

	private void logTestEnvironmentInfo(Scenario method) {
		logTestEnvironmentInfo(method.getName(), TestConfig.getEnvironmentType(), TestConfig.getBrowserType());
	}

	private void logTestEnvironmentInfo(String method, EnvironmentTypeUtil.EnvironmentType environment,
			BrowserTypeUtil.BrowserType browser) {
		log("Test case: " + method);
		log("Execution Environment: " + environment);
		log("Execution Browser: " + browser);
	}

	public void maxWindow() {
		if (!(TestConfig.getBrowserType() == BrowserTypeUtil.BrowserType.iexplore)) {
			JavascriptExecutor js = (JavascriptExecutor) Constants.driver;
			String script = "if (window.screen){window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);};";
			js.executeScript(script);
		}
	}

}

