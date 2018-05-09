package com.example.kpstar.sendcard;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSActivity extends AppCompatActivity {

    Button btnSendSMS;
    EditText txtPhoneNo;
    EditText txtMessage;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);

        SharedPreferences sharedPreferences = getSharedPreferences("Profile", Context.MODE_PRIVATE);
        txtMessage.setText(sharedPreferences.getString("message", ""));


        btnSendSMS.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String phoneNo = txtPhoneNo.getText().toString();
                String message = txtMessage.getText().toString();
                if (phoneNo.length()>0 && message.length()>0)
                    sendSMS(phoneNo, message);
                else
                    Toast.makeText(SMSActivity.this,
                            "Please enter both phone number and message.",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, SMSActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);
    }

//    PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
//            new Intent(SENT), 0);
//
//    PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
//            new Intent(DELIVERED), 0);
//
//    //---when the SMS has been sent---
//    registerReceiver(new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context arg0, Intent arg1) {
//            switch (getResultCode())
//            {
//                case Activity.RESULT_OK:
//                    Toast.makeText(getBaseContext(), "SMS sent",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                    Toast.makeText(getBaseContext(), "Generic failure",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//                case SmsManager.RESULT_ERROR_NO_SERVICE:
//                    Toast.makeText(getBaseContext(), "No service",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//                case SmsManager.RESULT_ERROR_NULL_PDU:
//                    Toast.makeText(getBaseContext(), "Null PDU",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//                case SmsManager.RESULT_ERROR_RADIO_OFF:
//                    Toast.makeText(getBaseContext(), "Radio off",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    }, new IntentFilter(SENT));
//
//    //---when the SMS has been delivered---
//    registerReceiver(new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context arg0, Intent arg1) {
//            switch (getResultCode())
//            {
//                case Activity.RESULT_OK:
//                    Toast.makeText(getBaseContext(), "SMS delivered",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//                case Activity.RESULT_CANCELED:
//                    Toast.makeText(getBaseContext(), "SMS not delivered",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    }, new IntentFilter(DELIVERED));
//
//    SmsManager sms = SmsManager.getDefault();
//    sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
}
