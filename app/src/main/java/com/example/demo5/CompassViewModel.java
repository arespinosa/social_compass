package com.example.demo5;

import android.app.Application;

import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CompassViewModel extends AndroidViewModel {
    private FriendRepository repo;
    private FriendDao dao;

    public LiveData<List<Friend>> friends;

    public CompassViewModel(@NonNull Application application) {
        super(application);

        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);

        dao = db.getDao();

        this.repo = new FriendRepository(dao);
    }

    public LiveData<List<Friend>> getFriends() {
        if (friends == null) {
            friends = repo.getSynced();
        }
        return friends;
    }

    public void putFriend(String uid) {
        Friend friend = new Friend();
        //friend.setLocation();
        friend.setUid(uid);
        repo.upsertLocal(friend);
    }

    public Friend getFriend(String uid) {
        return dao.get(UUID.fromString(uid));
    }
}
