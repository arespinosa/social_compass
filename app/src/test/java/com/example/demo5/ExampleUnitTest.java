package com.example.demo5;

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




/**
 * (User Story 2,Test 1): Checking if the angles are calculated correctly
 *
 */

@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {
    public static final int DEGREES_IN_A_CIRCLE = 360;

    @Test
    public void testParentHouseAngle() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);


        //need to have Activity in STARTED state for LiveData observer to be active
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
        //assertEquals(activity.findViewById(R.id.btn_one).performClick(), true);
        MutableLiveData<Pair<Double,Double>> mockLocationSource = new MutableLiveData<Pair<Double,Double>>();
        LocationService locationService = LocationService.singleton(activity);
        locationService.setMockOrientationSource(mockLocationSource);

        double expectedLat = (double)0;
        double expectedLong = (double)90;

        mockLocationSource.setValue(new Pair(expectedLat,expectedLong));
        ImageView parentHouse = activity.findViewById(R.id.parentHouse);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) parentHouse.getLayoutParams();

        TextView textView = activity.findViewById(R.id.timeTextView);

        long expected =0;

        locationService.getLocation().observe(activity, loc -> {
            textView.setText(Double.toString(loc.first) + " , " +
                    Double.toString(loc.second));

            //check whether the values are equal
            //assert layoutParams.circleAngle == expected;
            assert layoutParams.circleAngle == expected;
        });
    });
}

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
}

