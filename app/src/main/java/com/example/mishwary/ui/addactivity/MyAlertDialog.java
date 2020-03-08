package com.example.mishwary.ui.addactivity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class MyAlertDialog extends DialogFragment {
    Ringtone ringtone;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(getContext(), alarmUri);
        ringtone.play();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        // Set a message/question for alert dialog
        builder.setMessage("Reminder for your trip!!!!");
        // Specify the dialog is not cancelable
        builder.setCancelable(true);
        // Set a title for alert dialog
        builder.setTitle("THEME_TRADITIONAL");

        // Set the positive/START button click listener
        builder.setPositiveButton("START", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click positive button

            }
        });

        // Set the negative/CANCEL button click listener
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click the negative button
                // addContractViewInterface.cancelAlarm();
                ringtone.stop();
                dialog.cancel();
            }
        });

        // Set the neutral/SNOOZE button click listener
        builder.setNeutralButton("SNOOZE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click the neutral button
                /*Intent intent = new Intent(getContext(), );
                startNotification(getContext(),intent);*/
            }
        });
        return builder.create();
    }
    public static  void startNotification(Context context, Intent intent) {

    }
}
