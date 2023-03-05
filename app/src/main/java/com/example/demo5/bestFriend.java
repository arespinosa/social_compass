package com.example.demo5;

public class bestFriend {
    long latitude;
    long longitude;

    bestFriend() {
        latitude = 0;
        longitude = 0;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

/*    public void testMove() throws InterruptedException {
        for (int i = 0; i < 10; ++i) {
            latitude++;
            longitude++;

            Thread.sleep(10000);
        }
    }*/
}
