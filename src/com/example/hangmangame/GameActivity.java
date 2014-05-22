package com.example.hangmangame;

import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.constant.Action;
import com.example.constant.BuildConfig;
import com.example.preference.PreferenceManager;
import com.example.server.comm.Connection;
import com.example.server.comm.HttpRequest;

@SuppressLint("NewApi")
public class GameActivity extends Activity implements OnClickListener {

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

		Log.e("SecretInGameActivity", BuildConfig.SECRET);

		setPlatform();
		
		boolean connection = Connection.isConnected(getApplicationContext());
		if(connection == false) { //not connected to the internet
			//TODO
			Log.e("CONNECTION", "Not connected to the internet");
			Toast.makeText(getApplicationContext(), "Not connected to the internet", Toast.LENGTH_LONG).show();
		}
	}

	private void setWord() {

		textView = new TextView(this);
		// set the current letter
		textView.setText(BuildConfig.WORD);

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
	public void letterPressed(View view) throws InterruptedException,
			ExecutionException {
		
		// find out which letter was pressed
		String ltr = ((TextView) view).getText().toString();

		// disable view
		view.setEnabled(false);
		view.setBackgroundResource(R.drawable.letter_down);

		// check the guess to the server
		try {
			String response = new HttpRequest().execute(Action.GUESS, ltr)
					.get();

			// splitting the string to find the word
			Helpers splitString = new Helpers();

			numberOfGuessAllowedForThisWord = splitString.findValueToKey(
					response, "numberOfGuessAllowedForThisWord");
			guessAllowedView.setText("Remaining number allowed for guess: "
					+ numberOfGuessAllowedForThisWord);

			numberOfWordsTried = splitString.findValueToKey(response,
					"numberOfWordsTried");
			wordTriedView.setText("Number of words you have tried: "
					+ numberOfWordsTried);

			String wordReturned = splitString.findValueToKey(response, "word");
			//TODO wordReturned could be "Error. Push the button more more time". Then this string gets recognized as correct! Because 

			if (!wordReturned.equals(currWord)) { // if the guess is correct // improved from the last word 저 번 단어랑 틀리지만 
				if (wordReturned.contains("*")) { // but still has *
					// saving the information and continue playing
					BuildConfig.WORD = wordReturned;
					currWord = BuildConfig.WORD;
					setWord();
				} else { // got the all letters correct
					// Display Alert Dialog
					AlertDialog.Builder loseBuild = new AlertDialog.Builder(
							this);
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

					loseBuild.show();
				}
			}

			else if (currPart < numParts) { // got it wrong but still have some
											// quesses left
				bodyParts[currPart].setVisibility(View.VISIBLE);
				currPart++;
				if (currPart == numParts) { // the number of guess allowed is
											// run over, prompt a dialog

					disableBtns();

					// Display Alert Dialog
					AlertDialog.Builder loseBuild = new AlertDialog.Builder(
							this);
					loseBuild.setTitle("OOPS");
					loseBuild.setMessage("You didn't get this word right");
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

					loseBuild.show();
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		if (numberOfWordsTried.equals("80")) { // reaches 80 words --> get test
												// results
			String result = new HttpRequest().execute(Action.GET_RESULT).get();
			// TODO I don't think this should be here..
			// present the result on the the app

		}

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

			pushToStart = false;
			try {
				// request to server and get the next word
				String response = new HttpRequest().execute(Action.NEXT).get();
				Log.i("http response in game", response);

				// splitting the string to find the word
				Helpers splitString = new Helpers();
				String returnedValue = splitString.findValueToKey(response,
						"word");

				// saving the information
				BuildConfig.WORD = returnedValue;

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			if (pushToStart == false) {
				buttonNext.setText("Skip the Word");
			}
			currWord = BuildConfig.WORD;
			// mPreferenceManager.setWord(getApplicationContext(), currWord);
			setWord();
		}
	}

}
