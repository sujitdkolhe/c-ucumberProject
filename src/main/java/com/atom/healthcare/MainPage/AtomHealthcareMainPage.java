package com.atom.healthcare.MainPage;

import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Level;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.atom.healthcare.util.BasePageObject;

public class AtomHealthcareMainPage extends BasePageObject {

	public AtomHealthcareMainPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public AtomHealthcareMainPage(WebDriver driver, String url) {
		super(driver);
		if (url != null) {
			String sanitizedUrl = url.trim();
			driver.get(sanitizedUrl);

		}

		driver.manage().window().maximize();
		printCookies();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		PageFactory.initElements(driver, this);
	}

	public void printCookies() {
		Set<Cookie> cookies = driver.manage().getCookies();
		log("Printing Cookies -------", Level.DEBUG);
		for (Cookie c : cookies) {
			log(c.toString(), Level.DEBUG);
		}
		log("--------------------------", Level.DEBUG);
	}

	public boolean assessPageElements(ArrayList<WebElement> allElements) {
		log("Checking page elements");

		for (WebElement element : allElements) {
			int attempt = 1;
			while (attempt < 3) {
				log("Searching for element: " + element.toString(), Level.DEBUG);
				try {
					wait.until(ExpectedConditions.visibilityOf(element));
					log(elementToString(element) + " : is displayed", Level.DEBUG);
					attempt = 3;
				} catch (StaleElementReferenceException ex) {
					log("StaleElementReferenceException was catched, attempt: " + attempt++, Level.DEBUG);
				} catch (TimeoutException ex) {
					log(ex.getMessage());
					log(elementToString(element) + " was not assessed successfuly due to TimeoutException",
							Level.ERROR);
					return false;
				} catch (Exception ex) {
					ex.printStackTrace();
					log(elementToString(element) + " was not assessed successfuly due to some Exception", Level.ERROR);
					return false;
				}
			}
		}
		return true;
	}

	public String elementToString(WebElement element) {
		return "Element (id: " + element.getAttribute("id") + ", tag: " + element.getTagName() + ")";
	}

}
