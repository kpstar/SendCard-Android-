package com.example.kpstar.sendcard;

public class ClientsModel {

    public String mID;
    public String mEmail;
    public String mName;
    public String mCreated;
    public String mNum;

    public ClientsModel(String id, String email, String name, String created, String num) {

        mID = id;
        mEmail = email;
        mNum = num;
        mName = name;
        mCreated = created;
    }

    public String getmID() {
        return mID;
    }

    public String getmNum() {
        return mNum;
    }

    public String getmName() {
        return mName;
    }

    public String getmCreated() {
        return mCreated;
    }

    public String getmEmail() {
        return mEmail;
    }
}
