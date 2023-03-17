package com.example.demo5;

import android.app.Activity;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ZoomFeature extends AppCompatActivity{
    public int zoomCounter;
    public Activity activity;

    public ZoomFeature(int zoomCounter, Activity activity) {
        this.zoomCounter = zoomCounter;
        this.activity = activity;

        ImageView default_sizeC1 = this.activity.findViewById(R.id.circle_one);
        ImageView default_sizeC2 = this.activity.findViewById(R.id.circle_two);
        ImageView default_sizeC3 = this.activity.findViewById(R.id.circle_three);
        ImageView default_sizeC4 = this.activity.findViewById(R.id.circle_four);

        if(zoomCounter == 1) {
            this.zoom1(default_sizeC1, default_sizeC2, default_sizeC3, default_sizeC4);
        }
        if(zoomCounter == 2) {
            this.zoom2(default_sizeC1, default_sizeC2, default_sizeC3, default_sizeC4);
        }
        if(zoomCounter == 3) {
            this.zoom3(default_sizeC1, default_sizeC2, default_sizeC3, default_sizeC4);
        }
        if(zoomCounter == 4) {
            this.zoom4(default_sizeC1, default_sizeC2, default_sizeC3, default_sizeC4);
        }
    }

    public void zoom1(ImageView default_sizeC1, ImageView default_sizeC2, ImageView default_sizeC3, ImageView default_sizeC4) {
//        float currentScale = default_sizeC1.getScaleX();
//        default_sizeC1.setScaleX(currentScale + 15);
//        currentScale = default_sizeC1.getScaleY();
//        default_sizeC1.setScaleY(currentScale + 15);
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
