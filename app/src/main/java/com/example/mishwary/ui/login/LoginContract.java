package com.example.mishwary.ui.login;

public interface LoginContract {
    public interface LoginView{
        void goToHome();
        void showError();
    }
    public interface LoginPresenter{
        void validateAccount(String email ,String password);
    }
}
