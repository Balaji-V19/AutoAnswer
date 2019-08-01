package com.example.autoanswer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn1,request;
    private EditText sec;
    private AutoCompleteTextView edit;
    private static final String[] data=new String[]{"Hello I'm busy now","you reached my phone","Call willbe recorded",
    "I'm not avilable right now"};
    private sms_message sms_message=new sms_message();

    private static final int RESULT_CODE=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request=findViewById(R.id.request);
        btn1=findViewById(R.id.btn1);
        sec=findViewById(R.id.sec);
        edit=findViewById(R.id.edit);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        edit.setAdapter(adapter);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sms_message.setMessage(edit.getText().toString());
                sms_message.setSec(Integer.parseInt(sec.getText().toString()));
                Intent i=new Intent(MainActivity.this,Secondpage.class);
                startActivity(i);
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckPermission())
                {
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    requestPermission();
                }
            }
        });
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ANSWER_PHONE_CALLS,Manifest.permission.CALL_PRIVILEGED,
                        Manifest.permission.CAPTURE_AUDIO_OUTPUT,Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS},
                RESULT_CODE);
    }

    private boolean CheckPermission() {
        int SEND_SMS= ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS);
        int READ_CALL_LOG=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG);
        int CAPTURE_AUDIO=ContextCompat.checkSelfPermission(this,Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        int MODIFY_AUDIO=ContextCompat.checkSelfPermission(this,Manifest.permission.MODIFY_AUDIO_SETTINGS);
        int CALL_PREVELEGED=ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PRIVILEGED);
        int READ_PHONE_STATE=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);
        int Answer_PHONE_CALL=ContextCompat.checkSelfPermission(this,Manifest.permission.ANSWER_PHONE_CALLS);
        int WRITE_External= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int AUDIO_Record=ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return WRITE_External== PackageManager.PERMISSION_GRANTED && SEND_SMS==PackageManager.PERMISSION_GRANTED &&
                AUDIO_Record==PackageManager.PERMISSION_GRANTED
                && READ_PHONE_STATE==PackageManager.PERMISSION_GRANTED && READ_CALL_LOG==PackageManager.PERMISSION_GRANTED &&
                MODIFY_AUDIO==PackageManager.PERMISSION_GRANTED && CAPTURE_AUDIO==PackageManager.PERMISSION_GRANTED &&
                Answer_PHONE_CALL==PackageManager.PERMISSION_GRANTED && CALL_PREVELEGED==PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==RESULT_CODE)
        {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
