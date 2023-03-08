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

        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);

        var dao = db.getDao();

        this.repo = new FriendRepository(dao);
    }

    public LiveData<List<Friend>> getFriends() {
        if (friends == null) {
            friends = repo.getAllLocal();
        }
        return friends;
    }
}
