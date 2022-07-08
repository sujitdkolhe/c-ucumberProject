package com.atom.healthcare.Hooks;

import com.atom.healthcare.util.BaseTest;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {
	BaseTest baseTest = new BaseTest();

	@Before
	public void beforeSuite(Scenario method) throws Exception {
		baseTest.beforeSuite();
		baseTest.setUp(method);
		//baseTest.testSetup(method);
	}

	@After
	public void postTestCase(Scenario result) throws Exception {
		//baseTest.tearDown(result);
		baseTest.postTestCase(result);
		
	}
}
