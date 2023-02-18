package com.example.demo5;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class TaskExecutorRuleDemoTest {

    // Try commenting out this rule and running the test, it will fail!
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void test_demo_why_executor_rule_is_needed() {
        var scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            var data = new MutableLiveData<String>();

            var latch = new CountDownLatch(1);

            data.observe(activity, str -> {
                assertEquals("hello!", str);
                latch.countDown();
            });

            data.postValue("hello!");

            // Wait 100 ms, but if we don't get the update, fail the test.
            try {
                var hitZeroWithoutTimingOut = latch.await(100, TimeUnit.MILLISECONDS);
                if (!hitZeroWithoutTimingOut) {
                    fail("Did not get update from LiveData.");
                }
            } catch (InterruptedException e) {
                fail("Test interrupted.");
            }
        });
    }
}
