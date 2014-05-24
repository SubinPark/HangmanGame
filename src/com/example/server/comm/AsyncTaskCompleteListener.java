package com.example.server.comm;

/**
 * AsyncTask 후속처리를 위한 콜백
 * @author subinpark
 *
 * @param <T>
 */
public interface AsyncTaskCompleteListener<T> {

	public void onTaskComplete(T result);
	public void onTaskComplete(T result, T result2, T result3);
	public void onTaskComplete(T result, T result2, T result3, T result4);

	//public void onTaskComplete(String word, String wordsTried,
			//String guessAllowed);

}