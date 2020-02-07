package com.example.womensaftey;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    private String CHANNEL_ID="LocationUpdates";
    private String CHANNEL_NAME="Location Updates";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel1=
                    new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            channel1.setDescription("This notification channel is important for location Update purpose");

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }

    }
}
