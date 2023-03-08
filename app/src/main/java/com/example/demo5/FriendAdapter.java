package com.example.demo5;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

public class FriendAdapter extends ArrayAdapter {
    private List<Friend> friends = Collections.emptyList();

    public FriendAdapter(@NonNull Context context, int resource, List friends) {
        super(context, resource);

        
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

}
