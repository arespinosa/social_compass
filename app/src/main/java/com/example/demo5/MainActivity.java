package com.example.demo5;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private TimeService timeService;
    private OrientationService orientationService;
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**orientationService = OrientationService.singleton(this);
        TextView textView = findViewById(R.id.timeTextView);

        orientationService.getOrientation().observe(this, orientation ->{
            textView.setText(Float.toString(orientation));
        });*/
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        locationService = LocationService.singleton(this);

        TextView textView = (TextView) findViewById(R.id.timeTextView);
        locationService.getLocation().observe(this,loc->{
            textView.setText(Double.toString(loc.first)+" , "+
                    Double.toString(loc.second));
        });
    }

    /**@Override
    protected void onPause(){
        super.onPause();
        orientationService.unregisterSensorListeners();
    }*/
}