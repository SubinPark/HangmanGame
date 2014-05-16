package com.example.hangmangame;

public class Helpers {
	
	public Helpers() {
		
	}

	public String findValueToKey(String result, String KeyToFind) {
		String trimmed = result.replace("\"", "");
		String[] pairs = result.split(",");

		for (String pair : pairs) {
			String[] element = pair.split(":");
			String key = element[0];
			String value = element[1];

			// Now do with key whatever you want with key and value...
			if (key.equals(KeyToFind)) {
				return value;
			}
		}
		return null;
	}
	
}
