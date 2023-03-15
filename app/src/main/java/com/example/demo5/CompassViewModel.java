package com.example.demo5;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CompassViewModel extends AndroidViewModel {

    private List<Friend> friends;
    private FriendRepository repo;
    private FriendDao dao;

    public MutableLiveData<List<Friend>> lst;

    public CompassViewModel(@NonNull Application application) {
        super(application);

        var context = application.getApplicationContext();
        var db = FriendDatabase.provide(context);

        dao = db.getDao();

        this.repo = new FriendRepository(dao);
    }

    public MutableLiveData<List<Friend>> getFriends() {
        if (friends == null)
            friends = repo.getAllLocal();
        lst.postValue(friends);
        return lst;
    }



    public void updateText(Friend friend, String s) {
        friend.name = s;
        dao.upsert(friend);
    }

    public FriendDao getDao() {
        return this.dao;
    }
}
