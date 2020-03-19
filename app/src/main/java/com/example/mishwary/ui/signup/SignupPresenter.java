package com.example.mishwary.ui.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mishwary.Models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupPresenter implements SignUpContract.SignupPresenter{
    SignUpContract.SignupView ref;
    boolean flag = false;
    DatabaseReference reference;
    public SignupPresenter(SignUpContract.SignupView ref) {
        this.ref = ref;
    }

    @Override
    public void addUser(User user) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("user");
        String id = reference.push().getKey();
        user.setId(id);
        reference.child(id).setValue(user);
        reference = null;
        ref.goToLogin(user);
    }

    @Override
    public void checkMail(final User user) {
        reference = FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    User retrieved = dataSnapshot1.getValue(User.class);
                    if (user.getEmail().equalsIgnoreCase(retrieved.getEmail())){
                        flag = true;
                        break;
                    }
                }
                if (flag){
                    ref.showError();
                }else {
                    ref.registerUser(user);
                }
                reference = null;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
