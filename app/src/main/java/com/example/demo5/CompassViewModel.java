package com.example.demo5;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CompassViewModel extends AndroidViewModel {
    private FriendRepository repo;
    private FriendDao dao;

    public CompassViewModel(@NonNull Application application) {
        super(application);

        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);

        dao = db.getDao();

        this.repo = new FriendRepository(dao);
    }

    public LiveData<List<Friend>> getFriends() {
        var friends = repo.getAllSynced();

        return friends;
    }

    public LiveData<Friend> getFriend(String public_code) {
        var friend = repo.getSynced(public_code);

        return friend;
    }

    public void save(Friend friend) {
        //repo.upsertLocal(note);
        repo.upsertRemote(friend);
    }
}
