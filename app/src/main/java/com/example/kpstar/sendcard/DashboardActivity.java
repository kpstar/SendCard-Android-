package com.example.kpstar.sendcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {


    TextView txtRole, txtAllusers;
    Button btnMain;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    ArrayList<ClientsModel> clientsModels;
    ClientsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtAllusers = (TextView)findViewById(R.id.txtAlluser);
        txtRole = (TextView)findViewById(R.id.txtRole);
        btnMain = (Button)findViewById(R.id.btnMain);
        recyclerView = (RecyclerView)findViewById(R.id.tableRecycle);

        MyHttpClient client = new MyHttpClient(this);

        Bundle b = getIntent().getExtras();
        String jsonString = b.getString("JSON");

        clientsModels = new ArrayList<ClientsModel>();

        ClientsModel clientsModel = new ClientsModel("No","Email","Name","Registered", "Num");
        clientsModels.add(clientsModel);

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("clients");

            String userRole = jsonObject.getString("userrole");
            String userName = jsonObject.getString("username");

            userRole = userRole + " : " + userName;
            txtRole.setText(userRole);


            userRole = "All Users : ";
            userRole += String.valueOf(jsonArray.length());
            txtAllusers.setText(userRole);
            JSONObject json;
            for (int i=0;i<jsonArray.length();i++) {

                json = jsonArray.getJSONObject(i);
                clientsModel = new ClientsModel(String.valueOf(i+1),json.getString("email"),json.getString("name"), json.getString("regiserdate"),
                        String.valueOf(json.getInt("loginnum")));
                clientsModels.add(clientsModel);
            }

            adapter = new ClientsAdapter(this, clientsModels);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
