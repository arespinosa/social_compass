package com.example.demo5;

import android.os.Bundle;
import android.os.Handler;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

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
        friends = viewModel.getDao().getAll();

        //Clear local database
/*        for (Friend curr : viewModel.getDao().getAll()) {
            viewModel.getDao().delete(curr);
        }*/

        userLocation = new Pair<Double,Double>(0.0,0.0);

        for (Friend curr : friends) {
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

        if (future != null) {
            this.future.cancel(true);
        }
        this.future = backgroundThreadExecutor.submit(() -> {
            for (Friend friend : this.friends) {
                friend.testMove();
            }
        });

        viewModel.getDao().upsert(friend1);
        viewModel.getDao().upsert(friend2);

        FriendAdapter adapter = new FriendAdapter();
        var friends = viewModel.getFriends();
        friends.observe(this, adapter::setFriends);

        var locationData = locationService.getLocation();
        locationData.observe(this, latLong -> {
            TextView locationText = findViewById(R.id.locationText);
            locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
            userLocation = latLong;
            viewModel.getFriends().observe(this, adapter::setFriends);
        });

        Log.i("CHECK", "Make sure friends locations 0,0");
        for (Friend f : adapter.getFriends()) {
            String uid = f.getUidString();
            Log.i(f.loc.toString(), uid);
        }

        adapter.getFriends().get(0).setLocation();
        adapter.getFriends().get(1).setLocation();

        Log.i("RECHECK", "Make sure friends locations changed");
        for (Friend f : adapter.getFriends()) {
            String uid = f.getUidString();
            Log.d(f.loc.toString(), uid);
        }

        new Handler().postDelayed(() -> {
            // display the locations of the friends in the database to show that they have been updated
            Log.i("RECHECK", "Make sure friends are in database");
            for (Friend f : viewModel.getDao().getAll()) {
                String uid = f.getUidString();
                Log.i(f.loc.toString(), uid);
            }
        }, 1000);

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

        for (Friend friend : this.friends) {
            var bestFriendLocationData1 = friend.getLocation();

            friend.setFriendRad(angleCalculation(bestFriendLocationData1));

            ConstraintLayout.LayoutParams lay = new ConstraintLayout.LayoutParams(findViewById(R.id.friend1).getLayoutParams());

            lay.circleAngle = (float) angleCalculation(friend.getLocation());
            friend.spot.setLayoutParams(lay);
            //System.out.println(friend.getName());
        }


        /*for (int i = 0; i <= 1; ++i) {
            int ind = i;

            bestFriendLocationData1.observe(this, friendLocation -> {
                friends.get(ind).setFriendRad(angleCalculation(friendLocation));
            });

            if (i == 0) {
                TextView bestFriend1 = findViewById(R.id.best_friend1);
                ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams)
                        bestFriend1.getLayoutParams();
                layoutParams1.circleAngle = (float) Math.toDegrees(friends.get(i).getFriendRad());
                bestFriend1.setLayoutParams(layoutParams1);

            }
            else {
                TextView bestFriend1 = findViewById(R.id.best_friend2);
                ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams)
                        bestFriend1.getLayoutParams();
                layoutParams1.circleAngle = (float) Math.toDegrees(friends.get(i).getFriendRad());
                bestFriend1.setLayoutParams(layoutParams1);

            }
        }*/
    }

    public void submit(View view) {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.compass);

        TextView friend = new TextView(this);

        EditText inp = (EditText) findViewById(R.id.enter_uid);
        String name = inp.getText().toString();
        friend.setText(name);

        Friend newfriend = new Friend();
        newfriend.setName(name);
        this.friends.add(newfriend);
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