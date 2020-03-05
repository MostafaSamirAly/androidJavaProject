package com.example.mishwary.ui.signup;

import com.example.mishwary.Models.User;

public interface SignUpContract  {
    public interface SignupView{
        void goToLogin(User user);
    }
    public interface SignupPresenter{
        void addUser(User user);
    }
}
