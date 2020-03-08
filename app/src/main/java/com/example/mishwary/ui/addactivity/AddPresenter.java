package com.example.mishwary.ui.addactivity;

import com.example.mishwary.Models.Trip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPresenter implements AddContract.AddPresenter
{
    private AddContract.AddView addView;

    public AddPresenter(AddContract.AddView addView) {
        this.addView = addView;
    }

    @Override
    public void addTrip(Trip trip) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        DatabaseReference databaseReference = firebaseDatabase.getReference("upcoming_trip").child(trip.getUserId());
        trip.setId(databaseReference.push().getKey());
        databaseReference.child(trip.getId()).setValue(trip);
        addView.gotoHome();
    }

    @Override
    public void stop() {
        addView = null;
    }
}
