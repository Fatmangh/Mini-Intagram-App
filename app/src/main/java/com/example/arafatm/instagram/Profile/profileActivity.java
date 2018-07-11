package com.example.arafatm.instagram.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arafatm.instagram.R;
import com.example.arafatm.instagram.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.ParseUser;

public class profileActivity extends AppCompatActivity {
    private static final String TAG = "profileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context context = profileActivity.this;
    private TextView posts;
    private TextView followers;
    private TextView following;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupBttomNavigationView();

        posts = (TextView) findViewById(R.id.tvPosts);
        followers = (TextView) findViewById(R.id.tvFollowers);
        following = (TextView) findViewById(R.id.tvFollow);
        image = (ImageView) findViewById(R.id.profile_image);

        ParseUser.getCurrentUser().fetchInBackground();
        ParseUser currentUser = ParseUser.getCurrentUser();

        posts.setText(currentUser.getString("NumPosts"));
        String str = currentUser.getString("followers");
        followers.setText(str);
        following.setText(currentUser.getString("following"));

        Glide.with(context).load(ParseUser.getCurrentUser().getParseFile("image").getUrl()).into(image);

    }


    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.d(TAG, "clicked on menu item: " + menuItem);

                switch (menuItem.getItemId()) {
                    case R.id.profileMenu:
                        Log.d(TAG, "Navigating to profilePage: " + menuItem);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    /**
     * ButtomView SETUP
     **/
    private void setupBttomNavigationView() {
        Log.d(TAG, "Setting up Nav View");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupButtomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(context, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
