package com.atom.healthcare.util;

public class BrowserTypeUtil {
	public enum BrowserType {
		iexplore, firefox, safari, chrome, opera, htmlunit
	}
	
	public static BrowserType getBrowserType(String browser){
		if (browser == null)
			return BrowserType.htmlunit;
		if (browser.equalsIgnoreCase("*iexplore") || browser.equalsIgnoreCase("iexplore"))
			return BrowserType.iexplore;
		else if (browser.equalsIgnoreCase("*firefox") || browser.equalsIgnoreCase("firefox"))
			return BrowserType.firefox;
		else if (browser.equalsIgnoreCase("*safari") || browser.equalsIgnoreCase("safari"))
			return BrowserType.safari;
		else if (browser.equalsIgnoreCase("*chrome") || browser.equalsIgnoreCase("chrome"))
			return BrowserType.chrome;
		else if (browser.equalsIgnoreCase("*opera") || browser.equalsIgnoreCase("opera"))
			return BrowserType.opera;		
		else 
			return BrowserType.htmlunit; 
	}
	
}