package com.example.demo5;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    Friend friend1 = new Friend();
    Friend friend2 = new Friend();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        locationService = LocationService.singleton(this);

        adapter = new FriendAdapter(this, 0, friends);

        viewModel = new ViewModelProvider(this).get(CompassViewModel.class);
        adapter.setOnTextEditedHandler(viewModel::updateText);

        for (Friend L : viewModel.getDao().getAll()) {
            String uid = L.getUidString();
            System.out.println(uid);
            viewModel.getDao().delete(L);
        }

        friend1.setUid("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454");
        friend2.setUid("c81d4e2e-bcf2-11e6-869b-7df92533d2db");

        viewModel.getDao().upsert(friend1);
        viewModel.getDao().upsert(friend2);

        System.out.println("They are in " + viewModel.getDao().get(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454")));
        System.out.println(viewModel.getDao().get(UUID.fromString("c81d4e2e-bcf2-11e6-869b-7df92533d2db")));

        /*ArrayList j = new ArrayList();
        j.add(new Friend());
        j.add(new Friend());

        this.friends = j;*/

        //if (this.friends != null)
        this.reobserveLocation();


        if (future != null) {
            this.future.cancel(true);
        }
        this.future = backgroundThreadExecutor.submit(() -> {
            if (this.friends != null && this.friends.get(0) != null && this.friends.get(1) != null) {
                this.friends.get(0).testMove();
                this.friends.get(1).testMove();
            } else {
                Log.d("ERROR", "not moving");
            }
        });

    }

    private void reobserveLocation() {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }

    private void reobserveFriendsLocation() {
        var friends = viewModel.getFriends();
        friends.observe(this, adapter::setFriends);
        this.friends = adapter.getFriends();
    }
    
    private void onLocationChanged(Pair<Double, Double> latLong) {
        TextView locationText = findViewById(R.id.locationText);
        locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
        userLocation = latLong;
        this.reobserveFriendsLocation();
        //whenFriendLocationChanges();
    }

    public void whenFriendLocationChanges() {
        //rad = angleCalculation(location);
        var bestFriendLocationData1 = friends.get(0).getLocation();

        for (int i = 0; i <= 1; ++i) {
            int ind = i;
            bestFriendLocationData1.observe(this, friendLocation -> {
                friends.get(ind).setFriendRad(angleCalculation(friendLocation));
            });

            if (i == 0) {
                TextView bestFriend1 = findViewById(R.id.friend1);
                ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams)
                        bestFriend1.getLayoutParams();
                layoutParams1.circleAngle = (float) Math.toDegrees(friends.get(i).getFriendRad());
                bestFriend1.setLayoutParams(layoutParams1);

            } else {
                TextView bestFriend1 = findViewById(R.id.friend2);
                ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams)
                        bestFriend1.getLayoutParams();
                layoutParams1.circleAngle = (float) Math.toDegrees(friends.get(i).getFriendRad());
                bestFriend1.setLayoutParams(layoutParams1);

            }
        }
    }

    private double angleCalculation(Pair<Double, Double> friendLocation) {
        return Math.atan2(friendLocation.second - userLocation.second, friendLocation.first - userLocation.first);
    }
}