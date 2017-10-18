package com.example.bryjohseb.jsonread;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BryJohSeb on 5/2/2017.
 */

public class CancelNotification extends BroadcastReceiver{
    private int id;

    @Override
    public void onReceive(Context context, Intent intent) {
        id = intent.getIntExtra("notification",1);
        NotificationManager notmngr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notmngr.cancel(id);
    }
}
