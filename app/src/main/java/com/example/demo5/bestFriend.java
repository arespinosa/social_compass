package com.example.demo5;

public class bestFriend {
    double latitude;
    double longitude;

    bestFriend() {
        latitude = 0;
        longitude = 0;
        testMove();
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

   public void testMove() {
        for (int i = 0; i < 10; ++i) {
            latitude++;
            longitude++;

            try {
                Thread.sleep(10000);
            } catch (Exception e) {

            }
        }
    }
}
