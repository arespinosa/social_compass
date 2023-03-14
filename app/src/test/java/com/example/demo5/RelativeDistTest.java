package com.example.demo5;

import static org.junit.Assert.assertEquals;

import android.content.SharedPreferences;
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
public class RelativeDistTest {
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
     * Will Test if the appropriate miles is calculated and placed within the correct disk
     */
    @Test
    public void checkDisc1() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            Distance distance = new Distance(activity);
            Pair<String, String> friendLocation = distance.retrieveFriendLocation();
            double expected = 0.6275605443700261;
            System.out.println(friendLocation.first);
            System.out.println(friendLocation.second);

            double actual = distance.distanceCalculation(33.81212871249689, -117.91895732047263);

            //Will check if we are less than a mile away thus signifying that we are in the correct location
            System.out.println(actual);
            assert (actual < 1);
            assert(actual * 50 < 50);
            //assert (expected == actual);

        });
    }

    @Test
    public void checkDisc2() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            Distance distance = new Distance(activity);
            double expected = 2.0342139162529262;

            double actual = distance.distanceCalculation(33.86234, -117.92193);

            //Will check if we are less than a mile away thus signifying that we are in the correct location
            assert (actual < 10);
            assert((actual / 10) * 100 < 100);
            assert (expected == actual);

            System.out.println("Actual: " + actual);

        });
    }


    @Test
    public void CheckDisc3(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            //Will check if it's 50 miles away
            Distance distance = new Distance(activity);
            double expected = 55.31004861580981;

            double actual = distance.distanceCalculation(32.880238450982766, -117.2339547722171);
            //Will check if we are less than a mile away thus signifying that we are in the correct location
            assert (actual >= 10 && actual < 500);
            assert((actual /490) * 200 < 200);
            assert (expected == actual);

        });
    }

    @Test
    public void CheckDisc4(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            //Will check if it's 500 miles away
            Distance distance = new Distance(activity);
            double expected = 4335.999311862558;
            int expectedWhole = (int) Math.round(expected);

            double actual = distance.distanceCalculation(-19.824292359262596, -47.058219362212725);
            int expectedActual = (int) Math.round(actual);

            //Will check if we are less than a mile away thus signifying that we are in the correct location
            assert (actual >= 500);
            assert((actual /12427.0) * 500 < 500);
            assert (expectedWhole == expectedActual);

        });
    }
}