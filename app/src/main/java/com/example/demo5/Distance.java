package com.example.demo5;

//import android.util.Pair;
import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;


public class Distance extends AppCompatActivity {

    public Activity activity;


    /**
     * Declaring the longitude and lattitude
     * TODO: Implement their distances based on the friend's location
     */
    double latitude;
    double longitude;
    private LocationService locationService;

    public Distance(Activity activity){
        this.activity = activity;

    }

//    public void reobserveLocation(LocationService locationService) {
//        var locationData = locationService.getLocation();
//        locationData.observe(this, this::onLocationChanged);
//    }



    private void onLocationChanged(Pair<Double, Double> latLong) {
        updateCompassWhenLocationChanges(latLong.first, latLong.second);
    }

    public void settingCircleAngle(int ang) {
        TextView friendtext = this.activity.findViewById(R.id.best_friend);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) friendtext.getLayoutParams();

        layoutParams.circleRadius = ang;
        friendtext.setLayoutParams(layoutParams);


    }

    public void updateCompassWhenLocationChanges(Double longitude, Double latitude) {


        //Initializing the variables to find the miles

        //Initializing the variables to find the miles
        double mile_total = distanceCalculation(longitude, latitude);
        double circle_ang;

        //Starting the placement of friend onto screen
        if (mile_total < 1) {
            //Place it onto disk 1
            circle_ang = mile_total / 1.0;
            circle_ang *= 50.0;
            int ang = (int) Math.round(circle_ang);

            settingCircleAngle(ang);
        } else if (mile_total >= 1 && mile_total < 10) {
            //Place it onto disk 2
            circle_ang = mile_total / 10.0;
            //Space in between 50 - 100
            circle_ang *= 50;

            circle_ang = circle_ang + 65;

            int ang = (int) Math.round(circle_ang);

            settingCircleAngle(ang);
        } else if (mile_total >= 10 && mile_total < 500) {
            //Place it onto disk 3
            //490
            circle_ang = mile_total / 490.0;
            circle_ang *= 100;
            circle_ang = circle_ang + 135;
            int ang = (int) Math.round(circle_ang);

            settingCircleAngle(ang);
        } else {
            //Placing it onto disk 4

            circle_ang = mile_total / 12427.0;
            circle_ang *= 200;

            circle_ang = circle_ang + 275;

            int ang = (int) Math.round(circle_ang);

            settingCircleAngle(ang);
        }
    }
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

        double distance = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * 6371 * 0.621371;

        //Will return the distance in miles
        //System.out.println("Mile distance = " + distance);
        return distance;
    }


    /**
     * Retrieving the location of the friend
     * @return : long and lat as a pair of strings
     */
    public Pair<String, String> retrieveFriendLocation() {
        //TODO: Step 1: Retrieving their location based on their UID

        String friendLongText = "33.804246573813415";
        String friendLatText = "-117.9106578940017";

        return new Pair<>(friendLongText, friendLatText);
    }

}

