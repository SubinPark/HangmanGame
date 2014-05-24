Hello. Thank you so much for your time reviewing my code.
My game has two activities that will be shown to users: GameActivity and MainActivity. Actual game-playing will be on GameActivity.
For sending http request, I implemented AsyncTask for each of five actions so that main UI thread is not disturbed by any request actions. Building parameters and sending a request is done in doInBackground(). Then onPostExecute() processes the response so that “After” methods that implement AsyncTaskCompleteListener can use the processed response to do required actions on main thread.

I organized my code by action events.

I also included buttons for Next Word(skipping) and to go back, and important information such as remaining number of allowed for guess and number of words have tried.

Thank you so much again for your time. I enjoyed making this game and truly learned a lot. Thank you for giving me the opportunity! :)