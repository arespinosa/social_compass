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
    public static final float TWO_PIE_RADIANS = (float) 6.28;
    public static final float HALF_PIE_RADIANS = (float) 1.57;
    public static final Double NINETY_DEGREES = 90.0;
    public static final long NINETY_DEGREES_LONG = 90;
    private static final float TWO_SEVENTY_LONG = 270;

    public void UnitTest() {
        assertEquals(1, 1);
    }
}
