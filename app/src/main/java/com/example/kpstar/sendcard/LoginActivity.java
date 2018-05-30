package com.example.kpstar.sendcard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, edtPasswd;
    CheckBox remember;
    Button loginBtn;
    Boolean rememFlag = false;
    ProgressBar mProgress;
    SharedPreferences mShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText) findViewById(R.id.edtEmail);
        edtPasswd = (EditText)findViewById(R.id.edtPasswd);
        loginBtn = (Button)findViewById(R.id.btnLogin);
        remember = (CheckBox)findViewById(R.id.checkBox);

        mShare = getSharedPreferences("Profile", Context.MODE_PRIVATE);
        rememFlag = mShare.getBoolean("RememberMe", false);

        if (rememFlag) {
            editEmail.setText(mShare.getString("UserEmail", ""));
            edtPasswd.setText(mShare.getString("UserPasswd", ""));
        }
        remember.setChecked(rememFlag);

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememFlag = isChecked;
            }
        });

        mProgress = (ProgressBar)findViewById(R.id.mProgress);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String error = "";

                String email = editEmail.getText().toString();
                String passwd = edtPasswd.getText().toString();

                if (email.isEmpty()) {
                    error = "Please insert your email";
                }
                if (passwd.isEmpty()) {
                    error = "Please insert your password";
                }
                if (!error.isEmpty()) {

                }

                MyHttpClient myHttpClient = new MyHttpClient(LoginActivity.this);
                myHttpClient.login(email, passwd);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("Authentication")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            mProgress.setVisibility(View.INVISIBLE);
            String mUser = editEmail.getText().toString();
            String mPasswd = edtPasswd.getText().toString();
            editEmail.setText("");
            edtPasswd.setText("");

            String temp_desc = "";

            temp_desc = intent.getStringExtra("Login");

            if (temp_desc == "Success") {

                SharedPreferences.Editor editor = mShare.edit();

                if (rememFlag) {
                    editor.putString("UserEmail", mUser);
                    editor.putString("UserPasswd", mPasswd);
                } else {
                    editor.putString("UserEmail", "");
                    editor.putString("UserPasswd", "");
                }
                editor.putBoolean("RememberMe", rememFlag);
                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                startIntent();
                return;
            } else if (temp_desc == "Failure") {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                mBuilder.setTitle("Error")
                        .setMessage("Username or Password is incorrect.")
                        .setNeutralButton("OK", null);
                mBuilder.show();
                return;
            }
        }
    };

    public void startIntent() {
        Intent mIntent = new Intent(this, DashboardActivity.class);
        startActivity(mIntent);
        finish();
    }
}
