package com.example.demo5;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class OrientationService implements SensorEventListener {
    private static OrientationService instance;

    private final SensorManager sensorManager;
    private float[] accelerometerReading;
    private float[] magnetometerReading;
    private MutableLiveData<Float> azimuth;

    /**
     * constructor for orientationservice
     * @param activity context needed to initiate sensormanager
     */
    protected OrientationService (Activity activity){
        this.azimuth = new MutableLiveData<>();
        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        //register sensor listeners
        this.registerSensorListeners();
    }

    private void registerSensorListeners() {
        //register our listener to accelerometer and magnetometer
        //need both pieces of data to compute orientation
        sensorManager.registerListener( this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static OrientationService singleton(Activity activity){
        if(instance ==null){
            instance = new OrientationService(activity);
        }
        return instance;
    }

    /**
     * this method is called when the sensor detects a change in value
     * @param event the event containing the values we need
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() ==Sensor.TYPE_ACCELEROMETER){
            //if we only have this sensor, we can't compute the orientation with it alone
            //but we should still save it for later
            accelerometerReading = event.values;
        }
        if(event.sensor.getType() ==Sensor.TYPE_MAGNETIC_FIELD){
            //if we only have this sensor, we can't compute the orientation with it alone
            //but we should still save it for later
            magnetometerReading = event.values;
        }
        if(accelerometerReading != null && magnetometerReading !=null){
            //we have both sensors, so we can compute the orientation
            onBothSensorDataAvailable();
        }
    }

    /**
     * called when we have readings for both sensors
     */
    private void onBothSensorDataAvailable() {
        //discount contract checking. think design by contract!
        if(accelerometerReading == null ||magnetometerReading == null){
            throw new IllegalStateException("Both sensors must be available to compute orientation");
        }

        float[] r = new float[9];
        float[] i = new float[9];
        //now we do some linear algebra magic using the two sensor readings
        boolean success = SensorManager.getRotationMatrix(r, i, accelerometerReading, magnetometerReading);
        //did it work?
        if(success){
            //ok we're good to go!
            float[] orientation = new float[3];
            SensorManager.getOrientation(r,orientation);
            
            //orientation now contains in order: azimuth, pitch and roll...
            //these are coordinates in 3d space commonly used by aircraft
            //but we only care about azimuth
            //azimuth is the angle between the magnetic north pole and the y axis
            //around the z axis (-pi to pi)
            //an azimuth of 0 means that the device is pointer north, and pi means it's pointed south
            //pi/2 means it's pointed east, and 3pi/2 means its pointer west
            this.azimuth.postValue(orientation[0]);
        }
    }

    public void unregisterSensorListeners(){sensorManager.unregisterListener(this);}

    public LiveData<Float> getOrientation(){return this.azimuth;}

    public void setMockOrientationSource(MutableLiveData<Float> mockDataSource){
        unregisterSensorListeners();
        this.azimuth = mockDataSource;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
