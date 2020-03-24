package com.iti.mishwary.ui.addactivity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.iti.mishwary.Models.Trip;
import com.iti.mishwary.R;
import com.iti.mishwary.ui.floatingwidget.FloatingWidgetService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAlertDialog extends Activity {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    public static final int NOTIFICATION_ID = 1;
    Ringtone ringtone;
    String userId, tripId;
    Trip mTrip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        Intent intent = getIntent();
        tripId = intent.getStringExtra("tripId");
        userId = intent.getStringExtra("userId");
        getTrip();
        ringtone = RingtoneManager.getRingtone(MyAlertDialog.this, alarmUri);
        ringtone.play();
    }


    public void showDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyAlertDialog.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage("Reminder for your trip \"" + mTrip.getTripName() + "\" !!!!");
        builder.setCancelable(false);
        builder.setTitle("MishWary!");

        builder.setPositiveButton("START", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeFromUpcoming();
                addToHistory();
                //floating icon
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(context)) {
                        startFloatingWidgetService();
                    }
                }
                // open google maps with start and destination provided with the path
                if(mTrip.getStartPoint().equals("At Start Location")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+"&daddr="+mTrip.getDestination()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+mTrip.getStartPoint()+"&daddr="+mTrip.getDestination()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                cancelAlarm();
                ringtone.stop();
                dialog.cancel();
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeFromUpcoming();
                addToHistory();
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                cancelAlarm();
                ringtone.stop();
                dialog.cancel();
                finish();
            }
        });

        builder.setNeutralButton("SNOOZE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showNotification(MyAlertDialog.this);
                ringtone.stop();
                dialog.cancel();
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    public void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mTrip.getId().hashCode(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public void showNotification(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mManager.createNotificationChannel(channel);
        }

        Intent notifyIntent = new Intent(this, MyAlertDialog.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntent.putExtra("tripId", tripId);
        notifyIntent.putExtra("userId", userId);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, mTrip.getId().hashCode(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID);
        builder.setContentTitle("MishWary!")
                .setContentText("Your Trip \"" + mTrip.getTripName() + "\" is not started yet.")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notifyPendingIntent)
                .setOngoing(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void startFloatingWidgetService() {
        Intent intent = new Intent(this, FloatingWidgetService.class);
        intent.putExtra("tripId", mTrip.getId());
        intent.putExtra("tripTitle", mTrip.getTripName());
        startService(intent);
    }

    private void getTrip() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("upcoming_trip").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Trip retrievedTrip = dataSnapshot1.getValue(Trip.class);
                    if (retrievedTrip.getId().equals(tripId)) {
                        mTrip = retrievedTrip;
                        showDialog(getApplicationContext());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addToHistory() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("history_trip").child(userId);
        mTrip.setId(tripId);
        databaseReference.child(tripId).setValue(mTrip);
    }

    private void removeFromUpcoming() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("upcoming_trip").child(userId);
        databaseReference.child(tripId).removeValue();
    }
}
