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
 * Getting test result.
 * 
 * @author SubinPark
 *
 */
public class GetTestResults extends AsyncTask<String, Void, String> implements Action {

	private final AsyncTaskCompleteListener<String> callback;

	public GetTestResults(AsyncTaskCompleteListener<String> callback) {
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
		nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("action", GET_RESULT));
		nameValuePairs
				.add(new BasicNameValuePair("userId", BuildConfig.USERID));
		nameValuePairs
				.add(new BasicNameValuePair("secret", BuildConfig.SECRET));

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
		String wordsTried = findKey
				.findValueToKey(result, "numberOfWordsTried");
		String correctWords = findKey.findValueToKey(result,
				"numberOfCorrectWords");
		String wrongGuesses = findKey.findValueToKey(result,
				"numberOfWrongGuesses");
		String totalScore = findKey.findValueToKey(result, "totalScore");
		String returnValue = "Good job! Here is your result. You got "
				+ correctWords + " out of " + wordsTried + " words tried with "
				+ wrongGuesses + " wrong guesses. So the total score is "
				+ totalScore;

		// Starting callback method
		if (callback != null)
			callback.onTaskComplete(returnValue);
	}

}
