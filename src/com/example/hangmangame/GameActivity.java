package com.example.hangmangame;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.constant.Action;
import com.example.constant.BuildConfig;
import com.example.server.comm.HttpRequest;
import com.example.preference.PreferenceManager;

@SuppressLint("NewApi")
public class GameActivity extends Activity implements OnClickListener {

	// the words
	private String currWord;
	// the layout holding the answer
	private LinearLayout textLayout;
	// text views for each letter in the answer
	private TextView textView;
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
	// num chars in word
	private int numChars;
	// num correct so far
	private int numCorr;

	// Next button
	private Button buttonNext;
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

		textLayout = (LinearLayout) findViewById(R.id.word);

		// set home as up
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Log.e("SecretInGameActivity", BuildConfig.SECRET);

		setPlatform();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		// set word length and correct choices
		// numChars = currWord.length();
		numCorr = 0;

		// hide all parts
		for (int p = 0; p < numParts; p++) {
			bodyParts[p].setVisibility(View.INVISIBLE);
		}
	}
	
	
	
/**
	// letter pressed method
		public void letterPressed(View view) {
			// find out which letter was pressed
			String ltr = ((TextView) view).getText().toString();
			char letterChar = ltr.charAt(0);
			// disable view
			view.setEnabled(false);
			view.setBackgroundResource(R.drawable.letter_down);
			
			
			
			// check if correct
			new HttpRequest().onPreExecute(getApplicationContext());
			try {
				String response = new HttpRequest().execute(Action.GUESS).get();
				Log.i("http response in game", response);

				// splitting the string to find secret
				Helpers splitString = new Helpers();
				String returnedValue = splitString.findValueToKey(response,
						"word");

				// saving the information
				BuildConfig.WORD = returnedValue;

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			boolean correct = false;
			for (int k = 0; k < currWord.length(); k++) {
				if (currWord.charAt(k) == letterChar) {
					correct = true;
					numCorr++;
					charViews[k].setTextColor(Color.BLACK);
				}
			}
			
			
			
			
			// check in case won
			if (correct) {
				if (numCorr == numChars) {
					// disable all buttons
					disableBtns();
					// let user know they have won, ask if they want to play again
					AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
					winBuild.setTitle("YAY");
					winBuild.setMessage("You win!\n\nThe answer was:\n\n"
							+ currWord);
					winBuild.setPositiveButton("Play Again",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									GameActivity.this.playGame();
								}
							});
					winBuild.setNegativeButton("Exit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									GameActivity.this.finish();
								}
							});
					winBuild.show();
				}
			}
			
			
			
			// check if user still has guesses
			else if (currPart < numParts) {
				// show next part
				bodyParts[currPart].setVisibility(View.VISIBLE);
				currPart++;
			} else {
				// user has lost
				disableBtns();
				// let the user know they lost, ask if they want to play again
				AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
				loseBuild.setTitle("OOPS");
				loseBuild.setMessage("You lose!\n\nThe answer was:\n\n" + currWord);
				loseBuild.setPositiveButton("Play Again",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								GameActivity.this.playGame();
							}
						});
				loseBuild.setNegativeButton("Exit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								GameActivity.this.finish();
							}
						});
				loseBuild.show();
			}
		}
		
		**/
		
		
	// disable letter buttons
	public void disableBtns() {
		int numLetters = letters.getChildCount();
		for (int l = 0; l < numLetters; l++) {
			letters.getChildAt(l).setEnabled(false);
		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.next) {
			Log.i("clicked?", "yes");
			new HttpRequest().onPreExecute(getApplicationContext());
			try {
				String response = new HttpRequest().execute(Action.NEXT).get();
				Log.i("http response in game", response);

				// splitting the string to find secret
				Helpers splitString = new Helpers();
				String returnedValue = splitString.findValueToKey(response,
						"word");

				// saving the information
				BuildConfig.WORD = returnedValue;

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			currWord = BuildConfig.WORD;
			//mPreferenceManager.setWord(getApplicationContext(), currWord);
			setWord();
		}
	}

}
