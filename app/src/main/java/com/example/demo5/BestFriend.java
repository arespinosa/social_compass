package com.example.demo5;

import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BestFriend {
    MutableLiveData<Pair<Double, Double>> loc = new MutableLiveData<>();

    BestFriend() {
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
}
