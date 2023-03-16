package com.example.demo5;

import android.util.Log;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.util.Random;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "friends")
public class Friend {
    @SerializedName("name")
    public String name;

    @SerializedName("loc")
    Pair<Double, Double> loc;

    private double friendRad;

    @Ignore
    public TextView spot;
    @PrimaryKey
    @NonNull
    @SerializedName("uid")
    UUID uid;

    Friend() {
        loc = new Pair<Double,Double>(0.0,0.0);
        uid = UUID.randomUUID();
        this.name = "Suhaib";
    }

    String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
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

    public UUID getUid() {
        return uid;
    }

    public void setUid(String uuid) {
        uid = UUID.fromString(uuid);
    }

    public String getUidString() {
        return getUid().toString();
    }

    public Pair<Double, Double> getLocation() {
        return loc;
    }

    public void setLocation() {
        Double newLat = (new Random()).nextDouble() * 200 - 100;
        Double newLong = (new Random()).nextDouble() * 200 - 100;
        loc = new Pair<>(newLat, newLong);
    }

    public void testMove() {
        for (int i = 0; i < 100; ++i) {

            switch (i % 4) {
                case 0:
                    loc = new Pair<Double, Double>(1.0, 1.0);
                    break;
                case 1:
                    loc = new Pair<Double, Double>(1.0, -1.0);
                    break;
                case 2:
                    loc = new Pair<Double, Double>(-1.0, -1.0);
                    break;
                case 3:
                    loc = new Pair<Double, Double>(-1.0, 1.0);
                    break;
                default:
                    Log.i("ERROR", "yeah, this isn't working");
                    break;
            }

            try {
                Thread.sleep(1000);
                //Log.i(getUidString(), loc.toString());
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void testMove2() {
        loc = new Pair<Double, Double>(0.0, -1.0);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        //loc.postValue(new Pair<Double, Double>(-1.0, -1.0));
    }
}

