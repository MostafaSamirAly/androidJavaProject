package com.iti.mishwary.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iti.mishwary.R;
import com.iti.mishwary.Models.User;


public class signup extends AppCompatActivity implements View.OnClickListener,SignUpContract.SignupView{

    EditText _nameText,_emailText,_passwordText, _reEnterPasswordText;
    SignUpContract.SignupPresenter signupPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        _nameText = findViewById(R.id.input_name);
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _reEnterPasswordText=findViewById(R.id.input_password2);
        findViewById(R.id.btn_signup).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void UserSignUp(){
        String name = _nameText.getText().toString().trim();
        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        String reEnterPassword = _reEnterPasswordText.getText().toString().trim();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            _nameText.requestFocus();
            return;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            _emailText.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            _passwordText.requestFocus();
            return;
        }
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            _reEnterPasswordText.requestFocus();
            return;
        }
        signupPresenter = new SignupPresenter(this);
        User addedUser = new User(null,name,email,password);
        signupPresenter.checkMail(addedUser);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:
                UserSignUp();
                break;

            case R.id.btn_login :
                // Finish the registration screen and return to the Login activity
                goToLogin(null);
                break;
        }
    }

    @Override
    public void goToLogin(User user) {
        Intent replyIntent = new Intent();
        if(user != null) {
            replyIntent.putExtra("email", user.getEmail());
            replyIntent.putExtra("pass", user.getPassword());
        }
        setResult(RESULT_OK, replyIntent);
        finish();

    }

    @Override
    public void showError() {
        Toast.makeText(this,"This Email Already Exist",Toast.LENGTH_LONG).show();
    }

    @Override
    public void registerUser(User user) {
        signupPresenter.addUser(user);
    }

}
