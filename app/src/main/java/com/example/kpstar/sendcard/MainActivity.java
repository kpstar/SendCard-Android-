package com.example.kpstar.sendcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    EditText mEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputField();
    }

    public void InputField() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Profile", Context.MODE_PRIVATE);

        String temp = "";

        temp = sharedPreferences.getString("fullname", "");
        if (!temp.isEmpty() && sharedPreferences.getString("register","").isEmpty()) {
            Intent mIntent = new Intent(this, WorkingActivity.class);
            startActivity(mIntent);
            finish();
        }

        mEditText = (EditText)findViewById(R.id.edtFullName);
        mEditText.setText(temp);

        Button btn = (Button)findViewById(R.id.btnProfile);
        if (temp == "") {
            btn.setText("Register");
        } else {
            btn.setText("Keep Changes");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.InsertValue();
            }
        });

        temp = sharedPreferences.getString("jobtitle", "");

        mEditText = (EditText)findViewById(R.id.edtJobTitle);
        mEditText.setText(temp);

        temp = sharedPreferences.getString("companyname", "");
        mEditText = (EditText)findViewById(R.id.edtCompanyName);
        mEditText.setText(temp);

        temp = sharedPreferences.getString("phone", "");
        mEditText = (EditText)findViewById(R.id.edtPhone);
        mEditText.setText(temp);

        temp = sharedPreferences.getString("email", "");
        mEditText = (EditText)findViewById(R.id.edtEmail);
        mEditText.setText(temp);

        temp = sharedPreferences.getString("address", "");
        mEditText = (EditText)findViewById(R.id.edtAddress);
        mEditText.setText(temp);

        temp = sharedPreferences.getString("website", "");
        mEditText = (EditText)findViewById(R.id.edtWebsite);
        mEditText.setText(temp);

        temp = sharedPreferences.getString("google", "");
        mEditText = (EditText)findViewById(R.id.edtGoogle);
        mEditText.setText(temp);

        temp = sharedPreferences.getString("facebook", "");
        mEditText = (EditText)findViewById(R.id.edtFacebook);
        mEditText.setText(temp);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("register", "");
        editor.apply();
    }

    public void InsertValue() {
        SharedPreferences.Editor editor = this.getSharedPreferences("Profile", Context.MODE_PRIVATE).edit();

        mEditText = (EditText)findViewById(R.id.edtFullName);
        String temp = "";

        temp = mEditText.getText().toString();

        mEditText = (EditText)findViewById(R.id.edtEmail);
        String temp1 = "";
        temp1 = mEditText.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setNeutralButton("Dismiss", null);

        if (temp.isEmpty()) {
            builder.setMessage("Please insert your Full Name");
            builder.show();
            return;
        }

        if (temp1.isEmpty()) {
            builder.setMessage("Please insert your Email");
            builder.show();
            return;
        }

        editor.putString("fullname", temp);
        editor.putString("email", temp1);

        mEditText = (EditText)findViewById(R.id.edtJobTitle);
        editor.putString("jobtitle", mEditText.getText().toString());

        mEditText = (EditText)findViewById(R.id.edtCompanyName);
        editor.putString("companyname", mEditText.getText().toString());

        mEditText = (EditText)findViewById(R.id.edtPhone);
        editor.putString("phone", mEditText.getText().toString());

        mEditText = (EditText)findViewById(R.id.edtAddress);
        editor.putString("address", mEditText.getText().toString());

        mEditText = (EditText)findViewById(R.id.edtWebsite);
        editor.putString("website", mEditText.getText().toString());

        mEditText = (EditText)findViewById(R.id.edtGoogle);
        editor.putString("google", mEditText.getText().toString());

        mEditText = (EditText)findViewById(R.id.edtFacebook);
        editor.putString("facebook", mEditText.getText().toString());

        editor.apply();

        Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show();

        Intent mIntent = new Intent(this, WorkingActivity.class);
        startActivity(mIntent);
        finish();
    }
}
