package com.example.demo5;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Friend.class}, version = 0)
public abstract class FriendDatabase extends RoomDatabase {
    static public List<Friend> list = new ArrayList<>();
    static public Friend friend1;
    static public Friend friend2;

    private volatile static FriendDatabase instance = null;

    public abstract FriendDao getDao();

    public synchronized static FriendDatabase provide(Context context) {
        if (instance == null) {
            instance = FriendDatabase.make(context);
        }
        return instance;
    }

    private static FriendDatabase make(Context context) {
        list.add(friend1);
        list.add(friend2);

        return Room.databaseBuilder(context, FriendDatabase.class, "sc2.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                                   provide(context).getDao().getAll();
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void inject(FriendDatabase testDatabase) {
        if (instance != null ) {
            instance.close();
        }
        instance = testDatabase;
    }
}
