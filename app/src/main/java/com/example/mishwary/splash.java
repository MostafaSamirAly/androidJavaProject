package com.example.mishwary;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.mishwary.ui.login.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class splash extends Activity {
    private ProgressBar progressBar;
    protected boolean _active = true;
    protected int _splashTime = 3000; // time to display the splash screen in ms


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


     /*  try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            // T75Ykj3C83LZgScbTD7tqG1IQzc=
            //yT6KBfyKhcfob0Tua6a+6KzdQbE=

        }
        catch (NoSuchAlgorithmException e) {

        }*/

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