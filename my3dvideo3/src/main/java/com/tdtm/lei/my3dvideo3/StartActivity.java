package com.tdtm.lei.my3dvideo3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends Activity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mContext = this;
        startHomeActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startHomeActivity();
    }

    class StartHomeAcitivityTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, HomeActivity.class);

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//stuff that updates ui

                }
            });





        }

    }
    private void startHomeActivity()
    {
        /*
        Intent intent = new Intent(mContext, HomeActivity.class);

        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        */

        int time = 1000;
        Timer timer = new Timer();
        timer.schedule(new StartHomeAcitivityTask(),time);


    }
}
