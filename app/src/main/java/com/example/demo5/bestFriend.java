package com.example.demo5;

import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class bestFriend {
    MutableLiveData<Pair<Double, Double>> loc = new MutableLiveData<>();

    bestFriend() {
        loc.setValue(new Pair<Double, Double>(0.0, 0.0));
        //testMove();
    }


    public Double getLatitude() {
        return loc.getValue().first;
    }

    public Double getLongitude() {
        return loc.getValue().second;
    }

    public LiveData<Pair<Double, Double>> getLocation() {
        return loc;
    }

    public void testMove() {
        for (int i = 0; i < 100; ++i) {
            loc.postValue(new Pair<Double, Double>(this.getLatitude() + 1, this.getLongitude() + 1));

            try {
                Thread.sleep(1000);
                Log.i("SLEEP", String.valueOf(i));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
