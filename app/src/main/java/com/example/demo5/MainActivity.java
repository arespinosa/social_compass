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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;

public class MainActivity extends AppCompatActivity {

    private static final int DEGREES_IN_A_CIRCLE = 360;
    private static final String P_LAT_STRING = "parentLatitude";
    private static final String P_LONG_STRING = "parentLongitude";
    private static final String ZERO_STRING = "0";
    private static final String BLANK_STRING = "";
    private static final String HOUSE_LABEL_STRING = "houseLabel";
    private LocationService locationService;
    private OrientationService orientationService;
    public BestFriend bestFriend;
    private double bestFriendRad;
    private Future<?> future;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    public Pair<Double, Double> userLocation;
    private Distance distance;
    private ScheduledFuture<?> poller;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setContentView(R.layout.activity_compass);

        locationService = LocationService.singleton(this);

        bestFriend = new BestFriend();

        if (future != null) {
            this.future.cancel(true);
        }
        this.future = backgroundThreadExecutor.submit(() -> {
            bestFriend.testMove2();
        });

        distance = new Distance(this);
        this.reobserveLocation();



        //Setting the time, just testing it out
        /*var timeService = TimeService.singleton();
        var timeData = timeService.getTimeData();
        timeData.observe(this, this::onTimeChanged);*/
    }


    /**
     * Reobserving the location of the user by calling onLocationChanged
     */


    public void reobserveLocation() {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }



    /**
     * If the location changes, will notify the Compass in order to place the friend at the correct
     * distance of the user
     *
     * @param latLong : The longitude and latitude of the User
     */

    private void onLocationChanged(Pair<Double, Double> latLong) {
        this.updateGPSLabel();
        distance.updateCompassWhenLocationChanges(latLong.first, latLong.second);
        //TextView locationText = findViewById(R.id.locationText);
        //locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
        userLocation = latLong;
        whenFriendLocationChanges();
        distance.updateCompassWhenLocationChanges(latLong.first, latLong.second);
        //this.updateCompassWhenLocationChanges(33.812473718140716,-117.91903852984754);
    }


    public void whenFriendLocationChanges() {
        //rad = angleCalculation(location);
        var bestFriendLocationData = bestFriend.getLocation();
        bestFriendLocationData.observe(this, this::angleCalculation);
        updateFriendDirection();
    }

    public void updateFriendDirection() {
        TextView bestFriend = findViewById(R.id.best_friend);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                bestFriend.getLayoutParams();
        layoutParams.circleAngle = (float) Math.toDegrees(bestFriendRad);
        bestFriend.setLayoutParams(layoutParams);
    }


    public void angleCalculation(Pair<Double, Double> friendLocation) {
        //returns in radians
        //rad = Math.atan2(bestFriend.getLongitude() - userLocation.second, bestFriend.getLatitude() - userLocation.first);
        bestFriendRad = Math.atan2(friendLocation.second - userLocation.second, friendLocation.first - userLocation.first);
    }


    private void updateGPSLabel(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        TextView gpsStatus = findViewById(R.id.gpsSignal);
        if (this.poller != null && !this.poller.isCancelled()) {
            poller.cancel(true);
        }
        //ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        poller=executor.scheduleAtFixedRate(() -> {
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                long lastUpdateTime = lastLocation.getTime();
                long currentTime = System.currentTimeMillis();
                long timeSinceLastUpdate = currentTime - lastUpdateTime;
                //gpsStatus.setText("offline");
                System.out.println(timeSinceLastUpdate);
                // Check if it's been more than a minute since the last GPS signal was received
                if (timeSinceLastUpdate > 10000) {
                    //System.out.println("10 seconds");
                    gpsStatus.setVisibility(TextView.VISIBLE);
                }
                else{
                    gpsStatus.setVisibility(TextView.INVISIBLE);
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }



}