package com.example.arafatm.instagram;

import android.app.Application;

import com.example.arafatm.instagram.Model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fatmanTech")
                .clientKey("hashmapIntMap")
                .server("http://fatman-my-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
