package com.example.demo5;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

public class MainActivity extends AppCompatActivity {
    private TimeService timeService;
    private OrientationService orientationService;
    private LocationService locationService;

    public static final int DEGREES_IN_A_CIRCLE = 360;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        orientationService = OrientationService.singleton(this);

        ConstraintLayout compass = findViewById(R.id.compass);


        orientationService.getOrientation().observe(this, orientation ->{
            float deg = (float) Math.toDegrees(orientation);
            compass.setRotation(DEGREES_IN_A_CIRCLE - deg);
        });



        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        locationService = LocationService.singleton(this);

        TextView textView = findViewById(R.id.timeTextView);
        locationService.getLocation().observe(this,loc->{
            textView.setText(Double.toString(loc.first)+" , "+
                    Double.toString(loc.second));
        });
        loadProfile();
    }

    @Override
    protected void onPause(){
        super.onPause();
        orientationService.unregisterSensorListeners();
    }

    public void loadProfile () {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String s = preferences.getString("parentLatitude", "wrong");
        String t = preferences.getString("parentLongitude", "wrong 2");
        TextView parentLatitude = findViewById(R.id.parentLatitude);
        TextView parentLongitude = findViewById(R.id.parentLongitude);
        parentLatitude.setText(s);
        parentLongitude.setText(t);
        System.out.println(preferences.getAll().toString());
    }

    public void saveProfile () {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        TextView parentLatitude = findViewById(R.id.parentLatitude);
        editor.putString("parentLatitude", parentLatitude.getText().toString());
        TextView parentLongitude = findViewById(R.id.parentLongitude);
        editor.putString("parentLongitude", parentLongitude.getText().toString());
        editor.apply();
    }

    public void save(View view) {
        saveProfile();
    }
}