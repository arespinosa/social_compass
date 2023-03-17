package com.example.demo5;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;

@RunWith(RobolectricTestRunner.class)
public class gpsStatusTest {
    public static final String EMPTY_STRING = "";
    private static final String P_LAT_STRING = "parentLatitude";
    private static final String P_LONG_STRING = "parentLongitude";
    private static final String ZERO_STRING = "0";
    public static final int DEGREES_IN_A_CIRCLE = 360;
    public static final float TWO_PIE_RADIANS = (float) 6.28;
    public static final float HALF_PIE_RADIANS = (float) 1.57;
    public static final Double NINETY_DEGREES = 90.0;
    public static final long NINETY_DEGREES_LONG = 90;

    /**
     * Will be checking if our method can calculate our initial offline value being 10 seconds
     */
    @Test
    public void TestSeconds() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {


            GpsSignal gpsSignal = new GpsSignal(activity);

            String currentTime = gpsSignal.timeSinceGPSLastUpdated(10000);
            System.out.println(currentTime);


            assert ("Time since last updated: 10 seconds".equals(currentTime));
            //assert (expected == actual);

        });
    }


    /**
     * Will be checking if our method can convert into minutes rather than seconds
     */
    @Test
    public void TestMinutes() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {


            GpsSignal gpsSignal = new GpsSignal(activity);
            //So we have 3 minutes but is converted as milliseconds
            String currentTime = gpsSignal.timeSinceGPSLastUpdated(210000);
            System.out.println(currentTime);


            assert ("Time since last updated: 3 minutes".equals(currentTime));
            //assert (expected == actual);

        });
    }

    /**
     * Will be checking if our method can convert into hours rather than minutes
     */
    @Test
    public void TestHours() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {


            GpsSignal gpsSignal = new GpsSignal(activity);

            String currentTime = gpsSignal.timeSinceGPSLastUpdated(16200000);
            System.out.println(currentTime);


            assert ("Time since last updated: 4 hours".equals(currentTime));
            //assert (expected == actual);

        });
    }

    //Checking if the system is online
  //  @Test
//    public void CheckOnline() {
//        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
//
//        scenario.moveToState(Lifecycle.State.CREATED);
//        scenario.moveToState(Lifecycle.State.STARTED);
//
//        scenario.onActivity(activity -> {
//
//
//
//            GpsSignal gpsSignal = new GpsSignal(activity);
//
//            String currentTime = gpsSignal.timeSinceGPSLastUpdated(16200000);
//            System.out.println(currentTime);
//
//
//            assert ("Time since last updated: 4 hours".equals(currentTime));
//            //assert (expected == actual);
//
//        });
//    }
}
