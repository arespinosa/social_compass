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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TimeService timeService;
    private OrientationService orientationService;
    private LocationService locationService;

    public static final int DEGREES_IN_A_CIRCLE = 360;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = findViewById(R.id.house_labels);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.house_labels,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        orientationService = OrientationService.singleton(this);

        ConstraintLayout compass = findViewById(R.id.compass);






        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

        locationService = LocationService.singleton(this);

        ImageView parentHouse = findViewById(R.id.parentHouse);
        TextView textView = findViewById(R.id.timeTextView);
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
        loadProfile();
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}