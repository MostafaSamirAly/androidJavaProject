package com.iti.mishwary.ui.login;

import com.iti.mishwary.Models.User;

public interface LoginContract {
    public interface LoginView{
        void goToHome(User user);
        void showError();
        void SaveUser(User user);
    }
    public interface LoginPresenter{
       void validateAccount(String email ,String password);

    }
}
