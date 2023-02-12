package com.example.demo5;

import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TimeServiceTest {
    @Test
    public void test_time_service(){
        MutableLiveData<Long> mockDataSource = new MutableLiveData<Long>();
        TimeService timeService = TimeService.singleton();
        //Update the singleton instance to use the mockDataSource
        timeService.setMockTimeSource(mockDataSource);
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        //need to have Activity in STARTED state for LiveData observer to be active
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity->{
            long expected = 12312312L;
            //set a random value for the mock data source
            mockDataSource.setValue(expected);
            //get value from textview
            TextView textView = activity.findViewById(R.id.timeTextView);
            long observed = Long.parseLong(textView.getText().toString());


            //check whether the values are equal
            assert observed == expected;
        });
    }

}
