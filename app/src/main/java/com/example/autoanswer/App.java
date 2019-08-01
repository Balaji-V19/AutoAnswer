package com.example.autoanswer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String Channel_1="channel1";

    @Override
    public void onCreate() {
        super.onCreate();

        createnotification();
    }

    private void createnotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(Channel_1,"Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is for missed call");

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
