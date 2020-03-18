package com.example.mishwary.Models;

import com.google.firebase.database.FirebaseDatabase;

public class FireBaseCreator {
    private static final FireBaseCreator INSTANCE = new FireBaseCreator();
    private FireBaseCreator() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    public static FireBaseCreator getInstance(){
        return INSTANCE;
    }
}
