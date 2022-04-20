package com.edss.hitsales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.edss.hitsales.Utils.ConnectionDetector;

public class WelcomeActivity extends AppCompatActivity {


    private static final int SPLASH_TIME_OUT = 3000;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    ConnectionDetector cd;
    String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        cd = new ConnectionDetector(this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        userId = loginPreferences.getString("ID", "");
        Log.e("***","------ Login id ="+userId);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                try {

//                    if(userId.length() > 0){
//                        //UserId present in session
//                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }else{
                    //UserId not present in session
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
//                    }


                }catch (Exception e){e.printStackTrace();
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();}

            }
        }, SPLASH_TIME_OUT);
    }
}