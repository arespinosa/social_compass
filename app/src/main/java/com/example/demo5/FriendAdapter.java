package com.example.demo5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.function.BiConsumer;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class FriendAdapter extends ArrayAdapter {

    private Context context;
    private List<Friend> friends;
    private BiConsumer<Friend, String> onTextEditedHandler;

    public FriendAdapter(@NonNull Context context, int resource, List<Friend> friends) {
        super(context, resource);
        this.context = context;
        this.friends = friends;
    }

    public void setOnTextEditedHandler(BiConsumer<Friend, String> onTextEdited) {
        this.onTextEditedHandler = onTextEdited;
    }


    public void setFriends(List<Friend> friends) {
        this.friends.clear();
        this.friends.addAll(friends);
        notifyDataSetChanged();
    }

    public List<Friend> getFriends() {
        return friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.wtf("ADAPTER", "notify data set changed");
        // Inflate the view if it doesn't exist
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        }

        // Get the Friend object for the current position
        Friend friend = friends.get(position);

        // Get the UI elements
        TextView friend1 = convertView.findViewById(R.id.friend1);
        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams)
                friend1.getLayoutParams();
        layoutParams1.circleAngle = (float) Math.toDegrees(friends.get(0).getFriendRad());
        friend1.setLayoutParams(layoutParams1);
        friend1.setText(friend.getUidString());

        return convertView;
    }
}
