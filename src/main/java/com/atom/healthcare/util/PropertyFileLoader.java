// Copyright 2013-2021 NXGN Management, LLC. All Rights Reserved.
package com.atom.healthcare.util;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyFileLoader {

	private Properties property = new Properties();

	// Universal property loading
	public String getProperty(String prop) throws NullPointerException {
		if (property.getProperty(prop) == null)
			throw new NullPointerException("Property " + prop + " not found in the property file.");
		return property.getProperty(prop);
	}

	public PropertyFileLoader() throws IOException {

		String env = TestConfig.getEnvironmentType().toString();
		String propertyFileNameString = env + ".properties";

		URL url = ClassLoader.getSystemResource("data-driven/" + propertyFileNameString);
		FileReader inputStream = new FileReader(url.getFile());
		property.load(inputStream);

	}

	public Map<String, String> getPropertySubtree(String key) {
		Map<String, String> subtree = new HashMap<String, String>();

		for (String propertyName : property.stringPropertyNames()) {
			if (propertyName.startsWith(key)) {
				subtree.put(propertyName.replace(key + ".", ""), getProperty(propertyName));
			}
		}

		return subtree;
	}

}
