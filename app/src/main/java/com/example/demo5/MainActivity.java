package com.example.demo5;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;

public class MainActivity extends AppCompatActivity {

    private static final int DEGREES_IN_A_CIRCLE = 360;
    private static final String P_LAT_STRING = "parentLatitude";
    private static final String P_LONG_STRING = "parentLongitude";
    private static final String ZERO_STRING = "0";
    private static final String BLANK_STRING = "";
    private static final String HOUSE_LABEL_STRING = "houseLabel";
    private LocationService locationService;
    private OrientationService orientationService;
    public ArrayList<BestFriend> bestFriends;

    private Future<?> future;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    public Pair<Double, Double> userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        locationService = LocationService.singleton(this);
        bestFriends = new ArrayList<BestFriend>(2);

        if (future != null) {
            this.future.cancel(true);
        }
        this.future = backgroundThreadExecutor.submit(() -> {
            bestFriends.get(0).testMove();
            bestFriends.get(1).testMove();
        });

        this.reobserveLocation();
    }

    private void reobserveLocation() {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }

    private void onLocationChanged(Pair<Double, Double> latLong) {
        TextView locationText = findViewById(R.id.locationText);
        locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
        userLocation = latLong;
        whenFriendLocationChanges(bestFriends);
    }

    public void whenFriendLocationChanges(ArrayList<BestFriend> friends) {
        //rad = angleCalculation(location);
        var bestFriendLocationData1 = bestFriends.get(0).getLocation();
        var bestFriendLocationData2 = bestFriends.get(1).getLocation();

        bestFriendLocationData1.observe(this, friendLocation -> {
            friend.setBestFriendRad(Math.atan2(friendLocation.second - userLocation.second, friendLocation.first - userLocation.first));
        });

        TextView bestFriend = findViewById(R.id.best_friend);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                bestFriend.getLayoutParams();
        layoutParams.circleAngle = (float) Math.toDegrees(friend.getBestFriendRad());
        bestFriend.setLayoutParams(layoutParams);
    }



    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        locationService = LocationService.singleton(this);
//        this.reobserveLocation();
//
//        orientationService = OrientationService.singleton(this);
//        this.reobserveOrientation();
//
//        var timeService = TimeService.singleton();
//        var timeData = timeService.getTimeData();
//        timeData.observe(this, this::onTimeChanged);
//
//        loadProfile();
//    }

