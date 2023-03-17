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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import static java.lang.Math.floor;

import org.w3c.dom.Text;

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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        System.out.println("LOCATION MANAGER: " + locationManager.toString());


        distance = new Distance(this);
        gpsSignal = new GpsSignal(this);
        this.reobserveLocation();

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
        //this.updateGPSLabel();
        //distance.updateCompassWhenLocationChanges(latLong.first, latLong.second);

        userLocation = latLong;
        whenFriendLocationChanges();
        zoomFeature.PerformZoom(zoomCounter, distance, userLocation);
        //distance.updateCompassWhenLocationChanges(latLong.first, latLong.second);
        gpsSignal.updateGPSLabel(locationManager);
    }

    public void whenFriendLocationChanges() {
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

