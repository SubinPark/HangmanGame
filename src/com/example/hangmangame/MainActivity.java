package com.example.hangmangame;

import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.constant.Action;
import com.example.constant.BuildConfig;
import com.example.preference.PreferenceManager;
import com.example.server.comm.HttpRequest;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private String mSecret;
	private Context mContext = this;

	private PreferenceManager mPreferenceManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button playBtn = (Button) findViewById(R.id.playBtn);
		playBtn.setOnClickListener(this);

		mPreferenceManager = new PreferenceManager();
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

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.playBtn) {
			//initialize game
			new HttpRequest().onPreExecute(getApplicationContext());
			try {
				String response = new HttpRequest().execute(Action.INIT).get();
				Log.i("http response", response);

				// splitting the string to find secret
				Helpers splitString = new Helpers();
				String returnedValue = splitString.findValueToKey(response,
						"secret");

				// saving the information
				mPreferenceManager.setSecret(getApplicationContext(),
						returnedValue);
				BuildConfig.SECRET = returnedValue;

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Intent playIntent = new Intent(this, GameActivity.class);
			this.startActivity(playIntent);
		}
	}

}
