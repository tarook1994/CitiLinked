package example.smartgov;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class ServicesActivity extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;
    GridView gridView;
    Context context;
    ArrayList arrayList;

    public static String[] gridViewStrings = {
            "Digital ID",
            "Digital License",
            "Digital Passport",
            "Scan QR",
            "Governmental Services",
            "Speech to Text",
            "Help",
            "Logout"
    };

    public static int[] gridViewImages = {
            R.drawable.id,
            R.drawable.lic,
            R.drawable.passp,
            R.drawable.scan,
            R.drawable.pr,
            R.drawable.mic,
            R.drawable.help ,
            R.drawable.exit
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new CustomAndroidGridViewAdapter(this, gridViewStrings, gridViewImages));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent i = new Intent(ServicesActivity.this,IDActivity.class);
                    i.putExtra("ID","1234543356");
                    startActivity(i);
                } else {
                    if (position == 1 ){
                        Intent i = new Intent(ServicesActivity.this,IDActivity.class);
                        i.putExtra("ID","1234543356");
                        startActivity(i);
                    } else {
                        if( position == 2) {
                            Intent i = new Intent(ServicesActivity.this,IDActivity.class);
                            i.putExtra("ID","1234543356");
                            startActivity(i);
                        } else {
                            if(position == 3){
                                Intent i = new Intent(ServicesActivity.this,QRScanActivity.class);
                                startActivityForResult(i,1);
                            } else {
                                if(position == 4) {
                                    Intent i = new Intent(ServicesActivity.this,ConversationActivity.class);
                                    startActivity(i);
                                } else {
                                    if(position == 5){
                                        Intent i = new Intent(ServicesActivity.this,SpeechTest.class);
                                        startActivity(i);
                                    } else {
                                        if(position == 6){

                                        } else {
                                            finish();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String stredittext=data.getStringExtra("scanningURL");
                Toast.makeText(ServicesActivity.this, stredittext, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ServicesActivity.this,ShowUserIDActivity.class);
                i.putExtra("ID",stredittext);
                startActivity(i);

            }
        }
    }
    private void initInstances() {
        rootLayoutAndroid = (CoordinatorLayout) findViewById(R.id.android_coordinator_layout);
        collapsingToolbarLayoutAndroid = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_android_layout);
        collapsingToolbarLayoutAndroid.setTitle("Material Grid");
    }
}
