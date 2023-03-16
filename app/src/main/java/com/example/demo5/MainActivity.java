package com.example.demo5;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private LocationService locationService;
    private OrientationService orientationService;

    private Future<?> future;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    public Pair<Double, Double> userLocation;
    private LiveData<List<Friend>> friends;
    private LiveData<Friend> friend;
    private CompassViewModel viewModel;
    private List<Friend> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        locationService = LocationService.singleton(this);

        CompassViewModel viewModel = new ViewModelProvider(this).get(CompassViewModel.class);

        Friend friend1 = new Friend();
        Friend friend2 = new Friend();
        friend1.setUid("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454");
        friend2.setUid("c81d4e2e-bcf2-11e6-869b-7df92533d2db");


        // add friends to the database
        /*viewModel.getDao().upsert(friend1);
        viewModel.getDao().upsert(friend2);*/

        // get all friends from the database and display them in the adapter
        friends = viewModel.getFriends();
        friends.observe(this, this::setFriends);

        //Clear local database
/*        for (Friend curr : viewModel.getDao().getAll()) {
            viewModel.getDao().delete(curr);
        }*/

        userLocation = new Pair<Double,Double>(0.0,0.0);
        
        for (Friend curr : friendsList) {
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.compass);

            TextView friend = new TextView(this);

            String name = curr.getName();
            friend.setText(name);
            curr.spot = friend;

            ConstraintLayout.LayoutParams lay = new ConstraintLayout.LayoutParams(findViewById(R.id.friend1).getLayoutParams());

            lay.circleConstraint = R.id.compass;
            lay.circleRadius = 400;
            lay.circleAngle = (float) angleCalculation(curr.getLocation());

            layout.addView(friend, lay);
        }

        this.reobserveLocation();

    }

    private void setFriends(List<Friend> friends) {
        this.friendsList = friends;
    }

    private void reobserveLocation() {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }

    private void onLocationChanged(Pair<Double, Double> latLong) {
        TextView locationText = findViewById(R.id.locationText);
        locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
        userLocation = latLong;
        //whenFriendLocationChanges();
    }

    private double angleCalculation(Pair<Double, Double> friendLocation) {
        return Math.atan2(friendLocation.second - userLocation.second, friendLocation.first - userLocation.first);
    }

    public void whenFriendLocationChanges() {
        //rad = angleCalculation(location);

        for (Friend friend : this.friends.getValue()) {
            var bestFriendLocationData1 = friend.getLocation();

            friend.setFriendRad(angleCalculation(bestFriendLocationData1));

            ConstraintLayout.LayoutParams lay = new ConstraintLayout.LayoutParams(findViewById(R.id.friend1).getLayoutParams());

            lay.circleAngle = (float) angleCalculation(friend.getLocation());
            friend.spot.setLayoutParams(lay);
            //System.out.println(friend.getName());
        }
    }

    public void submit(View view) {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.compass);

        TextView friend = new TextView(this);

        EditText inp = (EditText) findViewById(R.id.enter_uid);
        String name = inp.getText().toString();
        friend.setText(name);

        Friend newfriend = new Friend();
        newfriend.setName(name);
        this.friends.getValue().add(newfriend);
        newfriend.spot = friend;
        viewModel.getDao().upsert(newfriend);

        //friend.setId(5);
        ConstraintLayout.LayoutParams lay = new ConstraintLayout.LayoutParams(findViewById(R.id.friend1).getLayoutParams());

        lay.circleConstraint = R.id.compass;
        lay.circleRadius = 400;
        lay.circleAngle = (float) angleCalculation(newfriend.getLocation());
        //friend.setLayoutParams(linearLayout.getLayoutParams());
        layout.addView(friend, lay);

        //friend.setText("Jay");

    }
}