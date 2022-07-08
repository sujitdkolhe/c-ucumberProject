package com.atom.healthcare.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.Reporter;

public class Log4jUtil {
	static Logger logger = Logger.getLogger(Log4jUtil.class);

	public static Level getLogLevel() {
		String _level = TestConfig.getLoggerLevel();
		if (_level.equalsIgnoreCase("debug"))
			return Level.DEBUG;
		else
			return Level.INFO;
		// we can do other level in the future.
	}

	public static void log(String message) {
		logger.info(message);
		Reporter.log(message + "<br/>", false);
	}

	public static void log(String message, Level lv) {
		Level _lv = getLogLevel();
		if (_lv.toInt() <= lv.toInt()) {
			if (lv == Level.INFO)
			{
				logger.info(message);
				Reporter.log(message + "<br/>", false);
			}
			if (lv == Level.DEBUG)
			{
				logger.debug(message);
				Reporter.log(message + "<br/>", false);
			}
			if (lv == Level.ERROR)
			{
				logger.error(message);
				Reporter.log(message + "<br/>", false);
			}
		}
	}

	public static void log(String message, String lv) {
		if (lv.equalsIgnoreCase("debug")) {
			log(message, Level.DEBUG);
		} else if (lv.equalsIgnoreCase("error")) {
			log(message, Level.ERROR);
		} else {
			// default is info
			log(message, Level.INFO);
		}
	}
}
