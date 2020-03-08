package com.example.mishwary.ui.login;

import android.app.DownloadManager;

import androidx.annotation.NonNull;

import com.example.mishwary.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter implements LoginContract.LoginPresenter {
    LoginContract.LoginView ref;

    public LoginPresenter(LoginContract.LoginView ref) {
        this.ref = ref;
    }

    @Override
    public void validateAccount(String email, String password) {
        Query query = FirebaseDatabase.getInstance().getReference("user")
                        .orderByChild("email").equalTo(email).orderByChild("password").equalTo(password);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    ref.goToHome(null);
                }else {
                    ref.showError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
