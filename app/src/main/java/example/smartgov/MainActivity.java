package example.smartgov;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeechService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MicrophoneInputStream capture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        testRequest();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private RecognizeOptions getRecognizeOptions() {
        return new RecognizeOptions.Builder()
                .continuous(true)
                .contentType(ContentType.OPUS.toString())
                .model("en-US_BroadbandModel")
                .interimResults(true)
                .inactivityTimeout(2000).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.DigitalID) {
            Intent i = new Intent(MainActivity.this,IDActivity.class);
            i.putExtra("ID","1234543356");
            startActivity(i);

        } else if (id == R.id.DigitalDrive) {
            Intent i = new Intent(MainActivity.this,TTSActivity.class);
            startActivity(i);

        } else if (id == R.id.Conversation) {
            Intent i = new Intent(MainActivity.this,ConversationActivity.class);
            startActivityForResult(i,1);

        } else if (id == R.id.scan) {
            Intent i = new Intent(MainActivity.this,QRScanActivity.class);
            startActivityForResult(i,1);

        } else if (id == R.id.NewsFeed) {
            Intent i = new Intent(MainActivity.this,ServicesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String stredittext=data.getStringExtra("scanningURL");
                Toast.makeText(MainActivity.this, stredittext, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,ShowUserIDActivity.class);
                i.putExtra("ID",stredittext);
                startActivity(i);

            }
        }
    }

    public void testRequest(){
        StringRequest charge = new StringRequest(Request.Method.GET,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Responseee", " res: " + response.toString());
                try {
                    JSONObject database = new JSONObject(response);
                    JSONObject users = database.getJSONObject("users");
                    Log.d("users",users.toString());
                    Log.d("Length",users.length()+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting parameters to login url
//                Map<String, String> params = new HashMap<String, String>();
//                Log.d("finalToken", "final: " + finalToken);
//                params.put("tokens", finalToken);
//                params.put("message", finalMessage);
//                return params;
//            }
        };
        String tag_string_req = "req_login";
        // Adding request to request queue
        if (charge == null) {
            Log.d("da b null", "null");
        } else {
            Log.d("msh null", "msh null");
            if (AppController.getInstance() == null) {
                Log.d("kda el app b ", "null");
            }
        }
        AppController.getInstance().addToRequestQueue(charge, tag_string_req);
    }


}
