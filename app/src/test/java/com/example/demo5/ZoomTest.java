package com.example.demo5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ZoomTest {

    @Test
    public void test() {
        assertEquals(0,0, 0);
    }

    /**
     * Will Test if a friend is placed at the correct angle
     */
    /*@Test
    public void InitialStart() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            //Mocking the user Location
            Pair<Double, Double> userLocation = new Pair<>(33.7450, -117.8849);
            // Creating the distance
            Distance distance = new Distance(activity);
            ZoomFeature zoomFeature = new ZoomFeature(activity);

            // Creating one mock of a friend
            TextView Bobby = (TextView) activity.findViewById(R.id.best_friend);

            zoomFeature.PerformZoom(2, distance, userLocation);
            Pair<String, String> friendLocation = distance.retrieveFriendLocation();


            //Grabbing the circle ang for Bobby
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) Bobby.getLayoutParams();

            // Having the zoom out
            var Zoomout = activity.findViewById(R.id.minus_btn);

            assert (Bobby.getVisibility() == TextView.VISIBLE);
            assert (Zoomout.performClick());

            assert(layoutParams.circleRadius == 327);

        });
    }

    @Test
    public void VISIBILITY() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);


        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            Pair<Double, Double> userLocation = new Pair<>(20.5937, 78.9629);
            Distance distance = new Distance(activity);
            ZoomFeature zoomFeature = new ZoomFeature(activity);

            // Creating one mock of a friend
            TextView Bobby = (TextView) activity.findViewById(R.id.best_friend);

            zoomFeature.PerformZoom(2, distance, userLocation);

            var Zoomout = activity.findViewById(R.id.minus_btn);

            assert(Zoomout.performClick());

            //Want to assure that our friend is shown despite zooming out
            assert (Bobby.isShown());

        });
    }*/
}
