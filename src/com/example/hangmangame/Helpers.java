package com.example.hangmangame;

import android.util.Log;

public class Helpers {
	String key;
	String value;

	public Helpers() {

	}

	public String findValueToKey(String result, String KeyToFind) {
		String trimmed = result.replaceAll("\"", "").replaceAll("\\{", "")
				.replaceAll("\\}", "");
		String[] pairs = trimmed.split(",");
		Log.i("Helper", KeyToFind);

		for (String pair : pairs) {
			String[] element = pair.split(":");
			if (element[0].equals("message")) {
				if (element[1].equals("Invalid Session")) {
					return "ERROR. Push the Button Again.";
				}
			}
			
			else if (element[0].contains("<!DOCTYPE html>")) {
				return "ERROR";
			}

			else if (element[0].equals("data")) {
				if (element[1].equals("numberOfWordsTried")) {
					key = element[1];
					value = element[2];
				} else if (element[1].equals("numberOfGuessAllowedForThisWord")) {
					key = element[1];
					value = element[2];
				}
			} else {
				key = element[0];
				value = element[1];
			}
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
