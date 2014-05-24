package com.example.hangmangame;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.constant.BuildConfig;
import com.example.preference.PreferenceManager;
import com.example.server.comm.AsyncTaskCompleteListener;
import com.example.server.comm.Connection;
import com.example.server.comm.GetTestResults;
import com.example.server.comm.GuessWord;
import com.example.server.comm.NextWord;

@SuppressLint("NewApi")
public class GameActivity extends Activity implements OnClickListener {

	private String mSecret;

	// the words
	private String currWord;

	// the layout holding the answer
	private LinearLayout textLayout;

	// text views for each letter in the answer
	private TextView textView;
	private TextView wordTriedView;
	private TextView guessAllowedView;

	// letter button grid
	private GridView letters;

	// letter button adapter
	private LetterAdapter ltrAdapt;

	// body part images
	private ImageView[] bodyParts;

	// total parts
	private int numParts = 6;

	// current part
	private int currPart;

	// Next button
	private Button buttonNext;

	private String numberOfGuessAllowedForThisWord;
	private String numberOfWordsTried;

	private boolean pushToStart = true;

	private PreferenceManager mPreferenceManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// Get the secret
		Intent intent = getIntent();
		mSecret = intent.getStringExtra("secret");

		// get answer area
		textLayout = (LinearLayout) findViewById(R.id.word);

		// get letter button grid
		letters = (GridView) findViewById(R.id.letters);

		// get body part images
		bodyParts = new ImageView[numParts];
		bodyParts[0] = (ImageView) findViewById(R.id.head);
		bodyParts[1] = (ImageView) findViewById(R.id.body);
		bodyParts[2] = (ImageView) findViewById(R.id.arm1);
		bodyParts[3] = (ImageView) findViewById(R.id.arm2);
		bodyParts[4] = (ImageView) findViewById(R.id.leg1);
		bodyParts[5] = (ImageView) findViewById(R.id.leg2);

		// get button
		buttonNext = (Button) findViewById(R.id.next);
		buttonNext.setOnClickListener(this);

		// get textviews
		wordTriedView = (TextView) findViewById(R.id.NumberOfWordsTried);
		guessAllowedView = (TextView) findViewById(R.id.NumberOfGuessAllowedForThisWord);

		// set home as up
		getActionBar().setDisplayHomeAsUpEnabled(true);

		setPlatform();

