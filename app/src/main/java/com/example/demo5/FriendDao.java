package com.example.demo5;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

@Dao
public abstract class FriendDao {
    //
    @Upsert
    public abstract long upsert(Friend friend);

    @Query("SELECT EXISTS(SELECT 1 FROM friends WHERE uid = :uid)")
    public abstract boolean exists(String uid);

    @Query("SELECT * FROM friends WHERE uid = :uid")
    public abstract LiveData<Friend> get(String uid);

    @Delete
    public abstract int delete(Friend friend);

    @Upsert
    public abstract void upsertAll(List<Friend> list);

    @Query("SELECT * FROM friends ORDER BY uid")
    public abstract LiveData<List<Friend>> getAll();
}
