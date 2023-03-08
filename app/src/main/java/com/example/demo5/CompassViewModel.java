package com.example.demo5;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CompassViewModel extends AndroidViewModel {

    private LiveData<List<Friend>> friends;
    private FriendRepository repo;

    public CompassViewModel(@NonNull Application application) {
        super(application);

    }

    public LiveData<List<Friend>> getNotes() {
        if (friends == null) {
            friends = repo.getAllLocal();
        }
        return friends;
    }
}
