package com.example.demo5;

import java.util.List;
import java.util.UUID;

import androidx.lifecycle.LiveData;

public class FriendRepository {
    private FriendDao dao;

    public FriendRepository (FriendDao d) {
        this.dao = d;
    }

    // Local Methods
    // =============

    public Friend getLocal(String title) {
        return dao.get(UUID.randomUUID());
    }

    public List<Friend> getAllLocal() {
        return dao.getAll();
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
}
