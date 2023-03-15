package com.example.demo5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

public class FriendAdapter extends BaseAdapter {
    private List<Friend> friends = Collections.emptyList();

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    public List<Friend> getFriends() {
        return friends;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return friends.get(position).getUidString().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the view if it doesn't exist
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
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
