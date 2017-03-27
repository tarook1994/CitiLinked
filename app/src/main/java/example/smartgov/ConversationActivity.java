package example.smartgov;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConversationActivity extends AppCompatActivity {
    TextView res,user;
    Button send,rec;
    EditText message;
    int counter = 0;
    String resp;
    boolean isTextChanged = false;
    Map<String,Object> contextMap =new HashMap<String,Object>();
    private SpeechToText speechToTextService;
    private boolean listening = false;
    private InputStream inputStream;
    int RECORD_REQUEST_CODE =101;
    StreamPlayer streamPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        speechToTextService = initSpeechToTextService();
        res = (TextView) findViewById(R.id.response);
        user = (TextView) findViewById(R.id.user);
        send = (Button) findViewById(R.id.send);
        rec = (Button) findViewById(R.id.record);
        message = (EditText) findViewById(R.id.message);
         task test = new task();
        if(counter==0){
            test.execute("Hi");
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message.getText().toString().length() !=0 ){
                    String msg = message.getText().toString();
                    task test = new task();
                    String[] stateContextInput = new String[2];
                    stateContextInput[0] = msg;
                    String lastContext ="";
                    try {
                        JSONObject convResponse = new JSONObject(resp);
                        JSONObject context = convResponse.getJSONObject("context");
                        Log.d("Contextt",context.toString());
                        lastContext = context.toString();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    stateContextInput[0] = msg;
                    stateContextInput[1]= lastContext;
                    test.execute(stateContextInput);
                    user.setText("You are Saying : "+ msg);
                    message.setText("");
                }
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
    private SpeechToText initSpeechToTextService() {
        SpeechToText speechToTextService = new SpeechToText();
        String username ="0d71ee52-4910-43d7-8b7d-cf76c0360b39";
        String password = "yY4XVRSRtFl2";
        speechToTextService.setUsernameAndPassword(username, password);
        speechToTextService.setEndPoint("https://stream.watsonplatform.net/speech-to-text/api");
        return speechToTextService;
    }

    private RecognizeOptions getRecognizeOptions() {
        return new RecognizeOptions.Builder()
                .continuous(true)
                .contentType(ContentType.OPUS.toString())
                .model("en-US_BroadbandModel")
                .interimResults(true)
                .inactivityTimeout(2000).build();
    }

    private void startRecord() {
        //buttonRecord.setText("Stop");
        inputStream = new MicrophoneInputStream(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    speechToTextService.recognizeUsingWebSocket(inputStream, getRecognizeOptions(),
                            new BaseRecognizeCallback() {
                                @Override
                                public void onTranscription(SpeechResults speechResults) {
                                    try {
                                        String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                                        showResult(text);
                                    } catch (IndexOutOfBoundsException e) {
                                        showError(e);
                                    }
                                }

                                @Override
                                public void onError(Exception e) {
                                    showError(e);
                                }

                                @Override
                                public void onDisconnected() {
                                }
                            });
                } catch (Exception e) {
                    showError(e);
                }
            }
        }).start();
        listening = true;
    }

    private void stopRecord() {
       //buttonRecord.setText("Start");
        try {
            inputStream.close();
            listening = false;
           // textView.setText("Press on Button");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ConversationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void showResult(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                message.setText(result);
            }
        });
    }
    public class task extends AsyncTask<String,Void,Void>{
        MessageResponse taskResp;

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            resp = taskResp.toString();
            counter++;
            Log.d("The resp main thread",resp);
        }

        @Override
        protected Void doInBackground(String... params) {

            ConversationService service = new ConversationService(ConversationService.VERSION_DATE_2016_07_11);
            service.setUsernameAndPassword("d0b4d9f5-72b0-414d-8477-14b5c5706865", "QbQFhmDMMvxk");
//            MessageRequest newMessage = new MessageRequest.Builder().inputText("").build();
            MessageRequest newMessage;
            if(counter==0){
                newMessage = new MessageRequest.Builder().inputText("Hii").build();
            } else {

                newMessage = new MessageRequest.Builder().context(contextMap).inputText(params[0]).build();


            }
            MessageResponse response = service.message("a2986926-1c00-4e68-98d7-ec2cc34953f9", newMessage).execute();
            Log.d("responseContext",response.getContext()+"");
            final MessageResponse msg = response;
            contextMap = response.getContext();
            taskResp = msg;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WatsonTask task = new WatsonTask();
                    task.execute(msg.getText().get(0));
                    res.setText("Watson is Saying : "+msg.getText().get(0));


                }
            });

            TextToSpeech service2 = new TextToSpeech();
            service2.setUsernameAndPassword("35f1d684-b78d-464e-a8a2-cf6cb6df7b86", "kFDwjKbo2dR6");

            List<Voice> voices = service2.getVoices().execute();
           // System.out.println(voices);
            System.out.println(response);
            return null;
        }

    }
    private class WatsonTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... textToSpeak) {

            final  TextToSpeech textToSpeech = initTextToSpeechService();
            streamPlayer = new StreamPlayer();
            streamPlayer.playStream(textToSpeech.synthesize(String.valueOf(textToSpeak[0]), Voice.EN_MICHAEL).execute());
            return "text to speech done";
        }

    }
}
