package com.iti.mishwary.Models;

import com.google.firebase.database.FirebaseDatabase;

public class FireBaseCreator {
    private static  FireBaseCreator INSTANCE = null;
    private FireBaseCreator() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    public static void setPresistence(){
        synchronized (FireBaseCreator.class) {
            if (INSTANCE == null) {
                INSTANCE = new FireBaseCreator();
            }
        }
    }
}
