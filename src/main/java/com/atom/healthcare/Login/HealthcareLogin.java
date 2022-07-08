package com.atom.healthcare.Login;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.atom.healthcare.MainPage.AtomHealthcareMainPage;
import com.atom.healthcare.util.BasePageObject;



public class HealthcareLogin extends AtomHealthcareMainPage {

	@FindBy(how = How.ID, using = "username")
	private WebElement inputUserName;

	@FindBy(how = How.ID, using = "password")
	private WebElement inputPassword;

	@FindBy(how = How.LINK_TEXT, using = "Login")
	private WebElement buttonLogin;


	public HealthcareLogin(WebDriver driver) {
		super(driver);
	}

	public HealthcareLogin(WebDriver driver, String url) {
		super(driver, url);
	}

	BasePageObject baseMethods = new BasePageObject(driver);

	public void login(String username, String pass) throws InterruptedException {
		baseMethods.highlightElement(inputUserName);
		inputUserName.sendKeys(username);
		baseMethods.highlightElement(inputPassword);
		inputPassword.sendKeys(pass);
		buttonLogin.click();
		PageFactory.initElements(driver, this);
	}

}
