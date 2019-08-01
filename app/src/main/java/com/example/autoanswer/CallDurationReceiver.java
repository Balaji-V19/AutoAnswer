package com.example.autoanswer;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class CallDurationReceiver extends BroadcastReceiver {

    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private sms_message sms_message=new sms_message();

    private int output_formats[] = {
            MediaRecorder.OutputFormat.THREE_GPP};
    private String file_exts[] = {
            AUDIO_RECORDER_FILE_EXT_3GP};



    private AudioManager audioManager;
    private MediaRecorder recorder;
    private int i=1;


    private NotificationManagerCompat notificationManagerCompat;





    @Override
    public void onReceive(final Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String num=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        try {
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {


                Toast.makeText(context, ""+num, Toast.LENGTH_SHORT).show();
                sendsms(num);



                Thread.sleep(sms_message.getSec()*1000);

                TelecomManager tm = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    tm = (TelecomManager) context
                            .getSystemService(Context.TELECOM_SERVICE);
                }


                if (tm == null) {
                    // whether you want to handle this is up to you really
                    throw new NullPointerException("tm == null");
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.
                            ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(context,Manifest.permission.ANSWER_PHONE_CALLS);
                        return;
                    }
                    tm.acceptRingingCall();
                }



                notificationManagerCompat=NotificationManagerCompat.from(context);
                Notification notification=new NotificationCompat.Builder(context,App.Channel_1)
                        .setSmallIcon(android.R.drawable.stat_notify_missed_call)
                        .setContentTitle("You have a call")
                        .setContentText("From "+num)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .build();
                notificationManagerCompat.notify(i,notification);
                i++;

            }
            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                audioManager = (AudioManager) context.getApplicationContext()
                        .getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.AUDIOFOCUS_GAIN);
                audioManager.setSpeakerphoneOn(true);
                Toast.makeText(context, "Attened", Toast.LENGTH_SHORT).show();





                //the above part is the play the audio file
                //the following is for recording the incoming call






                startrecording(num);
                try {
                    recorder.prepare();
                    recorder.start();
                } catch (IllegalStateException e) {
                    Log.e("REDORDING :: ",e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("REDORDING :: ",e.getMessage());
                    e.printStackTrace();
                }

            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {






                audioManager.setSpeakerphoneOn(false);
                audioManager.setMode(AudioManager.MODE_NORMAL);
                Toast.makeText(context, "Ended", Toast.LENGTH_SHORT).show();
                try{
                    if (recorder!=null) {
                        recorder.stop();
                        recorder.reset();
                        recorder.release();
                        recorder = null;
                    }
                }catch(RuntimeException stopException){
                    stopException.printStackTrace();

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendsms(String num) {
        SmsManager sms=SmsManager.getDefault();
        String mess=sms_message.getMessage();
        sms.sendTextMessage(num,null,""+mess,null,null);

    }

    private void startrecording(String num) {
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.M)
        {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(getFilename());
        }
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M){
            String filepath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(filepath, AUDIO_RECORDER_FOLDER);

            if (!file.exists()) {
                file.mkdirs();
            }
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            if (!num.isEmpty()){
                recorder.setOutputFile(new File(Environment.getExternalStorageDirectory().
                        getPath(),AUDIO_RECORDER_FOLDER).getAbsolutePath()
                        + "/" +num +"time" +System.currentTimeMillis() +file_exts[0]);
            }
            else {
                recorder.setOutputFile(new File(Environment.getExternalStorageDirectory().
                        getPath(),AUDIO_RECORDER_FOLDER).getAbsolutePath()
                        + "/"+num +"time" +System.currentTimeMillis()  +file_exts[0]);
            }
        }
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[0]);
    }
}