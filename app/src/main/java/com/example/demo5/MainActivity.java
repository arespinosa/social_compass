package com.example.demo5;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

public class MainActivity extends AppCompatActivity {
    private TimeService timeService;
    private OrientationService orientationService;
    private LocationService locationService;
    private boolean requestingLocationUpdates = false;
    public static final int DEGREES_IN_A_CIRCLE = 360;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        orientationService = OrientationService.singleton(this);

        ConstraintLayout compass = findViewById(R.id.compass);



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            requestingLocationUpdates = true;
        }

        locationService = LocationService.singleton(this);

        ImageView parentHouse = findViewById(R.id.parentHouse);
        TextView textView = findViewById(R.id.timeTextView);
       // final double new_angle=0.0;

        //US-1 & 2b
        locationService.getLocation().observe(this, loc -> {


            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            Double pLat = Double.parseDouble(preferences.getString("parentLatitude", "123"));
            Double pLong = Double.parseDouble(preferences.getString("parentLongitude", "123"));

//            pLat = Math.toRadians(pLat);
//            pLong = Math.toRadians(pLong);
//
//
//            double dLon = (pLong - Math.toRadians(loc.second));
//
//            double y = Math.sin(dLon) * Math.cos(pLat);
//            double x = Math.cos(Math.toRadians(loc.first)) * Math.sin(pLat) - Math.sin(Math.toRadians(loc.first))
//                    * Math.cos(pLat) * Math.cos(dLon);
//
//            double brng = Math.atan2(y, x);
//
//            brng = Math.toDegrees(brng);
//            brng = (brng + 360) % 360;
//            brng = 360 - brng; // count degrees counter-clockwise - remove to make clockwise

            // return brng;
            double adjacent = (loc.second-pLong);
            double hypotenuse = Math.sqrt(((pLat - loc.first) * (pLat - loc.first)) + ((pLong - loc.second) * (pLong - loc.second)));

            double ang = Math.acos(adjacent / hypotenuse);
            //textView.setText(Double.toString(Math.toDegrees(ang)));
            textView.setText(Double.toString(ang));




            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) parentHouse.getLayoutParams();
            layoutParams.circleAngle = 360 - (float) Math.toDegrees(ang);


            //US-2b
            TextView label = findViewById(R.id.labelTextView);
            ConstraintLayout.LayoutParams houseLabelParam = (ConstraintLayout.LayoutParams) label.getLayoutParams();
            houseLabelParam.circleAngle = 360 - (float)Math.toDegrees(ang);

            //US-10
            float new_angle = Float.parseFloat(preferences.getString("orientationLabel", "123"));
            orientationService.getOrientation().observe(this, orientation -> {
                float deg = (float) Math.toDegrees(orientation);

                compass.setRotation(DEGREES_IN_A_CIRCLE - new_angle - deg);
            });


        });
        loadProfile();

//        orientationService.getOrientation().observe(this, orientation -> {
//            float deg = (float) Math.toDegrees(orientation);
//
//            compass.setRotation(DEGREES_IN_A_CIRCLE - new_angle - deg);
//        });


    }

    protected void onPause(Bundle savedInstanceState) {
        super.onPause();
        orientationService.unregisterSensorListeners();
        //onStop(savedInstanceState);
    }

    /*
        protected void onStop(Bundle savedInstanceState) {
            super.onStop();
            onCreate(savedInstanceState);
        }

        @Override
        protected void onResume() {
            super.onResume();
            if (requestingLocationUpdates) {
                startLocationUpdates();
            }
        }

        private void startLocationUpdates() {
            //fusedLocationClient.requestLocationUpdates(locationRequest,
            //        locationCallback,
            //        Looper.getMainLooper());
        }
    */
    public void loadProfile() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        //US-1
        String s = preferences.getString("parentLatitude", "123");
        String t = preferences.getString("parentLongitude", "123");
        TextView parentLatitude = findViewById(R.id.parentLatitude);
        TextView parentLongitude = findViewById(R.id.parentLongitude);
        parentLatitude.setText(s);
        parentLongitude.setText(t);
        System.out.println(preferences.getAll().toString());


        //US-2b house labels
        String label = preferences.getString("houseLabel","");
        TextView houseLabel = findViewById(R.id.house_label);
        houseLabel.setText(label);

        //Creating the label - 2b
        TextView labelText = findViewById(R.id.labelTextView);
        labelText.setText(houseLabel.getText());

        //US-10
        String orientation = preferences.getString("orientationLabel","");
        TextView orientationLabel = findViewById(R.id.orientation_text);
        orientationLabel.setText(orientation);


    }
    public void saveProfile() {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //US-1
        TextView parentLatitude = findViewById(R.id.parentLatitude);
        editor.putString("parentLatitude", parentLatitude.getText().toString());
        TextView parentLongitude = findViewById(R.id.parentLongitude);
        editor.putString("parentLongitude", parentLongitude.getText().toString());

        //US-2b house labels
        TextView houseLabel = findViewById(R.id.house_label);
        editor.putString("houseLabel", houseLabel.getText().toString());

        //US-10 orientation labels
        TextView orientationLabel = findViewById(R.id.orientation_text);
        editor.putString("orientationLabel", orientationLabel.getText().toString());


        editor.apply();
    }

    public void save(View view) {
        saveProfile();
        loadProfile();
        //onPause(savedInstanceState);


        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }


}