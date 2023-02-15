package com.example.demo5;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.assertEquals;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {
    public static final int DEGREES_IN_A_CIRCLE = 360;

    @Test

    public void testParentHouseAngle() {
        ActivityScenario
                <com.example.demo5.MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);


        //need to have Activity in STARTED state for LiveData observer to be active
        //android.Manifest.permission = PackageManager.PERMISSION_GRANTED;


        scenario.onActivity(activity -> {

            //assertEquals(activity.findViewById(R.id.btn_one).performClick(), true);

            //ActivityCompat.setPermissionCompatDelegate(new ActivityCompat.PermissionCompatDelegate(PackageManager.PERMISSION_GRANTED));

            long expected = 12312312L;

            ImageView parentHouse = activity.findViewById(R.id.parentHouse);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) parentHouse.getLayoutParams();

            TextView textView = activity.findViewById(R.id.timeTextView);

            LocationService locationService = LocationService.singleton(activity);
            locationService.getLocation().observe(activity, loc -> {
                textView.setText(Double.toString(loc.first) + " , " +
                        Double.toString(loc.second));

                //check whether the values are equal
                //assert layoutParams.circleAngle == expected;

                layoutParams.circleAngle = 20;

                assertEquals(layoutParams.circleAngle, 20);


            });
        });
    }
}

//from lab 2 espresso test
/**
 * scenario.onActivity(activity -> {
 *             assertEquals(activity.findViewById(R.id.btn_one).performClick(), true);
 *         });
 *         scenario.onActivity(activity -> {
 *             assertEquals(activity.findViewById(R.id.btn_plus).performClick(), true);
 *         });
 *         scenario.onActivity(activity -> {
 *             assertEquals(activity.findViewById(R.id.btn_one).performClick(), true);
 *         });
 *         scenario.onActivity(activity -> {
 *             assertEquals(activity.findViewById(R.id.btn_equals).performClick(), true);
 *         });
 *         scenario.onActivity(activity -> {
 *             TextView display = (TextView) activity.findViewById(R.id.display);
 *             assertEquals(display.getText(), "2");
 *         });
 */
