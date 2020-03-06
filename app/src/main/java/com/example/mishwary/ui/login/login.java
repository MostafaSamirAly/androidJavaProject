package com.example.mishwary.ui.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mishwary.MainActivity;
import com.example.mishwary.R;
import com.example.mishwary.ui.signup.signup;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class login extends Activity implements LoginContract.LoginView {

    EditText _emailText, _passwordText;
    Button _loginButton, _signupLink, _forgetPass;
    LoginButton FBloginbtn;
    String TAG ="login" ;
    private FirebaseAuth mAuth;
    LoginPresenter loginPresenter;
    CallbackManager  callbackManager;
    boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        _emailText = findViewById(R.id.user);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);
        _forgetPass = findViewById(R.id.forgot_password);
        FBloginbtn = findViewById(R.id.login_button);
        FBloginbtn.setReadPermissions(Arrays.asList("email","public_profile"));
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "facebook:onError",exception);
                    }
                });


             mAuth = FirebaseAuth.getInstance();


        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });


        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, signup.class));
            }
        });
        _forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(login.this, forgetPassword.class));
            }
        });

        Intent intent = getIntent();
        if(intent != null){
            _emailText.setText(intent.getStringExtra("email"));
            _passwordText.setText(intent.getStringExtra("pass"));
            login();
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            Intent intentp = new Intent(this, MainActivity.class);
            startActivity(intentp );

        }

        //accessTokenTracker.startTracking();
        // if the user is already login
       /* if (SharedPrefManger.getInstance(this).isLoggedIn()) {
            Intent intentp = new Intent(this, MainActivity.class);
            intentp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startActivity(intent);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
   /* AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null)
            {
                _emailText.setText("");
            }
            else
            {
                getUserProfile(currentAccessToken);
            }

        }
    };*/
   /* private void getUserProfile(AccessToken accessToken)
    {
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    System.out.println("********************************");
                    System.out.println(object);
                    System.out.println("********************************");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String name = object.getString("first_name");
                    _emailText.setText(email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","name,email,id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }*/
   // AccessToken accessToken = AccessToken.getCurrentAccessToken();
   // boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

    public void login() {

        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid Email");
            _emailText.requestFocus();
            return;
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            _passwordText.requestFocus();
            return;
        }

        loginPresenter = new LoginPresenter(this);
        loginPresenter.validateAccount(email,password);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(login.this, "user id"+user.getUid(),
                                    Toast.LENGTH_SHORT).show();
                            if(flag)
                            {
                                Intent intentp = new Intent(login.this, MainActivity.class);
                                flag = false;
                                startActivity(intentp );
                                finish();

                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    @Override
    public void goToHome() {
        Intent intentp = new Intent(this, MainActivity.class);
        startActivity(intentp );
        finish();
    }

    @Override
    public void showError() {
        Toast.makeText(this,"Invalid UserName or Password",Toast.LENGTH_LONG).show();
    }
}

