package com.example.kpstar.sendcard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import pub.devrel.easypermissions.EasyPermissions;

public class WorkingActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mSendCard = null;
    private Button mSendWebsite = null;
    private Button mSendThank = null;
    private Button mSendReview = null;
    private Button mSetting = null;
    private Intent i = null;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String mSubject;
    private String mMessage;
    PopupMenu popup;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);

        String[] permissions = {Manifest.permission.SEND_SMS, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!EasyPermissions.hasPermissions(this, Manifest.permission.SEND_SMS) || !EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(permissions, 0);
        }

        mSendThank = (Button)findViewById(R.id.btnThank);
        mSendThank.setOnClickListener(this);

        mSetting = (Button)findViewById(R.id.btnSetting);
        mSetting.setOnClickListener(this);

        mSendReview = (Button)findViewById(R.id.btnReview);
        mSendReview.setOnClickListener(this);

        mSendCard = (Button)findViewById(R.id.btnCard);
        mSendCard.setOnClickListener(this);

        mSendWebsite = (Button)findViewById(R.id.btnWebsite);
        mSendWebsite.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("Profile", Context.MODE_PRIVATE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnThank:
                mMessage = "Hello, Just wanted to say thank you again for your business at " + sharedPreferences.getString("companyname", "");
                mSubject = "Thank You!";
                editor = sharedPreferences.edit();
                editor.putString("subject", mSubject);
                editor.putString("message", mMessage);
                editor.apply();
                popup = new PopupMenu(this, mSendThank);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.action, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.email:
                                sendEmail();
                                break;
                            case R.id.sms:
                                sendSMS();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
                break;
            case R.id.btnWebsite:

                mMessage = "Please visit our website at: " + sharedPreferences.getString("website", "");
                mSubject = "Invitation!";
                editor = sharedPreferences.edit();
                editor.putString("subject", mSubject);
                editor.putString("message", mMessage);
                editor.apply();
                popup = new PopupMenu(this, mSendWebsite);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.action, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.email:
                                sendEmail();
                                break;
                            case R.id.sms:
                                sendSMS();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
                break;
            case R.id.btnReview:
                mMessage = "Can you please help me by writing a review. Your review will hopefully bring me my next customer! Please click on the links below:\n\n"  + sharedPreferences.getString("google", "") + "\n" + sharedPreferences.getString("facebook", "");
                mSubject = "Review!";
                editor = sharedPreferences.edit();
                editor.putString("subject", mSubject);
                editor.putString("message", mMessage);
                editor.apply();
                popup = new PopupMenu(this, mSendReview);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.action, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.email:
                                sendEmail();
                                break;
                            case R.id.sms:
                                sendSMS();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
                break;
            case R.id.btnSetting:
                Intent mIntent = new Intent(this, MainActivity.class);
                SharedPreferences.Editor editor = this.getSharedPreferences("Profile", Context.MODE_PRIVATE).edit();
                editor.putString("register","yes");
                editor.apply();
                startActivity(mIntent);
                //finish();
                break;
            case R.id.btnCard:
                i = new Intent(this, CardActivity.class);
                startActivity(i);
            default:
                break;
        }
    }

    private void sendEmail() {
        i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        i.putExtra(Intent.EXTRA_TEXT   , mMessage);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS() {
        i = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "", null));
        i.putExtra("sms_body", mMessage);
        startActivity(i);
    }
}
