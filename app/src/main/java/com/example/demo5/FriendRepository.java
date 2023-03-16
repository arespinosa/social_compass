package com.example.demo5;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class FriendRepository {
    private FriendAPI api;
    private FriendDao dao;

    private List<Friend> friends;
    private ScheduledFuture<?> poller;
    private final MutableLiveData<List<Friend>> realFriendsData = new MutableLiveData<>();;
    private Friend friend;
    private final MutableLiveData<Friend> realFriendData = new MutableLiveData<>();
    private Future<?> backgroundTask;

    public FriendRepository(FriendDao d) {
        this.dao = d;
        api = FriendAPI.provide();
    }

    public LiveData<Friend> getLocal(String uid) {
        return dao.get(uid);
    }

    public LiveData<List<Friend>> getAllLocal() {
        return dao.getAll();
    }

    public void upsertLocal(Friend friend) {
        dao.upsert(friend);
    }
    
    private void upsertAllLocal(List<Friend> friends) { for (Friend f : friends) {upsertLocal(f);} }

    public void deleteLocal(Friend friend) {
        dao.delete(friend);
    }

    public boolean existsLocal(String uid) {
        return dao.exists(uid);
    }

    public LiveData<List<Friend>> getAllRemote() {

        if (this.poller != null && !this.poller.isCancelled()) {
            poller.cancel(true);
        }

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        poller = executor.scheduleAtFixedRate(() -> {
            friends = api.getFriends();
            realFriendsData.postValue(friends);
        }, 0, 3000, TimeUnit.MILLISECONDS);

        System.out.println(poller);

        return realFriendsData;
    }

    public LiveData<Friend> getRemote(String public_code) {

        if (this.poller != null && !this.poller.isCancelled()) {
            poller.cancel(true);
        }

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        poller = executor.scheduleAtFixedRate(() -> {
            friend = api.getFriend(public_code);
            realFriendData.postValue(friend);
        }, 0, 3000, TimeUnit.MILLISECONDS);

        System.out.println(poller);

        return realFriendData;
    }

    public LiveData<List<Friend>> getAllSynced() {
        var friends = new MediatorLiveData<List<Friend>>();

        Observer<List<Friend>> updateFromRemote = theirFriends -> {
            var ourFriends = friends.getValue();
            if (theirFriends == null) return; // do nothing
            if (ourFriends == null ) {
                upsertAllLocal(theirFriends);
            }
        };
        
        friends.addSource(getAllLocal(), friends::postValue);
        friends.addSource(getAllRemote(), updateFromRemote);

        return friends;
    }

    public LiveData<Friend> getSynced(String public_code) {
        var friend = new MediatorLiveData<Friend>();

        Observer<Friend> updateFromRemote = theirFriend -> {
            var ourFriend = friend.getValue();
            if (theirFriend == null) return; // do nothing
            if (ourFriend == null ) {
                upsertLocal(theirFriend);
            }
        };

        friend.addSource(getLocal(public_code), friend::postValue);
        friend.addSource(getRemote(public_code), updateFromRemote);

        return friend;
    }

    public void upsertRemote(Friend friend) {

        if (backgroundTask != null) {
            backgroundTask.cancel(true);
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        backgroundTask = executor.submit(() -> {
            api.putFriend(friend);
        });

    }
}
