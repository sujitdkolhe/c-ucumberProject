package com.atom.healthcare.util;

public class OSTypeUtil {
	public enum OSType {
		windows, mac, linux,  
	}
	
	public static OSType getTestClientOSType(String osType){
		if (osType.toLowerCase().contains("win"))
			return OSType.windows;
		else if (osType.toLowerCase().contains("mac"))
			return OSType.mac;
		else if (osType.toLowerCase().contains("linux"))
			return OSType.linux;
		else 
			return OSType.windows; //default to window
	}
	
}
