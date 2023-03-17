package com.example.demo5;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CompassViewModel extends AndroidViewModel {
    private FriendRepository repo;
    private FriendDao dao;
    private LiveData<List<Friend>> friends;
    private List<Friend> friendds;

    public CompassViewModel(@NonNull Application application) {
        super(application);

        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);

        dao = db.getDao();

        this.repo = new FriendRepository(dao);
    }

    public LiveData<List<Friend>> getFriends() {
        if (friends == null) {
            friends = repo.getAllSynced();
        }

        return friends;
    }

    public LiveData<Friend> getFriend(String public_code) {
        var friend = repo.getSynced(public_code);

        return friend;
    }

    public void save(Friend friend) {
        repo.upsertLocal(friend);
        repo.upsertRemote(friend);
    }

    public FriendDao getDao() {
        return dao;
    }
}
