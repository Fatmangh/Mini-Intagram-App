package com.example.arafatm.instagram.Home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arafatm.instagram.R;
import com.parse.ParseUser;

public class DetailsActivity extends AppCompatActivity {
    private ImageView imagePost;
    private ImageView profile;
    private TextView body;
    private TextView time;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imagePost = findViewById(R.id.d_image);
        profile = findViewById(R.id.d_profile);
        body = findViewById(R.id.d_body);
        username = findViewById(R.id.d_username);
        time = findViewById(R.id.d_time);

        String caption = getIntent().getStringExtra("caption");
        String date2 = getIntent().getStringExtra("time");
        time.setText(date2);
        body.setText(caption);
        username.setText(ParseUser.getCurrentUser().getUsername());

        Glide.with(DetailsActivity.this).load(ParseUser.getCurrentUser().getParseFile("image").getUrl()).into(profile);
        Glide.with(DetailsActivity.this).load(getIntent().getStringExtra("image")).into(imagePost);
    }
}
