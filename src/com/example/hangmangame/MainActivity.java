package com.example.hangmangame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.constant.BuildConfig;
import com.example.server.comm.HttpHandler;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	String mSecret;
	Context mContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button playBtn = (Button) findViewById(R.id.playBtn);
		playBtn.setOnClickListener(this);

		/**
		 * if (savedInstanceState == null) {
		 * getSupportFragmentManager().beginTransaction() .add(R.id.container,
		 * new PlaceholderFragment()).commit(); }
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_main, container,
					false);
			return rootView;
		}
	}

	// TODO ****************Initialize a game here
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.playBtn) {
			new HttpHandler() {
				public HttpUriRequest getHttpRequestMethod() {
					HttpPost httppost = new HttpPost(
							"http://strikingly-interview-test.herokuapp.com/guess/process");
					try {
						// Add your data
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								3);
						nameValuePairs.add(new BasicNameValuePair("action",
								BuildConfig.INIT));
						nameValuePairs.add(new BasicNameValuePair("userId",
								BuildConfig.USERID));
						// nameValuePairs.add(new BasicNameValuePair("secret",
						// "4MT1HQAE05W5EWMIV4QNK10U51NN4S"));
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs));

					} catch (IOException e) {
						// TODO Auto-generated catch block
					}

					return httppost;

				}

				public void onResponse(String result) {////////////////***************Here, this thread gets slower than the other
					Toast.makeText(getBaseContext(), "Requested!",
							Toast.LENGTH_LONG).show();

					// Split and get the 'secret' and save it to a BuildConfig.secret
					Log.e("Here the Result", result);
					
					Helpers test = new Helpers();
					String returnedValue = test.findValueToKey(result, "secret");
					Log.e("Returned", returnedValue);
					if(returnedValue != null) {
						Log.e("null?", "no");
						BuildConfig.SECRET = returnedValue;
						savePreferences("Secret", returnedValue);
					}
					
					/**
					String[] pairs = result.split(",");
					
					for(String pair: pairs) {
						   String[] element = pair.split(":");
						   String key = element[0];
						   String value = element[1];
						   
						   // Now do with key whatever you want with key and value...
						   if(key.equals("\"secret\"")) {
						       BuildConfig.SECRET = value;
						       Log.i("HERE", value);
						       mSecret = value;
						       savePreferences("Secret", value);
						       
						
							}
						}
						**/
				}
				
			}.execute();
			
			synchronized (HttpHandler.class) {
			    try {
					HttpHandler.class.wait(2000); // TODO couldn't figure out where notify should be. So for temporarily, I put 1500milliseconds
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


			
			savePreferences("Secret", mSecret); // for the value, add the result string after splitting
			loadSavedPreferences();
			
			Intent playIntent = new Intent(this, GameActivity.class);
			this.startActivity(playIntent);
		}
	}

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private void loadSavedPreferences() {
	
		        SharedPreferences sharedPreferences = PreferenceManager
		                .getDefaultSharedPreferences(this);
		        BuildConfig.SECRET = sharedPreferences.getString("Secret", "4MT1HQAE05W5EWMIV4QNK10U51NN4S");
		
		    }

	
}
