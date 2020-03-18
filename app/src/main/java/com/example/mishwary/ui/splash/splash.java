package com.example.mishwary.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.mishwary.R;
import com.example.mishwary.ui.login.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class splash extends Activity {
    private ProgressBar progressBar;
    protected boolean _active = true;
    protected int _splashTime = 3000; // time to display the splash screen in ms
    // 85:52:35:4C:04:D6:1B:43:0B:4F:80:7D:80:1A:84:D5:1B:65:16:32



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


      /* try {
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
            //JjXvqH69JnMao9mJF48J/645wSY=
            Log.d("KeyHash:", e.toString());

     //hVI1TATWG0MLT4B9gBqE1RtlFjI=
        }
        //
        catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());


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