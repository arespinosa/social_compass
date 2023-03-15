package com.example.demo5;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.function.BiConsumer;

import androidx.annotation.NonNull;

public class FriendAdapter extends ArrayAdapter {

    private List<Friend> friends;
    private BiConsumer<Friend, String> onTextEditedHandler;

    public FriendAdapter(@NonNull Context context, int resource, List<Friend> friends) {
        super(context, resource, friends);
        this.friends = friends;
    }

    public void setOnTextEditedHandler(BiConsumer<Friend, String> onTextEdited) {
        this.onTextEditedHandler = onTextEdited;
    }


    public void setFriends(List<Friend> friends) {
        this.friends.clear();
        //this.clear();
        this.friends.addAll(friends);
        //addAll(friends);
        notifyDataSetChanged();
    }

    public List<Friend> getFriends() {
        return friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.wtf("ADAPTER", "notify data set changed");
        return convertView;
    }
}
