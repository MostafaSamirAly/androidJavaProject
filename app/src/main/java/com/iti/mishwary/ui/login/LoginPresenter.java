package com.iti.mishwary.ui.login;

import androidx.annotation.NonNull;

import com.iti.mishwary.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter implements LoginContract.LoginPresenter {
    LoginContract.LoginView ref;

    public LoginPresenter(LoginContract.LoginView ref) {
        this.ref = ref;
    }

    @Override
    public void validateAccount(String email, final String password) {
        Query query = FirebaseDatabase.getInstance().getReference("user")
                        .orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userData : dataSnapshot.getChildren()){
                    User user = userData.getValue(User.class);
                    if(user.getPassword().equals(password)) {

                        ref.SaveUser(user);
                        ref.goToHome(user);

                    }else {
                        ref.showError();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
