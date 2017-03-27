package example.smartgov;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class QRScanActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    String scanningURL;
    private QRCodeReaderView mydecoderview;
    int counter =0;
    private int CAMERA_PERMISSION_CODE = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);



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

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if(counter==0){
            scanningURL=text;

            // Toast.makeText(getApplicationContext()," scanned text"+""+scanningURL,Toast.LENGTH_LONG).show();
            ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(500);
            Intent intent = new Intent();
            intent.putExtra("scanningURL",scanningURL);
            setResult(RESULT_OK, intent);
            counter++;
            finish();
        }
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
}
