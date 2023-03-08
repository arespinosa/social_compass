package com.example.demo5;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationService = LocationService.singleton(this);

        this.reobserveLocation();




        //Setting the time, just testing it out
        var timeService = TimeService.singleton();
        var timeData = timeService.getTimeData();
        timeData.observe(this, this::onTimeChanged);
    }

    /**
     * Declaring the longitude and lattitude
     * TODO: Implement their distances based on the friend's location
     */
        double latitude;
        double longitude;


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
     * @param latLong : The longitude and latitude of the User
     */
    private void onLocationChanged(Pair<Double, Double> latLong) {
            updateCompassWhenLocationChanges(latLong.first, latLong.second);
            //this.updateCompassWhenLocationChanges(33.812473718140716,-117.91903852984754);
        }

    /**
     * Setting the placement of the friend at the appropriate angle within the constraints of the circle
     * @param ang
     */
    public void settingCircleAngle(int ang) {
            TextView friendtext = findViewById(R.id.best_friend);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) friendtext.getLayoutParams();

            layoutParams.circleRadius = ang;
            friendtext.setLayoutParams(layoutParams);
    }

    /**
     * This will update the display of the user interface based on the new distance of user
     * @param longitude
     * @param latitude
     */
    public void updateCompassWhenLocationChanges(Double longitude, Double latitude) {
        //Initializing the variables to find the miles
        double mile_total = distanceCalculation(longitude, latitude);
        double circle_ang;

        //Starting the placement of friend onto screen
        if(mile_total < 1) {
            //Place it onto disk 1
            circle_ang = mile_total/1.0;
            circle_ang *= 50.0;
            int ang  = (int) Math.round(circle_ang);

            settingCircleAngle(ang);
            System.out.println("we out here: disk  1");
        }
        else if(mile_total >=1 && mile_total < 10) {
            //Place it onto disk 2
            circle_ang = mile_total/10.0;
            //Space in between 50 - 100
            circle_ang *= 50;

            circle_ang = circle_ang + 65;

            int ang  = (int) Math.round(circle_ang);

            settingCircleAngle(ang);
            System.out.println("we out here: disk 2");
        }
        else if(mile_total >= 10 && mile_total < 500) {
            //Place it onto disk 3
            //490
            circle_ang = mile_total/490.0;
            circle_ang *= 100;
            circle_ang = circle_ang + 135;
            int ang  = (int) Math.round(circle_ang);

            settingCircleAngle(ang);
            System.out.println("The angle is " + ang);
            System.out.println("we out here: disk 3");
        }
        else {
            //Placing it onto disk 4

            circle_ang = mile_total/12427.0;
            circle_ang *= 200;

            circle_ang = circle_ang + 275;

            int ang  = (int) Math.round(circle_ang);

            settingCircleAngle(ang);
            System.out.println("we out here: disk 4");
        }

    }

    /**
     * Calculating the distance between user and their specific friend
     * @param longitude : Grabbing the longitude of friend
     * @param latitude : Grabbing the latitude of friend
     * @return The distance calculated
     */

    public double distanceCalculation(Double longitude, Double latitude) {
        Pair<String, String> friendLocation = retrieveFriendLocation();
        String parentLongText = friendLocation.first;
        String parentLatText = friendLocation.second;

        double friend_lat;
        double friend_long;

        double lat1;
        double lon1;
        double lat2;
        double lon2;

        try {
            friend_lat = Double.parseDouble(parentLatText);
            friend_long = Double.parseDouble(parentLongText);
        } catch (Exception e) {
            friend_lat = 0;
            friend_long = 0;
        }

        //Calculating the distance
        lat1 = Math.toRadians(friend_lat);
        lon1 = Math.toRadians(friend_long);
        lat2 = Math.toRadians(latitude);
        lon2 = Math.toRadians(longitude);

        double distance = Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*6371 * 0.621371;

        //Will return the distance in miles
        System.out.println("Mile distance = " + distance);
        return distance;
    }


    /**
     * Retrieving the location of the friend
     * @return : long and lat as a pair of strings
     */
    private Pair<String, String> retrieveFriendLocation() {
        //TODO: Step 1: Retrieving their location based on their UID

        String friendLongText = "33.804246573813415";
        String friendLatText = "-117.9106578940017";


        return new Pair<>(friendLongText, friendLatText);
    }


    /**
     * DONT DELETE
     * @param time
     */
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
    private void onTimeChanged(Long time) {}
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