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
/*
* package com.journaldev.singleton;

public class BillPughSingleton {

    private BillPughSingleton(){}

    private static class SingletonHelper{
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance(){
        return SingletonHelper.INSTANCE;
    }
}*/