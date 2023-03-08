package com.example.demo5;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendAdapter extends ArrayAdapter {

    private List<Friend> friends = Collections.emptyList();
    private BiConsumer<Friend,String> onTextEditedHandler;

    public FriendAdapter(@NonNull Context context, int resource) {
        super(context, resource);

        
    }

    public void setOnTextEditedHandler (BiConsumer<Friend, String> onTextEdited) {
        this.onTextEditedHandler = onTextEdited;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private Friend friend;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.friend);

            this.textView.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    onTextEditedHandler.accept(friend, textView.getText().toString());
                }
            });
        }
    }
}
