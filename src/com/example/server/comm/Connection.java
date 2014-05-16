package com.example.server.comm;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Connection {
	Context context;

	public void connect(Context tmpContext) {
		context = tmpContext;
		// check if you are connected or not
		if (isConnected()) {
			Log.i("Connection", "Connected");
		} else {
			Log.i("Connection", "NOT Connected");
		}
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}
}
