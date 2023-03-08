package com.example.demo5;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import androidx.core.util.Pair;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "friends")
public class Friend {
    @SerializedName("name")
    public String name;
    @SerializedName("loc")
    Pair<Double, Double> loc;

    private double friendRad;
    @PrimaryKey(autoGenerate = true)
    @SerializedName("uid")
    UUID uid;

    Friend() {
        loc = new Pair<Double, Double>(0.0, 0.0);
        uid = UUID.randomUUID();
        //testMove();
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

    public String getUidString() {
        return getUid().toString();
    }

    public Pair<Double, Double> getLocation() {
        return loc;
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
                Log.i("SLEEP", String.valueOf(i));
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

