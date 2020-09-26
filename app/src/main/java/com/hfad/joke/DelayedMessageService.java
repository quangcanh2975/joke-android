package com.hfad.joke;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class DelayedMessageService extends IntentService {
    public static final String EXTRA_MESSAGE = "message";
    //    private Handler handler; // no longer need handler
    public static final int NOTIFICATION_ID = 4213;

    public DelayedMessageService() {
        super("DelayedMessageService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
//        handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (this) {
            try {
                wait(2000);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        String text = intent.getStringExtra(EXTRA_MESSAGE);
        showText(text);
    }

    private void showText(final String text) {
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String description = "This is my channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.v("DelayedMessageService", "The message is: " + text);
//                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
//            }
//        });
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getString(R.string.app_name)).setAutoCancel(true).setContentIntent(pendingIntent).setContentText(text).build();
        } else {
            notification = new Notification.Builder(this).setPriority(Notification.PRIORITY_MAX).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getString(R.string.app_name)).setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setContentText(text).build();
        }

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
