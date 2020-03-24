package com.iti.mishwary.ui.splash;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.iti.mishwary.R;
import com.iti.mishwary.ui.login.login;


public class splash extends Activity {
    private ProgressBar progressBar;
    protected boolean _active = true;
    protected int _splashTime = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                            progressBar.setProgress( waited/30 );
                        }
                    }
                } catch (Exception e) {

                } finally {
                    startActivity(new Intent(splash.this, login.class));
                    finish();
                }
            };
        };
        splashTread.start();
    }
}