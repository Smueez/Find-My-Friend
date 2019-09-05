package com.example.findmyfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Animation animation_ToptoDown;
    ImageView imageView_logo;
    TextView textView_AppName;
    FirebaseAuth Auth;
    FirebaseUser user;
    ProgressBar progressBar;
    String TAG = "start page";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        animation_ToptoDown = AnimationUtils.loadAnimation(this,R.anim.toptodown);
        imageView_logo = findViewById(R.id.imageView);
        imageView_logo.setAnimation(animation_ToptoDown);
        textView_AppName = findViewById(R.id.textView);
        final Intent intent1 = new Intent(this, MapsActivity.class);
        final Intent intent2 = new Intent(this,Login_Activity.class);
        textView_AppName.animate().alpha(1).setDuration(2000);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onTick: progressbar");
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: finished!");
                if(Auth.getCurrentUser() == null){
                    //progressBar.setVisibility(View.GONE);
                   startActivity(intent2);                          //if no logged in then go to log in
                }
                else {
                   // progressBar.setVisibility(View.GONE);
                    startActivity(intent1);                          //if logged in go to map
                }
                //startActivity(intent2);
            }
        }.start();
        Locale locale = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);
        locale.getCountry();
        Log.d(TAG, "onCreate: "+locale.getCountry());
        Log.d(TAG, "onCreate: "+locale.getDisplayCountry());
    }
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        //Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();

    }
}
