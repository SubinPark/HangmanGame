package com.example.hangmangame;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.constant.BuildConfig;
import com.example.preference.PreferenceManager;
import com.example.server.comm.AsyncTaskCompleteListener;
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
		getSupportActionBar().setCustomView(R.layout.actionbar_main);
		
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
			Toast.makeText(mContext, "Initializing. Can take up to 1 min.", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(mContext, R.string.welcome, Toast.LENGTH_SHORT).show();
				
				BuildConfig.SECRET = result;
				
				//Open up the GameActivity
				Intent playIntent = new Intent(mContext, GameActivity.class);
				playIntent.putExtra("secret", result);
				mContext.startActivity(playIntent);
			}
			
		}

		@Override
		public void onTaskComplete(String result, String result2, String result3) {
		}
		
	}

}
