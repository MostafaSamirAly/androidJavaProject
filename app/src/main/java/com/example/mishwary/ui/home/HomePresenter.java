package com.example.mishwary.ui.home;

import androidx.annotation.NonNull;

import com.example.mishwary.Models.Trip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements  HomeContract.HomePresenter {
    private HomeContract.HomeView homeFragment;
    private DatabaseReference firebaseReference;
    private String id;
    public HomePresenter(HomeContract.HomeView ref, String id) {
        homeFragment = ref;
        this.id = id;
    }

    @Override
    public void getUpcomingTrips() {
        final List<Trip> upcomingTrips = new ArrayList<>();
        firebaseReference = FirebaseDatabase.getInstance().getReference("upcoming_trip").child(id);
        firebaseReference.keepSynced(true);
        firebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                upcomingTrips.clear();
                if (dataSnapshot.getChildren() != null) {
                    for (DataSnapshot trip : dataSnapshot.getChildren()) {
                        Trip upcoming = trip.getValue(Trip.class);
                        upcomingTrips.add(upcoming);
                    }
                }
                if (homeFragment!=null) {
                    if (upcomingTrips != null && upcomingTrips.size() > 0) {
                        homeFragment.displayTrips(upcomingTrips);
                    } else {
                        homeFragment.displayNoTrips();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void stop() { homeFragment = null; }
}
