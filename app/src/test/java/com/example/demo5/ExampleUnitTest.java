package com.example.demo5;

import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static final int DEGREES_IN_A_CIRCLE = 360;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testParentHouseAngle() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        //need to have Activity in STARTED state for LiveData observer to be active
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity->{
            long expected = 12312312L;

            ImageView parentHouse = (ImageView) findViewById(R.id.parentHouse);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) parentHouse.getLayoutParams();

            TextView textView = findViewById(R.id.timeTextView);
            locationService.getLocation().observe(this,loc->{
                textView.setText(Double.toString(loc.first)+" , "+
                        Double.toString(loc.second));

            //check whether the values are equal
            assert layoutParams.circleAngle == expected;
        });
    }
}