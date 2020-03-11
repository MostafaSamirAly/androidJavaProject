package com.example.mishwary.ui.addactivity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresFeature;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import com.example.mishwary.R;

public class MyAlertDialog extends Activity {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    public static  final  int NOTIFICATION_ID = 1;
    Ringtone ringtone;
    private AddContract.AddPresenter addPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        showDialog(getApplicationContext());
    }

    public void showDialog(Context context) {

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(MyAlertDialog.this, alarmUri);
        ringtone.play();

        AlertDialog.Builder builder = new AlertDialog.Builder(MyAlertDialog.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage("Reminder for your trip!!!!");
        builder.setCancelable(false);
        builder.setTitle("THEME_TRADITIONAL");

        builder.setPositiveButton("START", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                showNotification(getApplicationContext());
                finish();
            }
        });
        builder.create();
    }
    public void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api=Build.VERSION_CODES.JELLY_BEAN)

    public void showNotification(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mManager.createNotificationChannel(channel);

            Intent notifyIntent = new Intent(this, MyAlertDialog.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID);
            builder.setContentTitle("MishWary!")
                    .setContentText("Your Trip is not started yet.")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(notifyPendingIntent)
                    .setOngoing(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

}
