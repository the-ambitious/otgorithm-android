package com.ambit.otgorithm.dto;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.ambit.otgorithm.R;

public class Notification {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotifyBuilder;

    public Notification(Context context) {
        this.mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyBuilder = new NotificationCompat.Builder(mContext);
        mNotifyBuilder.setVibrate(new long[]{1000,1000});
        mNotifyBuilder.setPriority(100);
        mNotifyBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mNotifyBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

    }

    public Notification setTitle(String title){
        mNotifyBuilder.setContentTitle(title);
        mNotifyBuilder.setTicker(title);
        return this;
    }

    public Notification setText(String text){
        mNotifyBuilder.setContentText(text);
        return this;
    }

    public Notification setData(Intent intent) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(mContext);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent
                = taskStackBuilder.getPendingIntent(140, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setContentIntent(pendingIntent);
        return this;
    }

    public void notification() {
        try {
            mNotificationManager.notify(1, mNotifyBuilder.build());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
