package com.example.demo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeService {
    //LiveData variable which contains the latest time value.
    private MutableLiveData<Long> timeValue;
    //ScheduledFuture result of scheduling a task with a ScheduledExecuterService
    private ScheduledFuture<?> clockFuture;
    //Singleton instance of TimeService
    private static TimeService instance;

    /**
     * @return a singleton instance of the TimeService class
     */
    public static TimeService singleton(){
        if(instance ==null){
            instance = new TimeService();
        }
        return instance;
    }

    /**
     * Constructor for TimeService
     */
    protected TimeService(){
        this.timeValue= new MutableLiveData<>();
        this.registerTimeListener();
    }

    /**
     * Registers a time listener using ScheduledExecutorService
     * which runs at an interval and updates the time value
     */
    public void registerTimeListener(){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        this.clockFuture = executor.scheduleAtFixedRate(()->{
            this.timeValue.postValue(System.currentTimeMillis());
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * @return Current time value
     */
    public MutableLiveData<Long> getTime(){
        return this.timeValue;
    }

    /**
     * Unregisters the time listener
     */
    public void unregisterTimeListener(){
        this.clockFuture.cancel(true);
    }

    /**
     * Mocks the time source intead of using the time listener
     * @param mockTimeSource
     */
    public void setMockTimeSource(MutableLiveData<Long> mockTimeSource){
        unregisterTimeListener();
        this.timeValue= mockTimeSource;
    }

}
