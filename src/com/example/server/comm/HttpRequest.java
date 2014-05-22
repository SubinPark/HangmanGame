package com.example.server.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.constant.Action;
import com.example.constant.BuildConfig;

public class HttpRequest extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		List<NameValuePair> nameValuePairs = null;
		InputStream inputStream = null;
		String result = "";
		
		// Creating HTTP client
		HttpClient httpClient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(
				"http://strikingly-interview-test.herokuapp.com/guess/process");
		try {
			if (params[0].equals(Action.INIT)) {
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("action", Action.INIT));
				nameValuePairs.add(new BasicNameValuePair("userId",
						BuildConfig.USERID));
			}
			else if (params[0].equals(Action.NEXT)) {
				nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("action", Action.NEXT));
				nameValuePairs.add(new BasicNameValuePair("userId",
						BuildConfig.USERID));
				nameValuePairs.add(new BasicNameValuePair("secret",
						BuildConfig.SECRET));
			}
			
			else if (params[0].equals(Action.GUESS)) {
				nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs.add(new BasicNameValuePair("action", Action.GUESS));
				nameValuePairs.add(new BasicNameValuePair("userId",
						BuildConfig.USERID));
				nameValuePairs.add(new BasicNameValuePair("secret",
						BuildConfig.SECRET));
				nameValuePairs.add(new BasicNameValuePair("guess",
						params[1]));
			}
			else if (params[0].equals(Action.GET_RESULT)) {
				nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("action", Action.GET_RESULT));
				nameValuePairs.add(new BasicNameValuePair("userId",
						BuildConfig.USERID));
				nameValuePairs.add(new BasicNameValuePair("secret",
						BuildConfig.SECRET));
			}
			else if (params[0].equals(Action.SUBMIT_RESULT)) {
				nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("action", Action.SUBMIT_RESULT));
				nameValuePairs.add(new BasicNameValuePair("userId",
						BuildConfig.USERID));
				nameValuePairs.add(new BasicNameValuePair("secret",
						BuildConfig.SECRET));
			}
			
			
			
			
			// Add your data
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		
		// Making HTTP Request
		try {
			HttpResponse response = httpClient.execute(httppost);

			// receive response as inputStream
			inputStream = response.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

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
		// httpHandler.onResponse(result);
	}
	
	 private static String convertInputStreamToString(InputStream inputStream) throws IOException{
	        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	        String line = "";
	        String result = "";
	        while((line = bufferedReader.readLine()) != null)
	            result += line;
	        
	        inputStream.close();
	        return result;
	        
	    }

}