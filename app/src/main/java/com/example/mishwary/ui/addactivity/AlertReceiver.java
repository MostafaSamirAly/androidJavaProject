package com.example.mishwary.ui.addactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlertReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent toMyAlertDialog = new Intent(context, MyAlertDialog.class);
        toMyAlertDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(toMyAlertDialog);
    }
}
