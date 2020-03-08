package com.example.mishwary.ui.login;

import com.example.mishwary.Models.User;

public interface LoginContract {
    public interface LoginView{
        void goToHome(User user);
        void showError();
    }
    public interface LoginPresenter{
        void validateAccount(String email ,String password);

    }
}
