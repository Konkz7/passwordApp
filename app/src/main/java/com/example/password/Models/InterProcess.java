package com.example.password.Models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.password.R;

public class InterProcess extends Service {

    private final IBinder binder = new MyBinder();

    private static final String CHANNEL_ID = "PasswordChannel";
    public InterProcess() { }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();/*
        db = MainDatabase.getDatabase(getApplicationContext());
        SharedPreferences preferences =  getApplicationContext().getSharedPreferences("lastLogged", MODE_PRIVATE);
        lastUID = preferences.getLong("UID",-1);


        if(lastUID != -1){
            db.initRepo(lastUID);
            expo = new ExpiryModel(db.passwordRepo);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Listening for Events")
                .setContentText("Service is active")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .build();
        startForeground(1, notification);

         */

    }

    private void startRandomNotificationWorker() {
        androidx.work.OneTimeWorkRequest workRequest =
                new androidx.work.OneTimeWorkRequest.Builder(NotificationWorker.class).build();
        androidx.work.WorkManager.getInstance(this).enqueue(workRequest);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Listening for Events")
                .setContentText("Service is active")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .build();
        startForeground(1, notification);


      //  if(lastUID != -1) {
            startRandomNotificationWorker();
            //Log.d("lastUID",lastUID+"h");
        //}

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // Handle application closing


        // Destroy the service
        stopSelf();
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
