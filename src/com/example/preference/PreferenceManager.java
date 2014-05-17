package com.example.preference;

import com.example.constant.BuildConfig;
import com.example.preference.PreferenceManagerInterface;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager implements PreferenceManagerInterface {
	
	public void setSecret(Context context, String secret) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(PREF_SECRET, secret);
		editor.commit();
	}
	public String getSecret(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getString(PREF_SECRET, BuildConfig.SECRET);
	}

	public void setWord(Context context, String word) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(PREF_WORD, word);
		editor.commit();
	}
	public String getWord(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getString(PREF_WORD, BuildConfig.WORD);
	}
	
}
