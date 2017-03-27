package example.smartgov;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;

public class RegisterActivity extends AppCompatActivity {
    Button register,citizen,officer;
    EditText offEmail,offPass,offConfpass,offSecret,citIDnumber,citEmail,citPass,citConf;
    boolean isCitizen = true;
    LinearLayout citizenLayout,officerLayout;
    String email,pass,confpass,ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeViews();
        citizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCitizen){
                    isCitizen = true;
                    officerLayout.setVisibility(View.GONE);
                    citizenLayout.setVisibility(View.VISIBLE);
                    officer.setBackgroundResource(R.color.colorPrimary);
                    citizen.setBackgroundResource(R.color.colorPrimaryDark);

                }
            }
        });

        officer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCitizen){
                    isCitizen = false;
                    citizenLayout.setVisibility(View.GONE);
                    officerLayout.setVisibility(View.VISIBLE);
                    citizen.setBackgroundResource(R.color.colorPrimary);
                    officer.setBackgroundResource(R.color.colorPrimaryDark);

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isCitizen){
                    email = citEmail.getText().toString();
                    pass = citPass.getText().toString();
                    confpass = citConf.getText().toString();
                    ID = citIDnumber.getText().toString();

                } else {
                    email = offEmail.getText().toString();
                    pass = offPass.getText().toString();
                    confpass = offConfpass.getText().toString();
                    ID = offSecret.getText().toString();
                }
                Log.d("ID",ID);
                addUserToDb();
            }
        });



    }

    public void initializeViews(){
        citizenLayout = (LinearLayout) findViewById(R.id.citizenLayout);
        officerLayout = (LinearLayout) findViewById(R.id.officerLayout);
        register = (Button) findViewById(R.id.reg);
        citizen = (Button) findViewById(R.id.citizenbutton);
        officer = (Button) findViewById(R.id.offbutton);
        offEmail = (EditText) findViewById(R.id.ofemail);
        offPass = (EditText) findViewById(R.id.ofpass);
        offConfpass = (EditText) findViewById(R.id.ofpassconf);
        offSecret = (EditText) findViewById(R.id.secretcode);
        citIDnumber = (EditText) findViewById(R.id.IDnum);
        citEmail = (EditText) findViewById(R.id.remail);
        citPass = (EditText) findViewById(R.id.rpass);
        citConf = (EditText) findViewById(R.id.rpassconf);
    }

    public void addUserToDb(){
        StringRequest charge = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Responseee", " res: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("pass", pass);
                    params.put("id", ID);
                    params.put("type", "citizen");
                    params.put("killing", "Yes adn needed");
                    params.put("military", "needed");
                return params;
            }
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
