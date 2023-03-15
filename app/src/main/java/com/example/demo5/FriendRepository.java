package com.example.demo5;

import java.util.List;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class FriendRepository {
    private FriendDao dao;

    public FriendRepository (FriendDao d) {
        this.dao = d;
    }

    // Local Methods
    // =============

    public Friend getLocal(String uid) {
        return dao.get(UUID.fromString(uid));
    }

    public List<Friend> getAllLocal() {
        return dao.getAll();
    }

    public LiveData<List<Friend>> getAllLocalLive() {
        return dao.getAllLive();
    }

    public void upsertLocal(Friend friend) {
        dao.upsert(friend);
    }

    public void deleteLocal(Friend friend) {
        dao.delete(friend);
    }

    public boolean existsLocal(UUID uid) {
        return dao.exists(uid);
    }

    public LiveData<List<Friend>> getSynced() {
        var friends = new MediatorLiveData<List<Friend>>();

        friends.addSource(getAllLocalLive(), friends::postValue);
        return friends;
    }
}
