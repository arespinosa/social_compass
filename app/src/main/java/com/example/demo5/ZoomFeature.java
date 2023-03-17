package com.example.demo5;

import android.app.Activity;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

public class ZoomFeature extends AppCompatActivity{
    public int zoomCounter;
    public Activity activity;
    public Distance distance;

    public ZoomFeature(Activity activity){
        this.activity = activity;
    }

    public void PerformZoom(int zoomCounter, Distance distance, Pair<Double, Double> userLocation) {
        this.zoomCounter = zoomCounter;
        this.distance = distance;

        if(userLocation == null){
            System.out.println("NULL???");
        }
        else{
            double mile_total = distance.distanceCalculation(userLocation.first, userLocation.second);

            System.out.println("MILE TOTAL LOOKING FOR: " + mile_total);


            ImageView default_sizeC1 = this.activity.findViewById(R.id.circle_one);
            ImageView default_sizeC2 = this.activity.findViewById(R.id.circle_two);
            ImageView default_sizeC3 = this.activity.findViewById(R.id.circle_three);
            ImageView default_sizeC4 = this.activity.findViewById(R.id.circle_four);


            //If zoomCounter is 1, only one circle
            if(zoomCounter == 1) {
                System.out.println(mile_total);
                this.zoom1(default_sizeC1, default_sizeC2, default_sizeC3, default_sizeC4);
                if(mile_total < 1){
                    System.out.println("WE ARE IN ZOOM COUNTER 1!!");
                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));

                    double circle_ang;
                    //Starting the placement of friend onto screen

                    //Place it onto the singular disk
                    circle_ang = mile_total / 1.0;
                    circle_ang = mile_total * 200;
                    circle_ang = circle_ang + 275;
                    int ang = (int) Math.round(circle_ang);

                    distance.settingCircleAngle(ang);

                }
                else{
                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.INVISIBLE));
                }
            }

            //If zoomCounter is 2, only two circles

            if(zoomCounter == 2) {
                System.out.println("WE ARE IN ZOOM COUNTER 2");
                System.out.println(mile_total);
                this.zoom2(default_sizeC1, default_sizeC2, default_sizeC3, default_sizeC4);
                if (mile_total < 1) {
                    //Place it onto disk 1
                    System.out.println("We are in DISK 1");
                    double circle_ang = mile_total / 1.0;
                    circle_ang *= 100;
                    circle_ang = circle_ang + 135;
                    int ang = (int) Math.round(circle_ang);

                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));


                    distance.settingCircleAngle(ang);
                } else if (mile_total >= 1 && mile_total < 10) {
                    //System.out.println("Clicking the zoom in feature");
                    System.out.println("We are in DISK 2");
                    //Place it onto disk 2
                    double circle_ang = mile_total / 10.0;
                    //Space in between 50 - 100
                    circle_ang *= 200;

                    circle_ang = circle_ang + 275;

                    int ang = (int) Math.round(circle_ang);

                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));

                    distance.settingCircleAngle(ang);
                }
                //TODO: Might have to iterate through all the friends and make them INVISIBLE
                else{
                    System.out.println("SETTING BOB TO INVISIBLE ");
                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.INVISIBLE));
                }


            }

            //This is when ZoomCounter is equal to 3
            if(zoomCounter == 3) {

                System.out.println("WE ARE IN ZOOM COUNTER 3");
                this.zoom3(default_sizeC1, default_sizeC2, default_sizeC3, default_sizeC4);
                if (mile_total < 1) {
                    //Place it onto disk 1
                    double circle_ang = mile_total / 1.0;
                    circle_ang *= 50.0;
                    circle_ang = circle_ang + 65;
                    int ang = (int) Math.round(circle_ang);


                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));

                    distance.settingCircleAngle(ang);



                } else if (mile_total >= 1 && mile_total < 10) {
                    //Place it onto disk 2
                    double circle_ang = mile_total / 10.0;
                    //Space in between 50 - 100
                    circle_ang *= 100;

                    circle_ang = circle_ang + 135;

                    int ang = (int) Math.round(circle_ang);

                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));

                    distance.settingCircleAngle(ang);
                } else if (mile_total >= 10 && mile_total < 500) {
                    System.out.println("We are in disk 3, so this is where it should be visisble");
                    //Place it onto disk 3
                    //490
                    double circle_ang = mile_total / 490.0;
                    circle_ang *= 200;
                    circle_ang = circle_ang + 310;
                    int ang = (int) Math.round(circle_ang);

                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));

                    distance.settingCircleAngle(ang);
                }

                else{
                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.INVISIBLE));
                }
            }

            //This is the original count
            if(zoomCounter == 4) {
                this.zoom4(default_sizeC1, default_sizeC2, default_sizeC3, default_sizeC4);
                if (mile_total < 1) {
                    //Place it onto disk 1
                    double circle_ang = mile_total / 1.0;
                    circle_ang *= 50.0;
                    int ang = (int) Math.round(circle_ang);

                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));

                    distance.settingCircleAngle(ang);
                } else if (mile_total >= 1 && mile_total < 10) {
                    //Place it onto disk 2
                    double circle_ang = mile_total / 10.0;
                    //Space in between 50 - 100
                    circle_ang *= 50;

                    circle_ang = circle_ang + 65;

                    int ang = (int) Math.round(circle_ang);

                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));

                    distance.settingCircleAngle(ang);
                } else if (mile_total >= 10 && mile_total < 500) {
                    //Place it onto disk 3
                    //490
                    double circle_ang = mile_total / 490.0;
                    circle_ang *= 100;
                    circle_ang = circle_ang + 135;
                    int ang = (int) Math.round(circle_ang);

                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));

                    distance.settingCircleAngle(ang);
                } else {
                    //Placing it onto disk 4

                    double circle_ang = mile_total / 12427.0;
                    circle_ang *= 200;

                    circle_ang = circle_ang + 275;

                    int ang = (int) Math.round(circle_ang);

                    TextView BOB = this.activity.findViewById(R.id.best_friend);
                    runOnUiThread(() -> BOB.setVisibility(TextView.VISIBLE));

                    distance.settingCircleAngle(ang);
                }
            }
        }
    }








    public void zoom1(ImageView default_sizeC1, ImageView default_sizeC2, ImageView default_sizeC3, ImageView default_sizeC4) {

        default_sizeC1.setScaleX(8.0f);
        default_sizeC1.setScaleY(8.0f);

        default_sizeC2.setScaleX(0.0f);
        default_sizeC2.setScaleY(0.0f);

        default_sizeC3.setScaleX(0.0f);
        default_sizeC4.setScaleY(0.0f);

        default_sizeC4.setScaleX(0.0f);
        default_sizeC4.setScaleY(0.0f);
    }

    public void zoom2(ImageView default_sizeC1, ImageView default_sizeC2, ImageView default_sizeC3, ImageView default_sizeC4) {
        default_sizeC1.setScaleX(4.0f);
        default_sizeC1.setScaleY(4.0f);

        default_sizeC2.setScaleX(4.0f);
        default_sizeC2.setScaleY(4.0f);

        default_sizeC3.setScaleX(0.0f);
        default_sizeC4.setScaleY(0.0f);

        default_sizeC4.setScaleX(0.0f);
        default_sizeC4.setScaleY(0.0f);
    }

    public void zoom3(ImageView default_sizeC1, ImageView default_sizeC2, ImageView default_sizeC3, ImageView default_sizeC4){
        default_sizeC1.setScaleX(2.0f);
        default_sizeC1.setScaleY(2.0f);

        default_sizeC2.setScaleX(2.0f);
        default_sizeC2.setScaleY(2.0f);

        default_sizeC3.setScaleX(2.0f);
        default_sizeC3.setScaleY(2.0f);

        default_sizeC4.setScaleX(0.0f);
        default_sizeC4.setScaleY(0.0f);
    }

    public void zoom4(ImageView default_sizeC1, ImageView default_sizeC2, ImageView default_sizeC3, ImageView default_sizeC4) {
        default_sizeC1.setScaleX(1.0f);
        default_sizeC1.setScaleY(1.0f);

        default_sizeC2.setScaleX(1.0f);
        default_sizeC2.setScaleY(1.0f);

        default_sizeC3.setScaleX(1.0f);
        default_sizeC3.setScaleY(1.0f);

        default_sizeC4.setScaleX(1.0f);
        default_sizeC4.setScaleY(1.0f);
    }
}
