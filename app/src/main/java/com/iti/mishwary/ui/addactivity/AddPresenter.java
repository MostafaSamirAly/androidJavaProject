package com.iti.mishwary.ui.addactivity;

import com.iti.mishwary.Models.Trip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPresenter implements AddContract.AddPresenter
{
    private AddContract.AddView addView;
    private Trip rtnTrip;
    public AddPresenter(AddContract.AddView addView) {
        this.addView = addView;
    }

    @Override
    public void addTrip(Trip trip) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("upcoming_trip").child(trip.getUserId());
        String id = databaseReference.push().getKey();
        trip.setId(id);
        addView.setTripId(id);
        databaseReference.child(id).setValue(trip);
        addView.gotoHome();
    }

    @Override
    public void editTrip(Trip trip) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("upcoming_trip").child(trip.getUserId());
        databaseReference.child(trip.getId()).setValue(trip);
        addView.gotoHome();
    }


    @Override
    public void stop() {
        addView = null;
    }


}
