package com.veloeye.activity;

import android.os.Bundle;
import android.os.Handler;

import com.karumi.dexter.Dexter;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.veloeye.R;

/**
 * Created by Florin on 11.09.2015.
 */
public class SplashActivity extends AppActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Dexter.initialize();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(LoginActivity.class);
                finish();
            }
        }, 1500);
    }
}
