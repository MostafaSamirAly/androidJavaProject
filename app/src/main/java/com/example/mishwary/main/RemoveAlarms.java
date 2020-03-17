package com.example.mishwary.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.mishwary.Models.Trip;
import com.example.mishwary.ui.addactivity.AlertReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RemoveAlarms {
    private Context context;
    private String userId;
    List<Trip> upcomingTrips;

    public RemoveAlarms(Context context, String userId) {
        this.context = context;
        this.userId = userId;
        getUpcomingTrips();

    }

    public void getUpcomingTrips() {
        DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference("upcoming_trip").child(userId);
        firebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {
                    for (DataSnapshot trip : dataSnapshot.getChildren()) {
                        Trip upcoming = trip.getValue(Trip.class);
                        cancelAlarm(upcoming);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cancelAlarm(Trip trip) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.getId().hashCode(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
