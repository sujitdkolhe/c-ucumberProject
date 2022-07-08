package com.atom.healthcare.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.atom.healthcare.util.BrowserTypeUtil.BrowserType;

public class BasePageObject {
	protected WebDriver driver;
	public WebDriverWait wait;
	public JavascriptExecutor jse;
	protected WebElement submitFormElement;

	public BasePageObject(WebDriver driver) {
		this.driver = driver;
		jse = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, 20);
	}

	protected void waitForPageTitle(final String title, long sec) {
		WebDriverWait wait = new WebDriverWait(driver, sec);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().contains(title);
			}
		});
	}

	protected void log(String message) {
		Log4jUtil.log(message);
	}

	protected void log(String message, String level) {
		Log4jUtil.log(message, level);
	}

	protected void log(String message, Level level) {
		Log4jUtil.log(message, level);
	}

	protected void get(String url) {
		try {
			BrowserUtil.certHndl(driver, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		maxWindow();
	}

	protected void maxWindow() {
		if (!(TestConfig.getBrowserType() == BrowserType.iexplore)) {
			String script = "if (window.screen){window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);};";
			jse.executeScript(script);
		}
	}

	protected boolean switchToWindowUsingTitle(String title) throws InterruptedException {
		int retry = 0;
		String currentWindow = driver.getWindowHandle();
		while (retry < 3) {
			Set<String> availableWindows = driver.getWindowHandles();
			if (!availableWindows.isEmpty()) {
				for (String windowId : availableWindows) {
					driver.switchTo().window(windowId).getTitle();
					log("Current Window Title:  " + driver.getTitle(), Level.DEBUG);
					if (driver.getTitle().contains(title)) {
						log("Switch to " + driver.getTitle(), Level.INFO);
						return true;
					}
				}
			}
			retry++;
			Thread.sleep(5000);
		}
		log("Cannot find Window: " + title, Level.ERROR);
		driver.switchTo().window(currentWindow);
		return false;
	}

	protected void selectFromDropDown(String Xpath, String containsText) {
		Select select = new Select(driver.findElement(By.xpath(Xpath)));
		List<WebElement> allOptions = select.getOptions();
		for (WebElement option : allOptions) {
			if (option.getText().contains(containsText)) {
				select.selectByVisibleText(option.getText());
			}
		}
	}

	protected void selectFromDropDown(WebElement webElement, String containsText) {
		Select select = new Select(webElement);
		List<WebElement> allOptions = select.getOptions();
		for (WebElement option : allOptions) {
			if (option.getText().contains(containsText)) {
				select.selectByVisibleText(option.getText());
			}
		}
	}

	protected void scrollAndWait(int x, int y, int ms) throws InterruptedException {
		jse.executeScript("scroll(" + x + ", " + y + ");");
		// ((JavascriptExecutor) Constants.driver).executeScript("scroll(" + x + ", " +
		// y + ");");
		Thread.sleep(ms);
	}

	protected WebElement getFrameWebElement(String frameName) {
		driver.switchTo().defaultContent();
		List<WebElement> framesList = driver.findElements(By.xpath("//frame"));
		WebElement element = null;
		String fname = null;
		for (WebElement frame : framesList) {
			fname = frame.getAttribute("name");
			log("Found frame: " + fname, Level.DEBUG);
			if (fname.equals(frameName)) {
				element = frame;
				log("Found frame: " + fname, Level.INFO);
			}
		}

		if (element == null) {
			log("Cannot find frame: " + fname, Level.ERROR);
		}

		return element;
	}

	protected void clearTextFieldById(String id) {
		jse.executeScript("document.getElementById('" + id + "').value='';");
	}

	protected void clearTextField(WebElement e) {
		if (e != null) {
			e.clear();
			// using javascript to clear
			String id = e.getAttribute("id");
			clearTextFieldById(id);
		}
	}

	/**
	 * Checks to see if an element exists or not and returns the element or null.
	 * 
	 * @param parentElement The parent element to search off of or null to search
	 *                      off of the driver.
	 * @param by            How the element should be searched for.
	 * @param timeout       How long to wait IN SECONDS before we assume that it's
	 *                      not there.
	 * @return The element or null if it doesn't exist.
	 */
	protected WebElement getElement(final WebElement parentElement, final By by, int timeout) {
		return getElement(parentElement, by, (long) (1000 * timeout));
	}

	/**
	 * Checks to see if an element exists or not and returns the element or null.
	 * 
	 * @param parentElement The parent element to search off of or null to search
	 *                      off of the driver.
	 * @param by            How the element should be searched for.
	 * @param timeout       How long to wait IN MILLISECONDS before we assume that
	 *                      it's not there.
	 * @return The element or null if it doesn't exist.
	 */
	protected WebElement getElement(final WebElement parentElement, final By by, long timeout) {
		WebElement element = null;
		try {
			driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.MILLISECONDS);
			if (parentElement != null) {
				return parentElement.findElement(by);
			} else {
				return driver.findElement(by);
			}
		} catch (Exception e) {
			// Sometimes this is called to ensure an element is NOT
			// present -- not necessarily an error if not found.
			log("Unable to find element by " + by, Level.INFO);
		} finally {
			// reset the timeout to the default
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		}

		return element;
	}

	public void javascriptClick(WebElement element) {
		JavascriptExecutor ex = (JavascriptExecutor) driver;
		ex.executeScript("arguments[0].click();", element);
	}

	public void focusSelectAndSelectByValue(WebElement element, String value) {
		Actions build = new Actions(driver);
		build.moveToElement(element).build().perform();
		new Select(element).selectByVisibleText(value);
	}

	/**
	 * Will return true if Element exist else false
	 * 
	 * @param element :- WebElement
	 * @return boolean
	 */
	public boolean exists(WebElement element) {
		try {
			Point p = element.getLocation();
			log("Where on the page is the top left-hand corner of the rendered element" + p);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Desc:-Will first wait implicitly then will check if element exist or not .
	 * Will return true if Element exist else false
	 * 
	 * @param driver
	 * @param maxTimeInSecondsToWait
	 * @param element                Element that is checked
	 * @return
	 */

	public static boolean exists(WebDriver driver, long maxTimeInSecondsToWait, WebElement element) {
		boolean bexists = false;
		try {
			driver.manage().timeouts().implicitlyWait(maxTimeInSecondsToWait, TimeUnit.SECONDS);
			Point p = element.getLocation();
			System.out.println("Where on the page is the top left-hand corner of the rendered element" + p);
			bexists = true;
		} catch (Exception e) {
			System.out.println("Element was not found.");
		}

		finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		return bexists;
	}

	/**
	 * Description : This method converts the string to list provided the delimiter
	 * 
	 * @param inputString String
	 * @param dlimiter    String
	 * @return List
	 * @throws Exception
	 */
	public List<String> convertStringToList(String inputString, String dlimiter) throws Exception {
		List<String> NewList = new ArrayList<String>();

		String[] pieces = inputString.split(dlimiter);
		// for (int i = pieces.length - 1; i >= 0; i--) {
		// pieces[i] = pieces[i].trim();
		// NewList.add(pieces[i]);
		// }
		for (int i = 0; i <= pieces.length - 1; i++) {
			pieces[i] = pieces[i].trim();
			NewList.add(pieces[i]);
		}
		return NewList;
	}

	/**
	 * Description : Method to get the Current Date in yyyy-MMM-dd format
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentDate() throws Exception {
		DateFormat dfDate = new SimpleDateFormat("yyyy-MMMM-dd");
		Date today = Calendar.getInstance().getTime();
		String currentDate = dfDate.format(today);
		return currentDate;
	}

	/**
	 * Description : Method to get the Current Date in required Format, user has to
	 * provide the date format i.e "MM/dd/yyyy", dd/MM/yyyy etc
	 * 
	 * @param typeOfDateFormat [String]
	 * @return
	 * @throws Exception
	 */
	public static String getFormattedCurrentDate(String typeOfDateFormat) throws Exception {
		Date today = Calendar.getInstance().getTime();
		DateFormat dfDate = new SimpleDateFormat(typeOfDateFormat);
		String formattedCurrentDateTime = dfDate.format(today);
		int Len = formattedCurrentDateTime.length();
		System.out.println("***Before Modification ***" + formattedCurrentDateTime);
		if (typeOfDateFormat.equals("dd")) {
			if (formattedCurrentDateTime.charAt(0) == '0') {
				formattedCurrentDateTime = formattedCurrentDateTime.substring(1);
			}
		}

		else if (typeOfDateFormat.equals("MMMMM dd, yyyy hh:mm")) {
			// This Modification is required for Event Logger as it displays time like
			// 9:51:23 instead 09:51:23, this
			// removes 0 in front of 9
			if (Len > 10 && formattedCurrentDateTime.charAt(Len - 5) == '0') {
				String str1 = formattedCurrentDateTime.substring(0, (Len - 5));
				String str2 = formattedCurrentDateTime.substring((Len - 4), Len);
				formattedCurrentDateTime = str1.concat(str2);
				System.out.println("***After Modification ***" + formattedCurrentDateTime);
			}
		}

		else if (typeOfDateFormat.equals("MM/dd/yyyy hh:mm")) {
			String eventDate = formattedCurrentDateTime.substring(0, (Len - 6));
			String eventTime = formattedCurrentDateTime.substring((Len - 5), Len);
			if (eventTime.charAt(0) == '0') {
				eventTime = eventTime.substring(1);
			}
			// String eventPeriod = formattedCurrentDateTime.substring(17);
			formattedCurrentDateTime = eventDate + " " + "@" + " " + eventTime;
			System.out.println("***After Modification ***" + formattedCurrentDateTime);
		}

		return formattedCurrentDateTime;
	}

	public static String extractDateFromText(String text) throws IllegalStateException {
		// tries to find and return date from last updated date text
		Pattern pattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d|\\d\\d/\\d\\d/\\d\\d\\d\\d");
		Matcher matcher = pattern.matcher(text);

		if (matcher.find())
			return matcher.group();
		else
			throw new IllegalStateException("Date not found");
	}

	/**
	 * Description : Method to add or subtract days from current date
	 * 
	 * @param dateFormat [String] date format i.e "MM/dd/yyyy", dd/MM/yyyy etc
	 * @param days       [int] number of days i.e. -7, 15
	 * @return
	 * @throws Exception
	 */
	public static String getRequiredDate(String dateFormat, int days) throws Exception {
		DateFormat dfDate = new SimpleDateFormat(dateFormat);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days); // Subtracting 1 day to current date
		String newdate = dfDate.format(cal.getTime());

		if (newdate.charAt(0) == '0') {
			newdate = newdate.substring(1);
		}
		return newdate;
	}

	/**
	 * Method to convert from one date format to another *
	 * 
	 * @throws Exception
	 */
	public static String convertDate(String srcDate, String srcDateFormat, String destDateformat) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(srcDateFormat);
		Date dateStr = formatter.parse(srcDate);
		formatter = new SimpleDateFormat(destDateformat, Locale.ENGLISH);
		String FormattedDate = formatter.format(dateStr);
		System.out.println("FormattedDate" + FormattedDate);
		return FormattedDate;
	}

	/**
	 * Method to get the current date and time in yyyyMMMMddhhmmss format
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentDateTime() throws Exception {
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyyMMMMddhhmmss");
		Date now = Calendar.getInstance().getTime();
		String currentDateTime = sdfDateTime.format(now);
		return currentDateTime;
	}

	/**
	 * Description : This Method will return the difference between two dates in
	 * days
	 * 
	 * @param a [Date]
	 * @param b [Date]
	 * @return
	 */
	public static int calculate_Date_Difference(Date a, Date b) {
		int tempDifference = 0;
		int difference = 0;
		Calendar earlier = Calendar.getInstance();
		Calendar later = Calendar.getInstance();

		if (a.compareTo(b) < 0) {
			earlier.setTime(a);
			later.setTime(b);
		} else {
			earlier.setTime(b);
			later.setTime(a);
		}

		while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
			tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
			difference += tempDifference;

			earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
		}

		if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
			tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
			difference += tempDifference;

			earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
		}

		return difference;
	}

	/**
	 * Description : executes a java script when ever is needed. some times we need
	 * to execute a Js to for example to execute the js
	 * "top.body.Ext.getCmp('dateRangeMenu').items.get('defaultDateRangeButton').disabled"
	 * 
	 * @param javascript
	 * @return
	 * @throws Exception
	 */
	public Object executeJavascript(String javascript) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return js.executeScript("return " + javascript + "");
	}

	/**
	 * Description: Finds the position of the string in the Array & returns it.
	 * 
	 * @param listToSearchFrom: List from which the item is to be searched.
	 * @param searchItem:       Item that has to be searched in the list.
	 * @return Index: Position of the String in the array. Null if the string is not
	 *         found in the array.
	 */
	public Integer findItemInList(String[] listToSearchFrom, String searchItem) {
		for (int n = 0; n < listToSearchFrom.length; n++) {
			if (listToSearchFrom[n].equals(searchItem))
				return n;
		}
		return null;
	}

	/**
	 * Webdriver :-switchTo differnt window
	 * 
	 * @throws InterruptedException
	 */
	public void switchToPrintPopUp(int n) throws InterruptedException {
		Set<String> availableWindows = driver.getWindowHandles();
		Object[] ls = availableWindows.toArray();
		driver.switchTo().window((String) ls[n]);
		Thread.sleep(2000);
	}

	/**
	 * Wait will ignore instances of NotFoundException that are encountered (thrown)
	 * by default in the 'until' condition, and immediately propagate all others.
	 * You can add more to the ignore list by calling ignoring(exceptions to add).
	 * 
	 * @param driver
	 * @param n      :- how much time u want to wait
	 * @param ele    :- for which element u want to wait
	 */
	public static boolean waitForElement(WebDriver driver, int n, WebElement ele) {
		TestConfig.PrintMethodName();
		WebDriverWait wait = new WebDriverWait(driver, n);
		boolean found = false;
		try {
			found = wait.until(new WaitForWEIsDisplayedEnabled(ele));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

	/**
	 * Wait will ignore instances of NotFoundException that are encountered (thrown)
	 * by default in the 'until' condition, and immediately propagate all others.
	 * You can add more to the ignore list by calling ignoring(exceptions to add).
	 * 
	 * @param driver
	 * @param n      :- how much time u want to wait
	 * @param ele    :- for which element u want to wait
	 */
	public static boolean waitForElementInDefaultFrame(WebDriver driver, int n, WebElement ele) {
		TestConfig.PrintMethodName();
		driver.switchTo().defaultContent();
		return waitForElement(driver, n, ele);
	}

	/**
	 * Desc:- Method will set the frame.Frist will set to default Content and then
	 * to str frame
	 */
	public static void setFrame(WebDriver driver, String frameName) {
		TestConfig.PrintMethodName();
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver, 40);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
	}

	/*
	 * Desc:- Some functionality is embedded in multiple iFrames. This method will
	 * take a list of frames and keep switching to them.
	 * 
	 * @param driver
	 * 
	 * @param frames list of frames (specified in order from parent first to deepest
	 * child)
	 */
	public static void setFrameChain(WebDriver driver, List<String> frames) {
		TestConfig.PrintMethodName();

		driver.switchTo().defaultContent();
		for (String frame : frames) {
			driver.switchTo().frame(frame);
		}
	}

	/**
	 * Desc:- Method will set to default Content
	 */
	public static void setDefaultFrame(WebDriver driver) {
		TestConfig.PrintMethodName();
		driver.switchTo().defaultContent();
	}

	/**
	 * Desc:- Method give u date in Format("MMMM d, yyyy")
	 */
	public static String getDate_MMM_d_yyyy() {
		Date now = new Date();
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("PST"));
		String expectedPST = dateFormatGmt.format(now);
		return expectedPST;
	}

	/**
	 * Desc:- Method give u date in Format("MMMM")
	 */
	public static String getDate_Month() {
		Date now = new Date();
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMMM", Locale.ENGLISH);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("PST"));
		String expectedPST = dateFormatGmt.format(now);
		System.out.println("expectedPST" + expectedPST);
		return expectedPST;
	}

	/**
	 * Desc:- Method give u date in Format("d")
	 */
	public static String getDate_d() {
		Date now = new Date();
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("d", Locale.ENGLISH);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("PST"));
		String expectedPST = dateFormatGmt.format(now);
		System.out.println("expectedPST" + expectedPST);
		return expectedPST;
	}

	/**
	 * Desc:- Method give u date in Format("yyyy")
	 */
	public static String getDate_y() {
		Date now = new Date();
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy", Locale.ENGLISH);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("PST"));
		String expectedPST = dateFormatGmt.format(now);
		return expectedPST;
	}

	/**
	 * Desc:- Method give u date in Format("yyyy")
	 */
	public static String getEstTiming() {
		Date now = new Date();
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("EST"));
		String expectedPST = dateFormatGmt.format(now);
		return expectedPST;
	}

	public static String createRandomEmailAddress(String email, char delimiter) {
		TestConfig.PrintMethodName();
		String[] tmp = email.split("@");
		String randomEmail = tmp[0] + delimiter + createRandomNumber() + "@" + tmp[1];
		System.out.println("dynamic Email address " + randomEmail);
		return randomEmail;
	}

	public static String createRandomEmailAddress(String email) {
		return createRandomEmailAddress(email, '+');
	}

	public static String createRandomZip() {
		return RandomStringUtils.randomNumeric(5);
	}

	public static int createRandomNumber() {
		TestConfig.PrintMethodName();
		Random randomNumbers = new Random();
		int rnd = randomNumbers.nextInt(999999999);
		return rnd;
	}

	public static String createRandomNumericString(int length) {
		TestConfig.PrintMethodName();
		return RandomStringUtils.randomNumeric(length);
	}

	public static String createRandomNumericStringInRange(int min, int max) throws Exception {
		TestConfig.PrintMethodName();
		if (max < min) {
			throw new IllegalArgumentException();
		}
		Random rand = new Random();
		Integer randomNum = rand.nextInt(max - min) + min;
		return randomNum.toString();
	}

	public static String createRandomNumericString() {
		TestConfig.PrintMethodName();
		return createRandomNumericString(9);
	}

	/**
	 * @Desc:- Will return u data in EST format
	 * @return :- String
	 */
	public static String getEstTimingWithTime() {
		Date now = new Date();
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat();
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("EST"));
		String expectedEST = dateFormatGmt.format(now);
		System.out.println("expectedEST==========================" + expectedEST);
		return expectedEST;
	}

	/**
	 * @Description : This Method will return the difference between two dates in
	 *              hours
	 * @param a [Date]
	 * @param b [Date]
	 * @return
	 */
	public static int calculate_Date_Difference_in_Hours(Date a, Date b) {
		int tempDifference = 0;
		int difference = 0;
		Calendar earlier = Calendar.getInstance();
		Calendar later = Calendar.getInstance();

		if (a.compareTo(b) < 0) {
			earlier.setTime(a);
			later.setTime(b);
		} else {
			earlier.setTime(b);
			later.setTime(a);
		}

		if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
			tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
			difference += tempDifference;

			earlier.add(Calendar.HOUR_OF_DAY, tempDifference);
		}

		if (earlier.get(Calendar.HOUR_OF_DAY) != later.get(Calendar.HOUR_OF_DAY)) {
			tempDifference = later.get(Calendar.HOUR_OF_DAY) - earlier.get(Calendar.HOUR_OF_DAY);
			difference += tempDifference;

			earlier.add(Calendar.HOUR_OF_DAY, tempDifference);
		}

		System.out.println("difference================" + difference);
		return difference;
	}

	/**
	 * @Description:Pick the Random String
	 * @param array
	 * @return
	 */
	public static String pickRandomString(String[] array) {

		TestConfig.PrintMethodName();

		Random r = new Random();

		return array[r.nextInt(array.length)];
	}

	/**
	 * Submits form
	 */
	public void submitForm() {
		if (submitFormElement != null) {
			log("Submitting form");
			submitFormElement.click();
		} else {
			throw new UnsupportedOperationException(
					"Error when submitting form - submit form element has not been set up.");
		}
	}

	public void highlightElement(WebElement element) {
		jse.executeScript("arguments[0].setAttribute('style','border: solid 6px red');", element);
	}

	public String getDate(Calendar cal) {
		return "" + cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
	}

	public String generatRandomNum() {
		Random random = new Random();
		int randamNo = random.nextInt(100000);
		return String.valueOf(randamNo);
	}

}
