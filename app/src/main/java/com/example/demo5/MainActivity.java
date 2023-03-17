package com.example.demo5;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private LocationService locationService;
    private OrientationService orientationService;

    private Future<?> future;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    public Pair<Double, Double> userLocation = new Pair<Double,Double>(0.0,0.0);
    public List<Friend> friends;
    private CompassViewModel viewModel;
    private List<Friend> friendsList = Collections.EMPTY_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        locationService = LocationService.singleton(this);

        viewModel = new ViewModelProvider(this).get(CompassViewModel.class);

        // get all friends from the database and display them in the adapter
        friends = viewModel.getDao().getAll();

        //Clear local database
/*        for (Friend curr : viewModel.getDao().getAll()) {
            viewModel.getDao().delete(curr);
        }*/

        userLocation = new Pair<Double,Double>(0.0,0.0);

        for (Friend curr : friends) {
            System.out.println("hey");
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


    public void submit(View view) {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.compass);

        TextView friend = new TextView(this);

        EditText inp = (EditText) findViewById(R.id.enter_uid);
        String name = inp.getText().toString();
        friend.setText(name);

        Friend newfriend = new Friend(name);
        newfriend.setName(name);
        this.friends.add(newfriend);
        newfriend.spot = friend;

        System.out.println(viewModel.getDao().getAll().size());

        viewModel.getDao().upsert(newfriend);

        System.out.println(viewModel.getDao().getAll().size());

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