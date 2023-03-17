package com.example.demo5;

import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class UnitTest {
    public static final String EMPTY_STRING = "";
    private static final String P_LAT_STRING = "parentLatitude";
    private static final String P_LONG_STRING = "parentLongitude";
    private static final String ZERO_STRING = "0";
    public static final int DEGREES_IN_A_CIRCLE = 360;
    public static final float TWO_PIE_RADIANS = (float)6.28;
    public static final float HALF_PIE_RADIANS = (float)1.57;
    public static final Double NINETY_DEGREES = 90.0;
    public static final long NINETY_DEGREES_LONG = 90;
    private static final float TWO_SEVENTY_LONG = 270;

    @Test
    public void testUpdateCompassWhenLocationChanges() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>>
                    mockLocationSource = new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            TextView bestFriend = activity.findViewById(R.id.friend1);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                    bestFriend.getLayoutParams();

            //activity.updateFriendDirection();

            float expected = 0;

            assertEquals(expected, layoutParams.circleAngle, 0.001);
        });
    }

    @Test
    public void testUpdateCompassWhenFriendLocationChanges() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>>
                    mockLocationSource = new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));
            activity.userLocation = new androidx.core.util.Pair(expectedLat,expectedLong);

            TextView bestFriend = activity.findViewById(R.id.friend1);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                    bestFriend.getLayoutParams();

            //activity.whenFriendLocationChanges();

            float expected = 0;
            //System.out.println(activity.bestFriend.getLatitude() + ", " + activity.bestFriend.getLongitude());

            assertEquals(layoutParams.circleAngle, expected, 0.001);


        });
    }

}


