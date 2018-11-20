package com.fl.dlc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fl.dlc.R;
import com.fl.dlc.util.DLConstants;

public class DLSplashScreenActivity extends Activity {

    //Splash Screen Timeout
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlsplash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                //Start Main Activity once timer is over
                Intent intent = new Intent(DLSplashScreenActivity.this, DLMainActivity.class);
                intent.putExtra(DLConstants.CLEAR_SUSPENSIONS, true);
                startActivity(intent);

                //Close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