//    private void reobserveLocation() {
//        var locationData = locationService.getLocation();
//        locationData.observe(this, this::onLocationChanged);
//    }
//
//    public void reobserveOrientation() {
//        var orientationData = orientationService.getOrientation();
//        orientationData.observe(this, this::onOrientationChanged);
//    }
//
//    private void onLocationChanged(Pair<Double, Double> latLong) {
//        TextView locationText = findViewById(R.id.locationText);
//        locationText.setText(Utilities.formatLocation(latLong.first, latLong.second));
//        updateCompassWhenLocationChanges(latLong.first, latLong.second);
//    }
//
//    private void onOrientationChanged(Float orientation) {
//        updateCompassWhenOrientationChanges(orientation);
//    }
//
//    public void updateCompassWhenLocationChanges(Double latitude, Double longitude) {
//        double ang = angleCalculation(latitude, longitude);
//
//        //US-2b
//        String newLabel = retrieveParentLabel();
//        updateParentHouseLabel(newLabel);
//        updateParentHouseLabel(ang);
//
//        updateParentHouse(ang);
//    }
//
//    public void updateCompassWhenOrientationChanges(Float orientation) {
//        float deg = (float) Math.toDegrees(orientation);
//        ConstraintLayout compass = findViewById(R.id.compass);
//
//        //US-10
//        float new_angle = retrieveManualOrientation();
//
//        compass.setRotation(DEGREES_IN_A_CIRCLE - new_angle - deg);
//    }
//
//    public float retrieveManualOrientation() {
//        TextView orientationLabelView = findViewById(R.id.orientation_text);
//        String orientationLabelText = orientationLabelView.getText().toString();
//        Float orientationLabelValue;
//
//        try {
//            orientationLabelValue = Float.parseFloat(orientationLabelText);
//        } catch (Exception e) {
//            orientationLabelValue = Float.valueOf(0);
//
//        }
//
//        if (orientationLabelValue > DEGREES_IN_A_CIRCLE - 1) {
//            orientationLabelValue = Float.valueOf(DEGREES_IN_A_CIRCLE - 1);
//            orientationLabelView.setText(BLANK_STRING);
//        } else if (orientationLabelValue < 0) {
//            orientationLabelValue = Float.valueOf(0);
//            orientationLabelView.setText(BLANK_STRING);
//        }
//
//        return orientationLabelValue;
//    }
//
//    public double angleCalculation(Double latitude, Double longitude) {
//        Pair<String, String> parentLocation = retrieveParentLocation();
//        String parentLatText = parentLocation.first;
//        String parentLongText = parentLocation.second;
//
//        double plat;
//        double plong;
//
//        try {
//            plat = Double.parseDouble(parentLatText);
//            plong = Double.parseDouble(parentLongText);
//        } catch (Exception e) {
//            plat = 0;
//            plong = 0;
//        }
//
//        return Math.atan2(plong - longitude, plat - latitude);
//    }
//
//    private void updateParentHouseLabel(String newLabel) {
//        TextView label = findViewById(R.id.labelTextView);
//        label.setText(newLabel);
//    }
//
//    private void updateParentHouseLabel(double ang) {
//        TextView label = findViewById(R.id.labelTextView);
//        ConstraintLayout.LayoutParams houseLabelParam = (ConstraintLayout.LayoutParams)
//                                                         label.getLayoutParams();
//        houseLabelParam.circleAngle = (float) Math.toDegrees(ang);
//        label.setLayoutParams(houseLabelParam);
//    }
//
//    private void updateParentHouse(double ang) {
//        ImageView parentHouse = findViewById(R.id.parentHouse);
//        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
//                                                      parentHouse.getLayoutParams();
//        layoutParams.circleAngle = (float) Math.toDegrees(ang);
//        parentHouse.setLayoutParams(layoutParams);
//    }
//
//    private Pair<String, String> retrieveParentLocation() {
//        TextView parentLatView = findViewById(R.id.parentLatitude);
//        TextView parentLongView = findViewById(R.id.parentLongitude);
//
//        String parentLatText = parentLatView.getText().toString();
//        String parentLongText = parentLongView.getText().toString();
//
//        if (parentLatText.equals(BLANK_STRING)) {
//            parentLatText = ZERO_STRING;
//        }
//        if (parentLongText.equals(BLANK_STRING)) {
//            parentLongText = ZERO_STRING;
//        }
//
//        savePLatAndPLong(parentLatText, parentLongText);
//
//        return new Pair<>(parentLatText, parentLongText);
//    }
//
//    private String retrieveParentLabel() {
//        TextView parentLabelView = findViewById(R.id.house_label);
//        String parentLabelText = parentLabelView.getText().toString();
//        savePLabel(parentLabelText);
//        return parentLabelText;
//    }
//
//    private void savePLatAndPLong(String parentLatText, String parentLongText) {
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(P_LAT_STRING, parentLatText);
//        editor.putString(P_LONG_STRING, parentLongText);
//        editor.apply();
//    }
//
//    private void savePLabel(String parentLabelText) {
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(HOUSE_LABEL_STRING, parentLabelText);
//        editor.apply();
//    }
//
//    private void onTimeChanged(Long time) {}
//
//    public void loadProfile() {
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        String s = preferences.getString(P_LAT_STRING, ZERO_STRING);
//        String t = preferences.getString(P_LONG_STRING, ZERO_STRING);
//        TextView parentLatitude = findViewById(R.id.parentLatitude);
//        TextView parentLongitude = findViewById(R.id.parentLongitude);
//        parentLatitude.setText(s);
//        parentLongitude.setText(t);
//
//        // US-2b
//        String u = preferences.getString(HOUSE_LABEL_STRING, BLANK_STRING);
//        TextView parentLabel = findViewById(R.id.labelTextView);
//        TextView parentLabelField = findViewById(R.id.house_label);
//        parentLabel.setText(u);
//        parentLabelField.setText(u);
//    }
}