package com.example.demo5;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        locationService = LocationService.singleton(this);

        adapter = new FriendAdapter(this, 0);

        viewModel = new ViewModelProvider(this).get(CompassViewModel.class);
        adapter.setOnTextEditedHandler(viewModel::updateText);
        var friends = viewModel.getFriends();
        friends.observe(this, adapter::setFriends);
        this.friends = friends.getValue();

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

    private void onLocationChanged(Pair<Double, Double> latLong) {
        TextView locationText = findViewById(R.id.locationText);
        locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
        userLocation = latLong;
        var friends = viewModel.getFriends();
        friends.observe(this, this::whenFriendLocationChanges);
    }

    private void whenFriendLocationChanges(List<Friend> friends) {
        var friend0 = friends.get(0);
        var friend1 = friends.get(1);

        friend0.setFriendRad(angleCalculation(friend0.getLocation()));

        friend1.setFriendRad(angleCalculation(friend1.getLocation()));


        TextView friend0View = findViewById(R.id.best_friend1);
        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams)
                friend0View.getLayoutParams();
        layoutParams1.circleAngle = (float) Math.toDegrees(friend0.getFriendRad());
        friend0View.setLayoutParams(layoutParams1);

        TextView friend1View = findViewById(R.id.best_friend2);
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams)
                friend1View.getLayoutParams();
        layoutParams2.circleAngle = (float) Math.toDegrees(friend1.getFriendRad());
        friend1View.setLayoutParams(layoutParams2);
    }

    private double angleCalculation(Pair<Double, Double> friendLocation) {
        return Math.atan2(friendLocation.second - userLocation.second, friendLocation.first - userLocation.first);
    }
}