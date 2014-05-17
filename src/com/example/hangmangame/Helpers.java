package com.example.hangmangame;

import android.util.Log;

public class Helpers {
	
	public Helpers() {
		
	}

	public String findValueToKey(String result, String KeyToFind) {
		String trimmed = result.replaceAll("\"", "").replaceAll("\\{", "").replaceAll("\\}", "");
		String[] pairs = trimmed.split(",");
		Log.i("Helper", KeyToFind);
		
		for (String pair : pairs) {
			String[] element = pair.split(":");
			String key = element[0];
			String value = element[1];
			
			Log.i("Key", key);
			Log.i("Value", value);
			
			// Now do with key whatever you want with key and value...
			if (KeyToFind.equals(key)) {
				Log.i("ReturnValue", value);
				return value;
			}
		}
		return null;
	}
	
}
