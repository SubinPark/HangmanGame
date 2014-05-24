package com.example.server.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.example.constant.Action;
import com.example.constant.BuildConfig;
import com.example.extra.Helpers;

/**
 * Getting the next word
 * 
 * @author SubinPark
 *
 */
public class NextWord extends AsyncTask<String, Void, String> implements Action {

	private final AsyncTaskCompleteListener<String> callback;

	public NextWord(AsyncTaskCompleteListener<String> callback) {
		this.callback = callback;
	}

	@Override
	protected String doInBackground(String... params) {
		List<NameValuePair> nameValuePairs = null;
		InputStream inputStream = null;
		String result = "";

		// Creating HTTP client
		HttpClient httpClient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(
				"http://strikingly-interview-test.herokuapp.com/guess/process");

		// Building the parameters that will send to the server

		nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("action", NEXT));
		nameValuePairs
				.add(new BasicNameValuePair("userId", BuildConfig.USERID));
		nameValuePairs
				.add(new BasicNameValuePair("secret", BuildConfig.SECRET));

		Log.i("NextWord", BuildConfig.SECRET);
		
		// Add the parameter to HttpPost
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return "Error_addingParameter";
		}

		// Sending the Request
		try {
			HttpResponse response = httpClient.execute(httppost);

			// receive response as inputStream
			inputStream = response.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = Helpers.convertInputStreamToString(inputStream);
			else
				result = "Error_convertingString";

			// writing response to log
			Log.d("Http Response:", result);
		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		Helpers findKey = new Helpers();
		String message = findKey.findValueToKey(result, "message");
		if (message.equals("NO_KEY_FOUND_ERROR")) {
			// there is no message, precede
			String word = findKey.findValueToKey(result, "word");
			String wordsTried = findKey.findValueToKey(result,
					"numberOfWordsTried");
			String guessAllowed = findKey.findValueToKey(result,
					"numberOfGuessAllowedForThisWord");

			// Starting callback method
			if (callback != null)
				callback.onTaskComplete(word, wordsTried, guessAllowed);
		} else { //if there's a message
			if (callback != null)
				callback.onTaskComplete(message);
		}
	}

}
