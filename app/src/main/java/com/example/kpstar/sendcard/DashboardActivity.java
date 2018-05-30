package com.example.kpstar.sendcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {


    TextView txtRole, txtAllusers;
    Button btnMain;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtAllusers = (TextView)findViewById(R.id.txtAlluser);
        txtRole = (TextView)findViewById(R.id.txtRole);
        btnMain = (Button)findViewById(R.id.btnMain);
        recyclerView = (RecyclerView)findViewById(R.id.tableRecycle);

        btnMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        sharedPreferences = getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String temp = "";

        temp = sharedPreferences.getString("fullname", "");
        if (!temp.isEmpty()) {
            Intent mIntent = new Intent(this, WorkingActivity.class);
            startActivity(mIntent);
        } else {
            Intent mIntent = new Intent(this, MainActivity.class);
            startActivity(mIntent);
        }
    }
}
