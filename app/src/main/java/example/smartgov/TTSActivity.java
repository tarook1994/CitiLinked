package example.smartgov;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

public class TTSActivity extends AppCompatActivity {
    TextView text;
    EditText message;
    Button speak;
    StreamPlayer streamPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);
        text = (TextView) findViewById(R.id.textview);
        message = (EditText) findViewById(R.id.editText);
        speak = (Button) findViewById(R.id.button);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("the text to speech: " + text.getText());
                text.setText("TTS: " + message.getText());

                WatsonTask task = new WatsonTask();
                task.execute(message.getText().toString());
            }
        });
    }

    private TextToSpeech initTextToSpeechService(){
        TextToSpeech service = new TextToSpeech();
        String username = "35f1d684-b78d-464e-a8a2-cf6cb6df7b86";
        String password = "kFDwjKbo2dR6";
        service.setUsernameAndPassword(username, password);
        return service;
    }

    private class WatsonTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... textToSpeak) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setText("running the Watson thread");
                }
            });

          final  TextToSpeech textToSpeech = initTextToSpeechService();
            streamPlayer = new StreamPlayer();
            streamPlayer.playStream(textToSpeech.synthesize(String.valueOf(textToSpeak[0]), Voice.EN_MICHAEL).execute());
            return "text to speech done";
        }

        @Override
        protected void onPostExecute(String result) {
            text.setText("TTS status: " + result);
        }

    }
}



