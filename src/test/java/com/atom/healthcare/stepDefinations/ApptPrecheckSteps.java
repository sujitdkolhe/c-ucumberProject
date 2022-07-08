package com.atom.healthcare.stepDefinations;

import java.io.IOException;

import com.atom.healthcare.Login.HealthcareLogin;
import com.atom.healthcare.MainPage.AtomHealthcareMainPage;
import com.atom.healthcare.Pojo.Constants;
import com.atom.healthcare.util.BaseTest;
import com.atom.healthcare.util.PropertyFileLoader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class ApptPrecheckSteps extends BaseTest {
	HealthcareLogin loginPage;
	AtomHealthcareMainPage mainPage;
	PropertyFileLoader propertyData;
	
	@Given("user lauch atom healthcare url")
	public void user_lauch_atom_healthcare_url() throws IOException {
		propertyData = new PropertyFileLoader();
		//loginPage = new HealthcareLogin(Constants.driver, "http://www.google.com");
		//Background: Login to atom healthcare portal
		loginPage = new HealthcareLogin(Constants.driver, propertyData.getProperty("url"));
	}
	
	@When("user enter username and password")
	public void user_enter_username_and_password() throws Exception {

	}
	@Then("schedule an appointments within one month")
	public void schedule_an_appointments_within_one_month() {
	}       
}