package com.example.demo5;

import static org.junit.Assert.assertEquals;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import android.util.Pair;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;

@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {
    public static final int DEGREES_IN_A_CIRCLE = 360;
    public static final float TWO_PIE_RADIANS = (float)6.28;
    public static final float HALF_PIE_RADIANS = (float)1.57;
    public static final Double NINETY_DEGREES = 90.0;
    public static final long NINETY_DEGREES_LONG = 90;

    /**
     * (User Story 2,Test 2): check for non-null inputs
     */
    @Test
    public void checkNum(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        //need to have Activity in STARTED state for LiveData observer to be active
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            ImageView parentHouse = activity.findViewById(R.id.parentHouse);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) parentHouse.getLayoutParams();

            TextView latitude = activity.findViewById(R.id.parentLatitude);
            TextView longitude = activity.findViewById(R.id.parentLongitude);

            latitude.setText("90.4");
            longitude.setText("1234.5");

            // Checking if both the parents coordinates are not null
            assert (longitude.getText() != null);
            assert (latitude.getText() != null);
        });
    }


    /**
     * (User Story 2, Test 3) : Checking if the coordinates are within the range for longitude and latitude
     */
    @Test
    public void validCord(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        //need to have Activity in STARTED state for LiveData observer to be active
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            ImageView parentHouse = activity.findViewById(R.id.parentHouse);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) parentHouse.getLayoutParams();

            TextView latitude = activity.findViewById(R.id.parentLatitude);
            TextView longitude = activity.findViewById(R.id.parentLongitude);

            latitude.setText("10.4");
            longitude.setText("-67.5");

            //Parsing the string inputs as a double
            double lat_cord = Double.parseDouble(latitude.getText().toString());
            double long_cord = Double.parseDouble(longitude.getText().toString());

            // Checking if the inputs are valid within the range
            assert (Math.abs(lat_cord) <= 180);
            assert (Math.abs(long_cord) <= 90);
        });
    }

    @Test
    public void testUpdateCompassWhenLocationChanges() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource = new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            ImageView parentHouse = activity.findViewById(R.id.parentHouse);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) parentHouse.getLayoutParams();

            activity.updateCompassWhenLocationChanges(NINETY_DEGREES, 0.0);

            long expected = NINETY_DEGREES_LONG + NINETY_DEGREES_LONG;

            assert(layoutParams.circleAngle == expected);
        });
    }

    @Test
    public void testUpdateCompassWhenLocationChanges2() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource = new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            ImageView parentHouse = activity.findViewById(R.id.parentHouse);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) parentHouse.getLayoutParams();

            activity.updateCompassWhenLocationChanges(0.0, NINETY_DEGREES);

            long expected = -NINETY_DEGREES_LONG;

            assert(layoutParams.circleAngle == expected);
        });
    }

    @Test
    public void testUpdateCompassWhenOrientationChanges() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource = new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            ConstraintLayout compass = activity.findViewById(R.id.compass);

            activity.updateCompassWhenOrientationChanges(HALF_PIE_RADIANS);

            long expected = DEGREES_IN_A_CIRCLE - NINETY_DEGREES_LONG;
            System.out.println(compass.getRotation() + " vs " + expected);
            assert((long)compass.getRotation() == expected);
        });
    }

    @Test
    public void testUpdateCompassWhenOrientationChanges2() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource = new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            ConstraintLayout compass = activity.findViewById(R.id.compass);

            activity.updateCompassWhenOrientationChanges(TWO_PIE_RADIANS);

            long expected = 0;
            assert((long)compass.getRotation() == expected);
        });
    }
}

