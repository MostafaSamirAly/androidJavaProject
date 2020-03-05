package com.example.mishwary.ui.signup;

import com.example.mishwary.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupPresenter implements SignUpContract.SignupPresenter{
    SignUpContract.SignupView ref;
    public SignupPresenter(SignUpContract.SignupView ref) {
        this.ref = ref;
    }

    @Override
    public void addUser(User user) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        DatabaseReference reference = firebaseDatabase.getReference("user");
        String id = reference.push().getKey();
        user.setId(id);
        reference.child(id).setValue(user);
        ref.goToLogin(user);
    }
}
