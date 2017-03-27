package example.smartgov;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class Login_Activity extends AppCompatActivity {
    EditText email,pass;
    Button login,register;
    private int CAMERA_PERMISSION_CODE = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        if(!isCameraAllowed()){
            requestCameraPermission();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Activity.this,ServicesActivity.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Activity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        login.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(Login_Activity.this,SpeechTest.class);
                startActivity(i);
                return false;
            }
        });

        register.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(Login_Activity.this,QRScanActivity.class);
                startActivity(i);
                return false;
            }
        });

    }

    public void initializeViews(){
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.sign);
        register = (Button) findViewById(R.id.reg);
    }
    private void requestCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == CAMERA_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Camera Access Granted", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "You just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isCameraAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
}
