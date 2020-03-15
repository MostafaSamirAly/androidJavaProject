package com.example.mishwary.ui.login;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mishwary.MainActivity;
import com.example.mishwary.Models.User;
import com.example.mishwary.R;
import com.example.mishwary.ui.signup.SignupPresenter;
import com.example.mishwary.ui.signup.signup;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;


public class login extends Activity implements LoginContract.LoginView {

    EditText _emailText, _passwordText;
    Button _loginButton, _signupLink, _forgetPass;
    LoginButton FBloginbtn;
    String TAG ="login" ;
    private FirebaseAuth mAuth;
    LoginPresenter loginPresenter;
    CallbackManager  callbackManager;
    public static final int SIGNUP_ACTIVITY_REQUEST_CODE=2;
    public static final int RC_SIGN_IN = 1;
    public static  GoogleSignInClient mGoogleSignInClient;
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;


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
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });


        FBloginbtn.setReadPermissions(Arrays.asList("email","public_profile"));
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
                Intent intent = new Intent(login.this,signup.class);
                startActivityForResult(intent,SIGNUP_ACTIVITY_REQUEST_CODE);
            }
        });
        _forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(login.this, forgetPassword.class));
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(currentUser!=null)
        {
            Log.i("login",currentUser.getEmail()+" 1 "+currentUser.getUid()+" 2"+currentUser.getDisplayName()+" 3 "+currentUser.getPhoneNumber());
            User FacebookUser = new User(currentUser.getUid(),currentUser.getDisplayName(),currentUser.getEmail(),null);
            goToHome(FacebookUser);

        }
        if( pref.getString("id", null) != null)
        {
            User loginUser = new User(pref.getString("id",null),pref.getString("name",null),pref.getString("email",null),null);
            goToHome(loginUser);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode== SIGNUP_ACTIVITY_REQUEST_CODE&& resultCode==RESULT_OK)
        {

            _emailText.setText(data.getStringExtra("email"));
            _passwordText.setText(data.getStringExtra("pass"));

        }
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);



        } catch (ApiException e) {

            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User GoogleUser = new User(user.getUid(),user.getDisplayName(),user.getEmail(),null);
                            goToHome(GoogleUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


   private void signIn() {
       Intent signInIntent = mGoogleSignInClient.getSignInIntent();
       startActivityForResult(signInIntent, RC_SIGN_IN);
   }

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
     public void SaveUser(User user)
     {
         editor.putString("id", user.getId());
         editor.putString("name",user.getName());
         editor.putString("email",user.getEmail());
         editor.commit();
     }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User FacebookUser = new User(user.getUid(),user.getDisplayName(),user.getEmail(),null);
                            goToHome(FacebookUser);


                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }


    @Override
    public void goToHome(User user) {
        Intent intentp = new Intent(this, MainActivity.class);
        intentp.putExtra("id",user.getId());
        intentp.putExtra("name",user.getName());
        intentp.putExtra("email",user.getEmail());

        startActivity(intentp );
        finish();
    }

    @Override
    public void showError() {
        Toast.makeText(this,"Invalid UserName or Password",Toast.LENGTH_LONG).show();
    }
}

