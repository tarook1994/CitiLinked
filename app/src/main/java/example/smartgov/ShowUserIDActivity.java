package example.smartgov;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppConfig;
import app.AppController;

public class ShowUserIDActivity extends AppCompatActivity {
    ImageView front,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_id);
        front = (ImageView) findViewById(R.id.front);
        back = (ImageView) findViewById(R.id.back);
        requestUserWithID();


    }

    public void requestUserWithID(){
        StringRequest charge = new StringRequest(Request.Method.GET,
                AppConfig.URL_GET_USER+"/"+getIntent().getStringExtra("ID"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Responseee", " res: " + response.toString());
                try {
                    JSONObject user = new JSONObject(response);
                    JSONObject imgs = user.getJSONObject("img");
                    Log.d("result",imgs.getString("f")+" "+ imgs.toString());
                    String frontImage = imgs.getString("f");
                    String backImage = imgs.getString("b");

                    Picasso
                            .with(getApplicationContext())
                            .load(frontImage)
                            .fit() // will explain later
                            .into(front);
                    Picasso
                            .with(getApplicationContext())
                            .load(backImage)
                            .fit() // will explain later
                            .into(back);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ShowUserIDActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowUserIDActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
         }) {
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
