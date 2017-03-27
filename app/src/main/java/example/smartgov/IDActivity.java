package example.smartgov;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class IDActivity extends AppCompatActivity {

    ImageView qrCodeImageview;
    String QRcode;
    Thread splashTread;
    TextView text;
    public final static int WIDTH=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
        StartAnimations();
        text = (TextView) findViewById(R.id.QRText);
        getID();

// create thread to avoid ANR Exception
        Thread t = new Thread(new Runnable() {
            public void run() {
// this is the msg which will be encode in QRcode
                QRcode= getIntent().getStringExtra("ID");



                try {
                    synchronized (this) {
                        wait(5000);
// runOnUiThread method used to do UI task in main thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = null;

                                    bitmap = encodeAsBitmap(QRcode);
                                    text.setVisibility(View.VISIBLE);
                                    qrCodeImageview.setVisibility(View.VISIBLE);
                                    qrCodeImageview.setImageBitmap(bitmap);

                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } // end of catch block

                            } // end of run method
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        });
        t.start();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout mainLinearLayout=(LinearLayout) findViewById(R.id.lll_main);
        mainLinearLayout.clearAnimation();
        mainLinearLayout.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (true) {
                        sleep(100);
                        waited += 100;
                    }

                    //    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    //    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();

    }
    private void getID() {
        qrCodeImageview=(ImageView) findViewById(R.id.img_qr_code_image);
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    } /// end of this method
}
