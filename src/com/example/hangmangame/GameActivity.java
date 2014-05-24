package com.example.hangmangame;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.constant.BuildConfig;
import com.example.server.comm.AsyncTaskCompleteListener;
import com.example.server.comm.GetTestResults;
import com.example.server.comm.GuessWord;
import com.example.server.comm.NextWord;
import com.example.server.comm.SubmitTestResults;

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
	private int numParts = 10;

	// current part
	private int currPart;

	// Next button
	private Button buttonSkip;
	private Button buttonBack;

	private String numberOfGuessAllowedForThisWord;
	private String numberOfWordsTried;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// Customize action bar
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar_game);

		// Get the secret from MainActivity
		Intent intent = getIntent();
		mSecret = intent.getStringExtra("secret"); // commenting out for testing
		BuildConfig.SECRET = mSecret;

		// mSecret = "IZEBSRVCRFJKBJ4IE880V14YJVE7B9";
		// BuildConfig.SECRET = "IZEBSRVCRFJKBJ4IE880V14YJVE7B9";

		// get answer area
		textLayout = (LinearLayout) findViewById(R.id.word);

		// get letter button grid
		letters = (GridView) findViewById(R.id.letters);

		// get body part images
		bodyParts = new ImageView[numParts];
		bodyParts[0] = (ImageView) findViewById(R.id.gallows);
		bodyParts[1] = (ImageView) findViewById(R.id.head);
		bodyParts[2] = (ImageView) findViewById(R.id.body);
		bodyParts[3] = (ImageView) findViewById(R.id.arm1);
		bodyParts[4] = (ImageView) findViewById(R.id.arm2);
		bodyParts[5] = (ImageView) findViewById(R.id.leg1);
		bodyParts[6] = (ImageView) findViewById(R.id.leg2);
		bodyParts[7] = (ImageView) findViewById(R.id.shoe1);
		bodyParts[8] = (ImageView) findViewById(R.id.shoe2);
		bodyParts[9] = (ImageView) findViewById(R.id.no);

		// get buttons
		buttonSkip = (Button) findViewById(R.id.btn_skip);
		buttonSkip.setOnClickListener(this);
		buttonBack = (Button) findViewById(R.id.btn_back);
		buttonBack.setOnClickListener(this);

		// get textviews
		wordTriedView = (TextView) findViewById(R.id.NumberOfWordsTried);
		guessAllowedView = (TextView) findViewById(R.id.NumberOfGuessAllowedForThisWord);

		// set home as up
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Set up and get a word
		setPlatform();
		new NextWord(new AfterNextWord()).execute();
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
		if (v.getId() == R.id.btn_skip) {
			new NextWord(new AfterNextWord()).execute();
			Toast.makeText(getApplicationContext(), R.string.skip,
					Toast.LENGTH_SHORT).show();
		}
		if (v.getId() == R.id.btn_back) {
			finish();
		}
	}
	
	
	
	
	
	/////////////////// background callbacks after AsyncTask //////////////////

	private class AfterNextWord implements AsyncTaskCompleteListener<String> {

		public void onTaskComplete(String returnValue) {
			if (returnValue.contains("80")) { // wordsTried is 80
				Log.i("Guess checking2", "That was a 80th word");
				// showResult
				disableBtns();
				new GetTestResults(new AfterGetTestResults()).execute();
			}
		}

		@Override
		public void onTaskComplete(String word, String wordsTried, String guessAllowed) {
			if ("NO_KEY_FOUND_ERROR".equals(word)) {
				Toast.makeText(getApplicationContext(),
						R.string.error_on_getting_word, Toast.LENGTH_LONG)
						.show();
			} else {
				// update the word info on screen
				setPlatform();

				if (currPart == numParts) { // TODO 이게 맞나요??
					if (wordsTried.equals("80")) { // wordsTried is 80
						Log.i("Guess checking", "That was a 80th word");
						// showResult
						disableBtns();
						new GetTestResults(new AfterGetTestResults()).execute();
					}
				}
				setWordViews(word, wordsTried, guessAllowed);
			}

		}

	}

	private class AfterGuessWord implements AsyncTaskCompleteListener<String> {

		public void onTaskComplete(String returnValue) {
		}

		@Override
		public void onTaskComplete(String word, String wordsTried, String guessAllowed) {
			if ("NO_KEY_FOUND_ERROR".equals(word)) {
				Toast.makeText(getApplicationContext(),
						R.string.error_on_getting_word, Toast.LENGTH_LONG)
						.show();
				Log.e("AFTER_GUESS_WORD_TASK_COMPLETE", "ERROR");
			} else {
				// guess is correct
				Log.i("Guess checking", "No error");
				if (!word.equals(currWord)) {
					Log.i("Guess checking", "guess is correct");
					if (word.contains("*")) { // still has *
						Log.i("Guess checking", "Still has *");
						// saving the information and continue playing
						setWordViews(word, wordsTried, guessAllowed);

					} else { // got all letters correct
						Log.i("Guess checking", "Got the word right");
						showNextWordPromptDialog("YEAHHHSSSS", "You got the word right!");
					}
				}

				// guess is wrong
				else if (currPart < numParts) { // but still has guessAllowed
												// left
					Log.i("Guess checking",
							"Guess was wrong but still have a chance");
					bodyParts[currPart].setVisibility(View.VISIBLE);
					currPart++;
					setWordViews(word, wordsTried, guessAllowed);
					
					if (currPart == numParts) { // maximum guess reached
						Log.i("Guess checking", "No more guess allowed");
						if (wordsTried.equals("80")) { // wordsTried is 80
							Log.i("Guess checking", "That was a 80th word");
							disableBtns();
							// show result
							new GetTestResults(new AfterGetTestResults())
									.execute();

						} else { // No more guess allowed for this word
							Log.i("Guess checking",
									"No more guess for this word, have to get a new word");
							disableBtns();
							// Display Alert Dialog
							showNextWordPromptDialog("OOPS",
									"You ran out of guess allowed.");

						}
					}

				}

			}

		}

	}

	private class AfterGetTestResults implements
			AsyncTaskCompleteListener<String> {

		public void onTaskComplete(String returnValue) {
			if ("Error".equals(returnValue)) {
				// error checking
			} else {
				// Display Alert Dialog
				AlertDialog.Builder loseBuild = new AlertDialog.Builder(
						GameActivity.this);
				loseBuild.setTitle("Results");
				loseBuild.setMessage(returnValue);
				loseBuild.setPositiveButton("Submit the Result",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								new SubmitTestResults(
										new AfterSubmitTestResults()).execute();
							}
						});

				loseBuild.setNegativeButton("Exit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								GameActivity.this.finish();
							}
						});

				loseBuild.create().show();
			}
		}

		@Override
		public void onTaskComplete(String result, String result2, String result3) {

		}
	}

	private class AfterSubmitTestResults implements
			AsyncTaskCompleteListener<String> {

		public void onTaskComplete(String returnValue) {
			Log.i("AfterSubmitTestResults", "So just send it to email");
			// send the result to my email.
			sendEmail("subin.c.park@gmail.com", returnValue);
		}

		@Override
		public void onTaskComplete(String result, String result2, String result3) {

		}
	}
	
	
	
	
	
	
	/////////////////////Methos/////////////////

	private void setPlatform() {
		// reset adapter
		ltrAdapt = new LetterAdapter(this);
		letters.setAdapter(ltrAdapt);

		// start part at zero
		currPart = 0;

		// hide all parts
		for (int p = 0; p < numParts; p++) {
			bodyParts[p].setVisibility(View.INVISIBLE);
		}
	}
	
	public void setWordViews(String word, String wordsTried, String guessAllowed) {
		currWord = word;				
		setWord(word);
		wordTriedView.setText("Number of words you have tried: "
								+ wordsTried);
		guessAllowedView.setText("Remaining number allowed for guess: "
								+ guessAllowed);
	}
	
	private void setWord(String wordToSet) {

		textView = new TextView(this);

		// set the current text
		textView.setText(wordToSet);

		// set layout
		textView.setTextColor(0xFF6960EC);
		textView.setTextSize(30);
		textView.setBackgroundResource(R.drawable.letter_bg);

		// remove any existing letters
		textLayout.removeAllViews();
		
		textLayout.addView(textView);
	}
	
	
	public void sendEmail(String email, String content) {
		// send the result to my email.
		Intent sendEmail = new Intent(Intent.ACTION_SEND);
		sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Result from Hangman Game");
		sendEmail.putExtra(Intent.EXTRA_TEXT, content);
		sendEmail.setType("message/rfc822");
		startActivity(Intent.createChooser(sendEmail,
				"Choose an Email client :"));
	}

	public void showNextWordPromptDialog(String title, String message) {
		AlertDialog.Builder loseBuild = new AlertDialog.Builder(
				GameActivity.this);
		loseBuild.setTitle(title);
		loseBuild.setMessage(message);
		loseBuild.setPositiveButton("Next Word",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						GameActivity.this.onClick(buttonSkip);
					}
				});

		loseBuild.setNegativeButton("Exit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						GameActivity.this.finish();
					}
				});

		loseBuild.create().show();
	}

}
