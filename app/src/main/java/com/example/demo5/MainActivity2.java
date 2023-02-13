package com.example.demo5;

import android.app.Activity;
import android.content.Intent;
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

public class MainActivity2 extends AppCompatActivity {
    private TimeService timeService;
    private OrientationService orientationService;
    private LocationService locationService;

    public static final int DEGREES_IN_A_CIRCLE = 360;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        orientationService = OrientationService.singleton(this);

        ConstraintLayout compass = findViewById(R.id.compass2);






        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        locationService = LocationService.singleton(this);

        ImageView parentHouse = findViewById(R.id.parentHouse2);
        TextView textView = findViewById(R.id.timeTextView2);
        locationService.getLocation().observe(this,loc->{
            textView.setText(Double.toString(loc.first)+" , "+
                    Double.toString(loc.second));

            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            Double pLat =  Double.parseDouble(preferences.getString("parentLatitude", "123"));
            Double pLong = Double.parseDouble(preferences.getString("parentLongitude", "123"));

            double adjacent = (pLong - loc.second);
            double hypotenuse = Math.sqrt(((pLat - loc.first)*(pLat - loc.first)) + ((pLong - loc.second)*(pLong - loc.second)));

            double ang = Math.acos(adjacent/hypotenuse);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) parentHouse.getLayoutParams();
            layoutParams.circleAngle = DEGREES_IN_A_CIRCLE - (float)Math.toDegrees(ang);
        });

        loadProfile();

        orientationService.getOrientation().observe(this, orientation ->{
            float deg = (float) Math.toDegrees(orientation);
            compass.setRotation(DEGREES_IN_A_CIRCLE - deg);
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        orientationService.unregisterSensorListeners();
    }

    public void loadProfile () {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String s = preferences.getString("parentLatitude", "123");
        String t = preferences.getString("parentLongitude", "123");
        TextView parentLatitude = findViewById(R.id.parentLatitude2);
        TextView parentLongitude = findViewById(R.id.parentLongitude2);
        parentLatitude.setText(s);
        parentLongitude.setText(t);
        System.out.println(preferences.getAll().toString());
    }

    public void saveProfile () {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        TextView parentLatitude = findViewById(R.id.parentLatitude2);
        editor.putString("parentLatitude", parentLatitude.getText().toString());
        TextView parentLongitude = findViewById(R.id.parentLongitude2);
        editor.putString("parentLongitude", parentLongitude.getText().toString());
        editor.apply();
    }

    public void save(View view) {
        saveProfile();
        loadProfile();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}