package com.example.demo5;

import android.util.Log;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "friends")
public class Friend {

    private String label;
    private double latitude;
    private double longitude;
    private String createdAt;
    private String updatedAt;

    @Ignore
    private double friendRad;

    @Ignore
    public TextView spot;
    @PrimaryKey
    @NonNull
    @SerializedName("public_code")
    String uid;

    Friend() {
        uid = "UUID.randomUUID()";
        this.label = "Suhaib";
    }

    String getName() {
        return this.label;
    }
    public void setName(String name) {
        this.label = name;
    }

    @Override
    public String toString () {
        return this.getName();
    }

    public void setFriendRad(double val) {
        this.friendRad = val;
    }

    public double getFriendRad() {
        return this.friendRad;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) { this.uid = uid; }

    public static Friend fromJSON(String json) {
        return new Gson().fromJson(json, Friend.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}

