package com.example.hangmangame;

import android.util.Log;

public class Helpers {
	String key;
	String value;

	public Helpers() {

	}

	public String findValueToKey(String result, String KeyToFind) {
		if(result.contains("<!DOCTYPE html>")) {
			Log.e("ERROR", "Helper didn't find the key");
			return "NO_KEY_FOUND_ERROR";
		}
		
		String trimmed = result.replaceAll("\"", "").replaceAll("\\{", "")
				.replaceAll("\\}", "");
		String[] pairs = trimmed.split(",");
		Log.i("Helper", KeyToFind);

		for (String pair : pairs) {
			String[] element = pair.split(":");

			if (element[0].equals("data")) {
				if (element[1].equals("numberOfWordsTried")) {
					key = element[1];
					value = element[2];
				} else if (element[1].equals("numberOfGuessAllowedForThisWord")) {
					key = element[1];
					value = element[2];
				} else if (element[1].equals("numberOfCorrectWords")) {
					key = element[1];
					value = element[2];
				}else if (element[1].equals("numberOfWrongGuesses")) {
					key = element[1];
					value = element[2];
				}else if (element[1].equals("totalScore")) {
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
				Log.i("Found the " + key, value);
				return value;
			}
		}
		Log.e("ERROR", "Helper didn't find the key");
		return "NO_KEY_FOUND_ERROR";
	}

}
