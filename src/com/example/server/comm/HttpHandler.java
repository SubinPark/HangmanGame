package com.example.server.comm;

import org.apache.http.client.methods.HttpUriRequest;


public abstract class HttpHandler {

	public abstract HttpUriRequest getHttpRequestMethod();
	
	public abstract void onResponse(String result);
	
	public void execute(){
		synchronized (this) {
			/**
		    try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}**/
		}
		new AsyncHttpTask(this).execute();
	}

	
}
