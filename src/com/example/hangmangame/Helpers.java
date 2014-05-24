package com.example.hangmangame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class Helpers {
	static String key;
	static String value;

	public static String findValueToKey(String result, String KeyToFind) {
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
	
	public static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	} 

}
