package com.example.hangmangame;

import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
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
import android.widget.Toast;

import com.example.constant.Action;
import com.example.constant.BuildConfig;
import com.example.preference.PreferenceManager;
import com.example.server.comm.AsyncTaskCompleteListener;
import com.example.server.comm.HttpRequest;
import com.example.server.comm.InitiateGame;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private String mSecret;
	private Context mContext = this;

	private PreferenceManager mPreferenceManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//커스톰 액션바 구현.
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(R.layout.actionbar);
		
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
			new InitiateGame(new AfterInitiateGame()).execute();
		}
	}
	
	private class AfterInitiateGame implements AsyncTaskCompleteListener<String> {

		@Override
		public void onTaskComplete(String result) {
			if ("NO_KEY_FOUND_ERROR".equals(result)) {
				Toast.makeText(mContext, R.string.error_on_getting_secret, Toast.LENGTH_LONG).show();
			}
			else {
				// saving the information
				//mPreferenceManager.setSecret(getApplicationContext(),result);
				
				Toast.makeText(mContext, R.string.welcome, Toast.LENGTH_SHORT).show();
				
				BuildConfig.SECRET = result;
				
				//Open up the StartActivity
				Intent playIntent = new Intent(mContext, GameActivity.class);
				playIntent.putExtra("Secret", result);
				mContext.startActivity(playIntent);
			}
			
		}

		@Override
		public void onTaskComplete(String result, String result2, String result3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTaskComplete(String result, String result2,
				String result3, String result4) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
