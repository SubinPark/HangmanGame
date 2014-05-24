package com.example.server.comm;

/**
 * Callback for after AsyncTask
 * @author SubinPark
 *
 * @param <T>
 */
public interface AsyncTaskCompleteListener<T> {

	public void onTaskComplete(T result);
	public void onTaskComplete(T word, T wordsTried, T guessAllowed);
}