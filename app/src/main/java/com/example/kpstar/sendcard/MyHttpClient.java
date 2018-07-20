package com.example.kpstar.sendcard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyHttpClient extends AsyncHttpClient {

    public Context mContext;
//    public String mWebsiteUrl = "http://192.168.0.218/AmpFields/public/api";
    public String mLogin = "/api.php";
    public String mWebsiteUrl = "http://app.amplifiedreferrals.com";
    public String mInfo = "";
    public RequestParams mParams = null;
    public String mServerUrl = "";
    public LocalBroadcastManager mBroad = null;

    public MyHttpClient( Context context) { mContext = context; }

    public void login(String user, String password) {

        mServerUrl = mWebsiteUrl + mLogin;

        mParams = new RequestParams();
        mParams.put("email", user);
        mParams.put("pass", password);

        post(mServerUrl, mParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                mBroad = LocalBroadcastManager.getInstance(mContext);

                JSONObject jsonObject;
                String userRole, userName;
                int mClientsNum;
                JSONArray clients;
                Log.e("Response", new String(responseBody));

                String param = new String(responseBody);
                Intent mIntent = new Intent("Authentication");
                ArrayList<String> client, emails, created;
                client = new ArrayList<String>();
                emails = new ArrayList<String>();
                created = new ArrayList<String>();
                ArrayList<Integer> loginNum = new ArrayList<Integer>();
                if (param.equals("Incorrect Parameters")) {
                    mIntent.putExtra("Login","Failure");
                    mIntent.putExtra("userrole","Failure");
                    mBroad.sendBroadcast(mIntent);
                    return;
                } else {
                    try {

                        mIntent.putExtra("Login", "Success");
                        jsonObject = new JSONObject(new String(responseBody));
                        userRole = jsonObject.getString("userrole");
                        mIntent.putExtra("userrole", userRole);
                        if (userRole.equals("client")) {
                            mBroad.sendBroadcast(mIntent);
                            return;
                        }
//                        userName = jsonObject.getString("username");
//                        clients = jsonObject.getJSONArray("clients");
//                        mClientsNum = clients.length();
//
//                        mIntent.putExtra("username", userName);
//                        mIntent.putExtra("usernum", mClientsNum);
//
//                        for (int i = 0; i < mClientsNum; i++) {
//                            JSONObject temp = (JSONObject) clients.get(i);
//                            client.add(temp.getString("name"));
//                            emails.add(temp.getString("email"));
//                            created.add(temp.getString("registerdate"));
//                            loginNum.add(temp.getInt("loginnum"));
//                        }
//
//                        mIntent.putIntegerArrayListExtra("apinums", loginNum);
//                        mIntent.putStringArrayListExtra("apinames", client);
//                        mIntent.putStringArrayListExtra("apiemails", emails);
//                        mIntent.putStringArrayListExtra("apicreated", created);
                        Bundle b = new Bundle();
                        b.putString("JSON",jsonObject.toString());
                        mIntent.putExtras(b);
                        mIntent.putExtra("userrole", userRole);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mBroad.sendBroadcast(mIntent);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mBroad = LocalBroadcastManager.getInstance(mContext);

                Intent mIntent = new Intent("Authentication");
                mIntent.putExtra("Login", "Failure");
                mBroad.sendBroadcast(mIntent);
            }
        });
    }

    public void getInfos(int reward_id) {

        mServerUrl = mWebsiteUrl + mInfo;

        mParams = new RequestParams();
        mParams.put("reward_id", reward_id);

        post(mServerUrl, mParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                mBroad = LocalBroadcastManager.getInstance(mContext);

                Intent mIntent = new Intent("Authentication");

//                if (statusCode == 200) {
//                    Log.e("TAG", "Success");
//                    mIntent.putExtra("Login", "Success");
//                } else {
//                    Log.e("TAG", "Fail");
//                    mIntent.putExtra("Login", "Fail");
//                }

                mBroad.sendBroadcast(mIntent);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mBroad = LocalBroadcastManager.getInstance(mContext);

                Intent mIntent = new Intent("Authentication");
                mIntent.putExtra("Login", "Failure");
                mBroad.sendBroadcast(mIntent);
            }
        });
    }
}