		new NextWord(new AfterNextWord()).execute();
	}

	private void setWord(String wordToSet) {

		textView = new TextView(this);
		// set the current letter
		textView.setText(wordToSet);

		// set layout
		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.BLACK);
		textView.setBackgroundResource(R.drawable.letter_bg);

		// remove any existing letters
		textLayout.removeAllViews();

		textLayout.addView(textView);
	}

	private void setPlatform() {
		// reset adapter
		ltrAdapt = new LetterAdapter(this);
		letters.setAdapter(ltrAdapt);

		// start part at zero
		currPart = 0;
		// numCorr = 0;

		// hide all parts
		for (int p = 0; p < numParts; p++) {
			bodyParts[p].setVisibility(View.INVISIBLE);
		}
	}

	// letter pressed method
	public void letterPressed(View view) {

		// find out which letter was pressed
		String ltr = ((TextView) view).getText().toString();
		Log.i("Letter pressed is", ltr);

		// disable view
		view.setEnabled(false);
		view.setBackgroundResource(R.drawable.letter_down);

		new GuessWord(new AfterGuessWord()).execute(ltr);

	}

	// disable letter buttons
	public void disableBtns() {
		int numLetters = letters.getChildCount();
		for (int l = 0; l < numLetters; l++) {
			letters.getChildAt(l).setEnabled(false);
		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.next) {
			new NextWord(new AfterNextWord()).execute();
		}
	}

	private class AfterNextWord implements AsyncTaskCompleteListener<String> {

		public void onTaskComplete(String returnValue) {
		}

		@Override
		public void onTaskComplete(String result, String result2, String result3) {
			if ("NO_KEY_FOUND_ERROR".equals(result)) {
				Toast.makeText(getApplicationContext(),
						R.string.error_on_getting_word, Toast.LENGTH_LONG)
						.show();
			} else {
				// update the word info on screen
				setWord(result);
				currWord = result;
				wordTriedView.setText("Number of words you have tried: "
						+ result2);
				guessAllowedView.setText("Remaining number allowed for guess: "
						+ result3);
			}

		}

		@Override
		public void onTaskComplete(String result, String result2,
				String result3, String result4) {
			// TODO Auto-generated method stub

		}

	}

	private class AfterGuessWord implements AsyncTaskCompleteListener<String> {

		public void onTaskComplete(String returnValue) {

		}

		@Override
		public void onTaskComplete(String result, String result2, String result3) {
			if ("NO_KEY_FOUND_ERROR".equals(result)) {
				Toast.makeText(getApplicationContext(),
						R.string.error_on_getting_word, Toast.LENGTH_LONG)
						.show();
				Log.e("AFTER_GUESS_WORD_TASK_COMPLETE", "ERROR");
			} else {
				// guess is correct
				Log.i("Guess checking", "No error");
				if (!result.equals(currWord)) {
					Log.i("Guess checking", "guess is correct");
					if (result.contains("*")) { // still has *
						Log.i("Guess checking", "Still has *");
						// saving the information and continue playing
						currWord = result;
						setWord(result);
						wordTriedView
								.setText("Number of words you have tried: "
										+ result2);
						guessAllowedView
								.setText("Remaining number allowed for guess: "
										+ result3);

					} else { // got all letters correct
						Log.i("Guess checking", "Got the word right");

						AlertDialog.Builder loseBuild = new AlertDialog.Builder(
								GameActivity.this);
						loseBuild.setTitle("YEAHHHHSSSS");
						loseBuild.setMessage("You got the word right");
						loseBuild.setPositiveButton("Next Word",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										GameActivity.this.onClick(buttonNext);
										// reset!
										setPlatform();
									}
								});

						loseBuild.setNegativeButton("Exit",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										GameActivity.this.finish();
									}
								});

						loseBuild.create().show();

					}
				}

				// guess is wrong
				else if (currPart < numParts) { // but still has guessAllowed
												// left
					Log.i("Guess checking",
							"Guess was wrong but still have a chance");
					bodyParts[currPart].setVisibility(View.VISIBLE);
					currPart++;
					setWord(result);
					wordTriedView.setText("Number of words you have tried: "
							+ result2);
					guessAllowedView
							.setText("Remaining number allowed for guess: "
									+ result3);
					if (currPart == numParts) { // maximum guess reached
						Log.i("Guess checking", "No more guess allowed");
						if (result2.equals("80")) { // wordsTried is 80
							Log.i("Guess checking", "That was a 80th word");
							// TODO showResult();

							disableBtns();
							// Display Alert Dialog
							AlertDialog.Builder loseBuild = new AlertDialog.Builder(
									GameActivity.this);
							loseBuild.setTitle("Results");
							loseBuild.setMessage("You finished the game.");
							loseBuild.setPositiveButton("Submit",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											//GameActivity.this.onClick(submit);
										}
									});

							loseBuild.setNegativeButton("Exit",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											GameActivity.this.finish();
										}
									});

							loseBuild.create().show();

						} else { // No more guess allowed for this word
							Log.i("Guess checking",
									"No more guess for this word, have to get a new word");
							disableBtns();
							// Display Alert Dialog
							AlertDialog.Builder loseBuild = new AlertDialog.Builder(
									GameActivity.this);
							loseBuild.setTitle("OOPS");
							loseBuild
									.setMessage("You ran out of guess allowed.");
							loseBuild.setPositiveButton("Next Word",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											GameActivity.this
													.onClick(buttonNext);
											// reset!
											setPlatform();
										}
									});

							loseBuild.setNegativeButton("Exit",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											GameActivity.this.finish();
										}
									});

							loseBuild.create().show();

						}
					}

				}

			}

		}

		@Override
		public void onTaskComplete(String result, String result2,
				String result3, String result4) {
			// TODO Auto-generated method stub

		}

	}

	private class AfterGetTestResults implements AsyncTaskCompleteListener<String> {

		public void onTaskComplete(String returnValue) {

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

	private class AfterSubmitTestResults implements AsyncTaskCompleteListener<String> {

		public void onTaskComplete(String returnValue) {

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
