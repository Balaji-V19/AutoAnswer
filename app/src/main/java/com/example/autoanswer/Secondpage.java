package com.example.autoanswer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Secondpage extends AppCompatActivity {
    private Button btn3,stop;
    private AudioManager audioManager;
    private int i=1;


    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondpage);
        btn3=findViewById(R.id.btn3);
        stop=findViewById(R.id.stop);


        notificationManagerCompat=NotificationManagerCompat.from(Secondpage.this);
        stop.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                PackageManager pm=Secondpage.this.getPackageManager();
                ComponentName com=new ComponentName(Secondpage.this,CallDurationReceiver.class);
                pm.setComponentEnabledSetting(com,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                Toast.makeText(Secondpage.this, "Service is Stoped ", Toast.LENGTH_SHORT).show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm=Secondpage.this.getPackageManager();
                ComponentName com=new ComponentName(Secondpage.this,CallDurationReceiver.class);
                pm.setComponentEnabledSetting(com,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                Toast.makeText(Secondpage.this, "Started" , Toast.LENGTH_SHORT).show();
            }
        });
    }
}
