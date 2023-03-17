package com.example.demo5;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class GpsSignal extends AppCompatActivity {
    private ScheduledFuture<?> poller;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    public Activity activity;

    /**
     * Creating the constructor for GpsSignal
     * @param activity
     */
    public GpsSignal(Activity activity){
        this.activity = activity;
    }

    public void updateGPSLabel(LocationManager locationManager){
        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        TextView gpsStatus = this.activity.findViewById(R.id.gpsStatus);
        TextView gpsTime = this.activity.findViewById(R.id.timeSinceLastUpdated);

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


                System.out.println(timeSinceLastUpdate);
                // Check if it's been more than a minute since the last GPS signal was received
                if (timeSinceLastUpdate > 10000) {
                    //offline
                    runOnUiThread(()-> gpsStatus.setText("GPS Status: Offline"));
                    runOnUiThread(() -> gpsTime.setVisibility(TextView.VISIBLE));
                    runOnUiThread(() -> gpsTime.setText(timeSinceGPSLastUpdated(timeSinceLastUpdate)));
                }
                else{
                    //online
                    runOnUiThread(()-> gpsStatus.setText("GPS Status: Online"));
                    runOnUiThread(() -> gpsTime.setVisibility(TextView.INVISIBLE));
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public String timeSinceGPSLastUpdated(long lastUpdateTime) {
        System.out.println(Math.floor(lastUpdateTime));
        TextView gpsTime = this.activity.findViewById(R.id.timeSinceLastUpdated);
        String time = String.valueOf(lastUpdateTime);
        double milli = Double.parseDouble(time);
        double seconds = (int)(milli/1000);
        double minutes = seconds/60;
        seconds = seconds % 60;
        double hours = minutes / 60;
        minutes = minutes % 60;

        Math.floor(seconds);
        Math.floor(minutes);
        Math.floor(hours);

        String secondsString = String.valueOf((int) seconds);
        String minutesString = String.valueOf((int) minutes);
        String hoursString = String.valueOf((int) hours);

        if(hours >= 1.0) {
            return "Time since last updated: " +
                    hoursString + " hours";
        }
        if(minutes >= 1.0) {
            return "Time since last updated: " +
                    minutesString + " minutes";
        }
        else {
            return "Time since last updated: " +
                    secondsString + " seconds";
        }
    }
}
