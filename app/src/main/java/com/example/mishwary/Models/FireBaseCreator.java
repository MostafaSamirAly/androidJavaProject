package com.example.mishwary.Models;

import com.google.firebase.database.FirebaseDatabase;

public class FireBaseCreator {
    private FireBaseCreator() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    private static class SingletonHelper{
        private static final FireBaseCreator INSTANCE = new FireBaseCreator();
    }

    public static FireBaseCreator getInstance(){
        return SingletonHelper.INSTANCE;
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