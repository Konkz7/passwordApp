package com.example.password.Models;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.password.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "PasswordChannel";
    private ExpiryModel expo;

    private Long lastUID;

    private MainDatabase db;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        db = MainDatabase.getDatabase(getApplicationContext());
        SharedPreferences preferences =  getApplicationContext().getSharedPreferences("lastLogged", MODE_PRIVATE);
        lastUID = preferences.getLong("UID",-1);


        if(lastUID != -1){
            db.initRepo(lastUID);
            expo = new ExpiryModel(db.passwordRepo);
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        // Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Password Manager";
            String description = "Used for displaying when passwords expire";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        // Build and display the notification
        if (lastUID != -1) {
           MainDatabase.databaseWriteExecutor.execute(()-> {
               checkExpiryAndNotify();
           });
        }

        // Schedule the next random notification
        scheduleNextNotification();

        return Result.success();
    }

    private void scheduleNextNotification() {
        Random random = new Random();
        long delay = random.nextInt(12) + 1; // Delay between 1 and 12 hours

        androidx.work.OneTimeWorkRequest workRequest = new androidx.work.OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(delay, TimeUnit.HOURS)
                .build();

        androidx.work.WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
    }

    private void sendEventNotification(String eventDetails) {
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Event Detected")
                .setContentText(eventDetails)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .build();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private void checkExpiryAndNotify() {
        expo.expiryFunction();
        if (expo.expiryCheck()) {
            sendEventNotification("You have expired password(s)");
        }
    }
}
