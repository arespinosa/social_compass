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

    /*@Test
    public void checkNum() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            long expected = 12312312L;

            ImageView parentHouse = activity.findViewById(R.id.parentHouse);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                                                          parentHouse.getLayoutParams();

            TextView latitude = activity.findViewById(R.id.parentLatitude);
            TextView longitude = activity.findViewById(R.id.parentLongitude);

            latitude.setText(NINETY_DEGREES+EMPTY_STRING);
            longitude.setText(DEGREES_IN_A_CIRCLE+DEGREES_IN_A_CIRCLE+EMPTY_STRING);

            // Checking if both the parents coordinates are not null
            assert (longitude.getText() != null);
            assert (latitude.getText() != null);
        });
    }*/

    /*@Test
    public void validCord() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        //need to have Activity in STARTED state for LiveData observer to be active
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {

            ImageView parentHouse = activity.findViewById(R.id.parentHouse);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                                                          parentHouse.getLayoutParams();

            TextView latitude = activity.findViewById(R.id.parentLatitude);
            TextView longitude = activity.findViewById(R.id.parentLongitude);

            latitude.setText(NINETY_DEGREES+EMPTY_STRING);
            longitude.setText(-NINETY_DEGREES+EMPTY_STRING);

            //Parsing the string inputs as a double
            double lat_cord = Double.parseDouble(latitude.getText().toString());
            double long_cord = Double.parseDouble(longitude.getText().toString());

            // Checking if the inputs are valid within the range
            assert (Math.abs(lat_cord) <= 180);
            assert (Math.abs(long_cord) <= 90);
        });
    }*/

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

            float expected = 0 - NINETY_DEGREES_LONG;

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

            float expected = 0 - NINETY_DEGREES_LONG;
            //System.out.println(activity.bestFriend.getLatitude() + ", " + activity.bestFriend.getLongitude());

            assertEquals(layoutParams.circleAngle, expected, 0.001);

            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            expected = NINETY_DEGREES_LONG;
            layoutParams = (ConstraintLayout.LayoutParams)
                    bestFriend.getLayoutParams();
            System.out.println(activity.bestFriend.getLatitude() + ", " + activity.bestFriend.getLongitude());

            assertEquals(layoutParams.circleAngle, expected, 0.001);*/
        });
    }

    /*@Test
    public void testUpdateCompassWhenOrientationChangesWhenRotatedRight() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource =
                                                                          new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            ConstraintLayout compass = activity.findViewById(R.id.compass);

            activity.updateCompassWhenOrientationChanges(HALF_PIE_RADIANS);

            long expected = DEGREES_IN_A_CIRCLE - NINETY_DEGREES_LONG;
            assert((long)compass.getRotation() == expected);
        });
    }

    @Test
    public void testUpdateCompassWhenOrientationChangesWhenRotatedAround() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource =
                                                                           new MutableLiveData<>();
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

    @Test
    public void testRetrieveManualOrientationWhenOverDegrees() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource =
                                                                         new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            TextView orientationLabelView = activity.findViewById(R.id.orientation_text);
            orientationLabelView.setText(DEGREES_IN_A_CIRCLE+DEGREES_IN_A_CIRCLE+ EMPTY_STRING);

            Float orientation= activity.retrieveManualOrientation();

            long expected = DEGREES_IN_A_CIRCLE-1;
            assert(orientation == (float) expected);
        });
    }

    @Test
    public void testRetrieveManualOrientationWhenUnderDegrees() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource =
                                                                      new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            TextView orientationLabelView = activity.findViewById(R.id.orientation_text);
            orientationLabelView.setText(-NINETY_DEGREES+EMPTY_STRING);

            Float orientation= activity.retrieveManualOrientation();

            long expected = 0;
            assert(orientation == (float) expected);
        });
    }

    @Test
    public void testAngleCalculationWhenPositive() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource =
                                                                          new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            int observed = (int) activity.angleCalculation(NINETY_DEGREES,NINETY_DEGREES);
            int expected = (int) Math.atan2(-NINETY_DEGREES,-NINETY_DEGREES);

            assert(observed == expected);
        });
    }

    @Test
    public void testAngleCalculationWhenNegativeOrZero() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource =
                                                                        new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            int observed = (int) activity.angleCalculation(-NINETY_DEGREES,0.0);
            int expected = (int) Math.atan2(0.0,NINETY_DEGREES);

            assert(observed == expected);
        });
    }

    @Test
    public void testRetrieveParentLocationWhenPLatEmpty() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource =
                                                                         new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            TextView parentLatView = activity.findViewById(R.id.parentLatitude);
            TextView parentLongView = activity.findViewById(R.id.parentLongitude);

            parentLatView.setText(EMPTY_STRING);
            parentLongView.setText(NINETY_DEGREES+EMPTY_STRING);

            SharedPreferences preferences = activity.getPreferences(activity.MODE_PRIVATE);
            String parentLat = preferences.getString(P_LAT_STRING, ZERO_STRING);

            assert parentLat.equals(ZERO_STRING);
        });
    }

    @Test
    public void testRetrieveParentLocationWhenPLongEmpty() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            MutableLiveData<androidx.core.util.Pair<Double, Double>> mockLocationSource =
                                                                        new MutableLiveData<>();
            LocationService locationService = LocationService.singleton(activity);
            locationService.setMockOrientationData(mockLocationSource);

            double expectedLat = (double)0;
            double expectedLong = (double)0;

            mockLocationSource.setValue(new androidx.core.util.Pair(expectedLat,expectedLong));

            TextView parentLatView = activity.findViewById(R.id.parentLatitude);
            TextView parentLongView = activity.findViewById(R.id.parentLongitude);

            parentLongView.setText(EMPTY_STRING);
            parentLatView.setText(NINETY_DEGREES+EMPTY_STRING);

            SharedPreferences preferences = activity.getPreferences(activity.MODE_PRIVATE);
            String parentLong = preferences.getString(P_LONG_STRING, ZERO_STRING);

            assert parentLong.equals(ZERO_STRING);
        });
    }*/
}


