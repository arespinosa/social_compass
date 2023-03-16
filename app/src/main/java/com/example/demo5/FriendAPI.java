package com.example.demo5;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendAPI {

    private volatile static FriendAPI instance = null;

    private OkHttpClient client;

    public FriendAPI() {
        this.client = new OkHttpClient();
    }

    public static FriendAPI provide() {
        if (instance == null) {
            instance = new FriendAPI();
        }
        return instance;
    }

    public List<Friend> getFriends() {
        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/locations")
                .build();

        try (Response response = client.newCall(request).execute()) {


            String body = response.body().string();
            System.out.println(body);
            Gson gson = new Gson();
            Type friendListType = new TypeToken<List<Friend>>() {}.getType();
            List<Friend> friends = gson.fromJson(body, friendListType);

            for (Friend f : friends)
                Log.i("POSITION " + f.getLatitude() + ", " + f.getLongitude(), f.getLabel() );

            return friends;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Friend getFriend(String public_code) {
        String encodedPublicCode = public_code.replace(" ", "%20");
        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + encodedPublicCode)
                .build();

        try (Response response = client.newCall(request).execute()) {

            String body = response.body().string();
            System.out.println(body);
            Gson gson = new Gson();
            Friend friend = gson.fromJson(body, Friend.class);

            Log.i("POSITION " + friend.getLatitude() + ", " + friend.getLongitude(), friend.getLabel() );

            return friend;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean putFriend(Friend friend) {
        Gson gson = new Gson();
        String json = gson.toJson(friend);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        String encodedUid = friend.getUid().replace(" ", "%20");
        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + encodedUid)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean patchFriend(Friend friend) {
        Gson gson = new Gson();
        String json = gson.toJson(friend);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        String encodedUid = friend.getUid().replace(" ", "%20");
        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + encodedUid)
                .patch(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
