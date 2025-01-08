package com.example.password;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.password.Models.ExpiryModel;

public class InterProcess extends Service {

    private final IBinder binder = new MyBinder();
    private final ExpiryModel expo = new ExpiryModel();

    private static final String CHANNEL_ID = "PasswordChannel";

    public InterProcess() { }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Listening for Events")
                .setContentText("Service is active")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .build();
        startForeground(1, notification);

        //checkExpiryAndNotify();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Listening for Events")
                .setContentText("Service is active")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .build();
        startForeground(1, notification);
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Password Manager";
            String description = "Used for displaying expiry events";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendEventNotification(String eventDetails) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Event Detected")
                .setContentText(eventDetails)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(2, notification);
    }

    private void checkExpiryAndNotify() {
        expo.expiryFunction();
        if (expo.expiryCheck()) {
            //sendEventNotification("You have expired password(s)");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public InterProcess getService() {
            return InterProcess.this;
        }
    }
}
