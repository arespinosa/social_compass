package com.example.demo5;

//import android.util.Pair;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;

public class Distance extends AppCompatActivity {

    /**
     * Declaring the longitude and lattitude
     * TODO: Implement their distances based on the friend's location
     */
    double latitude;
    double longitude;
    private LocationService locationService;

    public Distance(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void reobserveLocation(LocationService locationService) {
        var locationData = locationService.getLocation();
        locationData.observe(this, this::onLocationChanged);
    }



    private void onLocationChanged(Pair<Double, Double> latLong) {
        updateCompassWhenLocationChanges(latLong.first, latLong.second);
    }

    public void settingCircleAngle(int ang) {
        TextView friendtext = findViewById(R.id.best_friend);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) friendtext.getLayoutParams();

        layoutParams.circleRadius = ang;
        friendtext.setLayoutParams(layoutParams);


    }

    public void updateCompassWhenLocationChanges(Double longitude, Double latitude) {
        //Initializing the variables to find the miles
        //Math.acos(Math.sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon2-lon1))*6371

        //TODO: bro the calculations are a lil off...

        double distance = distanceCalculation(longitude, latitude);

        double mile_total = distance * 69.4;


        //Starting the placement of friend onto screen
        if(mile_total < 1) {
            //Place it onto disk 1
            settingCircleAngle(45);
        }
        else if(mile_total >=1 && mile_total < 10) {
            //Place it onto disk 2
            settingCircleAngle(95);
        }
        else if(mile_total >= 10 && mile_total < 500) {
            //Place it onto disk 3
            settingCircleAngle(195);
        }
        else {
            //Placing it onto disk 4
            settingCircleAngle(355);
        }

    }

    public double distanceCalculation(Double longitude, Double latitude) {
        Pair<String, String> friendLocation = retrieveFriendLocation();
        String parentLongText = friendLocation.first;
        String parentLatText = friendLocation.second;

        double friend_lat;
        double friend_long;

        try {
            friend_lat = Double.parseDouble(parentLatText);
            friend_long = Double.parseDouble(parentLongText);
        } catch (Exception e) {
            friend_lat = 0;
            friend_long = 0;
        }

        //Calculating the distance
        double x = (friend_long - longitude) * (friend_long - longitude);
        double y = (friend_lat - latitude) * (friend_lat - latitude);
        double distance = Math.sqrt(x+y);

        return distance;
    }


    /**
     * Retrieving the location of the friend
     * @return : long and lat as a pair of strings
     */
    private Pair<String, String> retrieveFriendLocation() {
        //TODO: Step 1: Retrieving their location based on their UID

        String friendLatText = "90";
        String friendLongText = "0";

        return new Pair<>(friendLongText, friendLatText);
    }

}

