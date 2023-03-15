package com.example.demo5;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private static final int DEGREES_IN_A_CIRCLE = 360;
    private static final String P_LAT_STRING = "parentLatitude";
    private static final String P_LONG_STRING = "parentLongitude";
    private static final String ZERO_STRING = "0";
    private static final String BLANK_STRING = "";
    private static final String HOUSE_LABEL_STRING = "houseLabel";
    private LocationService locationService;
    private OrientationService orientationService;
    public List<Friend> friends;

    private Future<?> future;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    public Pair<Double, Double> userLocation;
    private CompassViewModel viewModel;
    private FriendAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        locationService = LocationService.singleton(this);

        viewModel = new ViewModelProvider(this).get(CompassViewModel.class);

        // create two friends
        Friend friend1 = new Friend();
        Friend friend2 = new Friend();
        friend1.setUid("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454");
        friend2.setUid("c81d4e2e-bcf2-11e6-869b-7df92533d2db");

        // add friends to the database
        viewModel.getDao().upsert(friend1);
        viewModel.getDao().upsert(friend2);

        // get all friends from the database and display them in the adapter
        friends = viewModel.getDao().getAll();
        adapter = new FriendAdapter(this, 0, friends);
        viewModel.getFriends().observe(this, adapter::setFriends);

        // set the location of the two friends
        adapter.getFriends().get(0).setLocation();
        adapter.getFriends().get(1).setLocation();

        // display the locations of the friends in the log to show that they have been updated
        Log.d("RECHECK", "Make sure friends locations changed");
        for (Friend f : adapter.getFriends()) {
            String uid = f.getUidString();
            Log.d(f.loc.toString(), uid);
        }

        // display the locations of the friends in the database to show that they have been updated
        new Handler().postDelayed(() -> {
            // display the locations of the friends in the database to show that they have been updated
            Log.d("RECHECK", "Make sure friends are in database");
            for (Friend f : viewModel.getDao().getAll()) {
                String uid = f.getUidString();
                Log.d(f.loc.toString(), uid);
            }
        }, 1000);

        this.reobserveLocation();
        adapter.setOnTextEditedHandler(viewModel::updateText);
/*
        if (future != null) {
            this.future.cancel(true);
        }
        this.future = backgroundThreadExecutor.submit(() -> {
            if (this.friends != null && this.friends.get(0) != null && this.friends.get(1) != null) {
                //adapter.getFriends().get(0).testMove();
                //adapter.getFriends().get(1).testMove();
                adapter.getFriends().get(1).testMove();
                Log.d("FRIEND1", friend1.loc.toString());
            } else {
                Log.d("ERROR", "not moving");
            }
        });
        this.future = backgroundThreadExecutor.submit(() -> {
            if (this.friends != null && this.friends.get(1) != null) {
                adapter.getFriends().get(0).testMove();
                Log.d("FRIEND2", friend2.loc.toString());
            } else {
                Log.d("ERROR", "not moving");
            }
        });*/
    }

    private void reobserveLocation() {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }

    private void reobserveFriendsLocation() {
        viewModel.getFriends().observe(this, adapter::setFriends);
        //this.friends = adapter.getFriends();
    }

    private void onLocationChanged(Pair<Double, Double> latLong) {
        TextView locationText = findViewById(R.id.locationText);
        locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
        userLocation = latLong;
        //reobserveFriendsLocation();
    }

    private double angleCalculation(Pair<Double, Double> friendLocation) {
        return Math.atan2(friendLocation.second - userLocation.second, friendLocation.first - userLocation.first);
    }
}