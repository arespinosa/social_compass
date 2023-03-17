package com.example.demo5;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import static java.lang.Math.floor;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private LocationService locationService;
    private OrientationService orientationService;

    private Future<?> future;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    public LiveData<List<Friend>> friends;
    private CompassViewModel viewModel;
    private List<Friend> friendsList;

    boolean skip1 = false;
    public Pair<Double, Double> userLocation;
    private Distance distance;
    private GpsSignal gpsSignal;
    private ScheduledFuture<?> poller;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private LocationManager locationManager;

    public int zoomCounter;

    public ZoomFeature zoomFeature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //userLocation = new Pair<>(33.7450, -117.8872);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_compass);

        friendsList = new ArrayList<>();

        locationService = LocationService.singleton(this);

        viewModel = new ViewModelProvider(this).get(CompassViewModel.class);

        // add friends to the database
        /*viewModel.getDao().upsert(friend1);
        viewModel.getDao().upsert(friend2);*/

        // get all friends from the database and display them in the adapter
        friends = viewModel.getFriends();

        //Clear local database
        /*for (Friend curr : viewModel.getDao().getAll()) {
            viewModel.getDao().delete(curr);
        }*/

        friends.observe(this, this::setFriends);

        userLocation = new Pair<Double, Double>(0.0, 0.0);

        locationService = LocationService.singleton(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        System.out.println("LOCATION MANAGER: " + locationManager.toString());


        distance = new Distance(this);
        gpsSignal = new GpsSignal(this);
        this.reobserveLocation();

    }

    private void setFriends(List<Friend> friends) {
        boolean skip = false;

        for (Friend curr : friends) {
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.compass);

            for (Friend fri : friendsList) {
                if (fri.getName() == curr.getName()) {
                    skip = true;
                    break;
                }
            }

            if (skip)
                break;

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

        friendsList.addAll(friends);
        //default view
        zoomCounter = 2;
        zoomFeature = new ZoomFeature(this);
        zoomFeature.PerformZoom(zoomCounter, distance, userLocation);


        //Setting the time, just testing it out
        /*var timeService = TimeService.singleton();
        var timeData = timeService.getTimeData();
        timeData.observe(this, this::onTimeChanged);*/
    }


    /**
     * Reobserving the location of the user by calling onLocationChanged
     */


    public void reobserveLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        TextView gpsStatus = findViewById(R.id.gpsStatus);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        long lastUpdateTime = lastLocation.getTime();
        long currTime = System.currentTimeMillis();
        if (lastLocation != null) {
            long locationTimestamp = lastLocation.getTime();
            long currentTimestamp = System.currentTimeMillis();
            long timeDifference = currentTimestamp - locationTimestamp;
            if (timeDifference > 10000) {
                //gps Signal is initially off
                gpsStatus.setText("GPS Status: Offline");
            }
        }
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);

    }


    /**
     * If the location changes, will notify the Compass in order to place the friend at the correct
     * distance of the user
     *
     * @param latLong : The longitude and latitude of the User
     */

    public void onLocationChanged(Pair<Double, Double> latLong) {

        userLocation = latLong;
        zoomFeature.PerformZoom(zoomCounter, distance, userLocation);
        gpsSignal.updateGPSLabel(locationManager);
    }

    private double angleCalculation(Pair<Double, Double> friendLocation) {
        return Math.atan2(friendLocation.second - userLocation.second, friendLocation.first - userLocation.first);

    }


    public void submit(View view) {


        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.compass);

        TextView friend = new TextView(this);

        EditText inp = (EditText) findViewById(R.id.enter_uid);
        String name = inp.getText().toString();

        LiveData<Friend> fr = viewModel.getFriend(name);
        
        // Check if in server
        fr.observe(this, this::checkServ);
        
        if (skip1)
            return;

        friend.setText(name);

        Friend newfriend = new Friend(name);
        newfriend.setName(name);
        this.friendsList.add(newfriend);
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

    private void checkServ(Friend friend) {
        if (friend == null)
            skip1 = true;
    }

    public void onClearClick(View view) {
        assert view instanceof Button;
        Button btn = (Button) view;

        for(Friend curr : friendsList) {
            String name = curr.getName();
            System.out.println("CLEARING: " + name);
            viewModel.getDao().delete(curr);
        }
        setContentView(R.layout.activity_compass);
    }

    public void onZoomInClick(View view) {

        assert view instanceof  Button;
        Button btn  = (Button) view;

        //can be zoomed in
        if(zoomCounter != 1) {
            zoomCounter--;
            System.out.println("Clicking the zoom in feature");
            zoomFeature.PerformZoom(zoomCounter, distance, userLocation);
        }
        //cannot be zoomed in anymore
        else {
            zoomFeature.PerformZoom(zoomCounter, distance, userLocation);
        }
    }

    public void onZoomOutClick(View view) {
        assert view instanceof  Button;
        Button btn = (Button) view;

        //can be zoomed out
        if(zoomCounter != 4) {
            zoomCounter++;
            zoomFeature.PerformZoom(zoomCounter, distance, userLocation);
        }
        //cannot be zoomed out anymore
        else {
            zoomFeature.PerformZoom(zoomCounter, distance, userLocation);
        }
    }
}