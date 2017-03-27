package example.smartgov;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.io.InputStream;

public class SpeechTest extends AppCompatActivity {

    private Button buttonRecord;
    private TextView textView;
    private SpeechToText speechToTextService;
    private boolean listening = false;
    private InputStream inputStream;
    int RECORD_REQUEST_CODE =101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_test);

        buttonRecord = (Button) findViewById(R.id.button_record);
        textView = (TextView) findViewById(R.id.text_view);

        speechToTextService = initSpeechToTextService();

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecordAllowed()) {
                    if (!listening) {
                        textView.setText("Say Something");
                        startRecord();
                    } else {
                        stopRecord();
                    }
                } else {
                    makeRequest();
                }

            }
        });

//        buttonRecord.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    Toast.makeText(SpeechTest.this, "Speak Now", Toast.LENGTH_SHORT).show();
//                    textView.setText("Say Something");
//                    startRecord();
//                } else {
//                    if(event.getAction() == MotionEvent.ACTION_UP){
//                        stopRecord();
//                        Toast.makeText(SpeechTest.this, "Speak End", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                return true;
//            }
//        });
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RECORD_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == RECORD_REQUEST_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can use Speech to Text ", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isRecordAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
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
            buttonRecord.setText("Stop");
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
                                           Log.d("index",speechResults.getResultIndex()+"");
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
            buttonRecord.setText("Start");
            try {
                inputStream.close();
                listening = false;
                textView.setText("Press on Button");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void showError(final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SpeechTest.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
        }

        private void showResult(final String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(result);
                }
            });
        }
    }

