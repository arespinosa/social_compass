package com.example.demo5;

import android.util.Log;

import java.util.UUID;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "friends")
public class Friend {
    MutableLiveData<Pair<Double, Double>> loc = new MutableLiveData<>();
    private double friendRad;
    @PrimaryKey(autoGenerate = true)
    UUID uid;


    Friend() {
        loc.setValue(new Pair<Double, Double>(0.0, 0.0));
        uid = UUID.randomUUID();
        //testMove();
    }

    public void setFriendRad(double val) {
        this.friendRad = val;
    }

    public double getFriendRad() {
        return this.friendRad;
    }


    public Double getLatitude() {
        return getLocation().getValue().first;
    }

    public Double getLongitude() {
        return getLocation().getValue().second;
    }

    public UUID getUid() {
        return uid;
    }

    public String getUidString() {
        return getUid().toString();
    }

    public LiveData<Pair<Double, Double>> getLocation() {
        return loc;
    }

    public void testMove() {
        for (int i = 0; i < 100; ++i) {

            switch (i % 4) {
                case 0:
                    loc.postValue(new Pair<Double, Double>(1.0, 1.0));
                    break;
                case 1:
                    loc.postValue(new Pair<Double, Double>(1.0, -1.0));
                    break;
                case 2:
                    loc.postValue(new Pair<Double, Double>(-1.0, -1.0));
                    break;
                case 3:
                    loc.postValue(new Pair<Double, Double>(-1.0, 1.0));
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
        loc.postValue(new Pair<Double, Double>(0.0, -1.0));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        //loc.postValue(new Pair<Double, Double>(-1.0, -1.0));
    }

    public static Friend fromJSON(String json) {
        return new Gson().fromJson(json, Friend.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}

