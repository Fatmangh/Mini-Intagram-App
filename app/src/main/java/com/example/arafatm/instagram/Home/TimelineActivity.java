package com.example.arafatm.instagram.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arafatm.instagram.Authentification.LoginActivity;
import com.example.arafatm.instagram.Model.Post;
import com.example.arafatm.instagram.R;
import com.example.arafatm.instagram.Utils.BottomNavigationViewHelper;
import com.example.arafatm.instagram.Utils.PostAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {
    public EditText descriptionInput;
    private SwipeRefreshLayout swipeContainer;
    public Button createButton;
    public Button refreshButton;
    public static String imagePath = "path to the picture!";
    private static final String TAG = "TimelineActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context context = TimelineActivity.this;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPost;
    private final int CODE = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupBttomNavigationView();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //checks if user is logged in or not
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
           Intent intent = new Intent(context, LoginActivity.class);
           startActivity(intent);
        }
        //find the Recycle view
        rvPost = (RecyclerView) findViewById(R.id.rvPost);
        //init the arraylist (data source)
        posts = new ArrayList<>();
        //construct the adapter from this data source
        postAdapter = new PostAdapter(posts);
        //RecycleView Setup (Layout manager, use adapter
        rvPost.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvPost.setAdapter(postAdapter);
        loadTopPosts();
    }


    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser().getPostsForUser(ParseUser.getCurrentUser());
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("TimelineActivity", "Post[" + i + "]" + objects.get(i).getDescription() + "\nusername = " + objects.get(i).getUser().getUsername());
                        Post post = objects.get(i);
                        posts.add(0, post);
                        //notify adapter
                        postAdapter.notifyItemChanged(posts.size() - 1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
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


    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == CODE) {
                if (resultCode == RESULT_OK) {
                    // by this point we have the camera photo on disk
                    Post post = (Post) Parcels.unwrap(data.getParcelableExtra("post"));

                    posts.add(0, post);

                    postAdapter.notifyItemChanged(0);
                    rvPost.scrollToPosition(0);
                }

            } else  { // Result was a failure
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
        }

        public void fetchTimelineAsync(int page) {
            // Send the network request to fetch the updated data
            // `client` here is an instance of Android Async HTTP
            // getHomeTimeline is an example endpoint.
            final Post.Query postQuery = new Post.Query();
            postQuery.getTop().withUser().getPostsForUser(ParseUser.getCurrentUser()).getTop();
            postQuery.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> objects, ParseException e) {
                    if (e == null) {
                        // Remember to CLEAR OUT old items before appending in the new ones
                        postAdapter.clear();
                        // ...the data has come back, add new items to your adapter...
                        postAdapter.addAll(objects);
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);
                    } else {
                        e.printStackTrace();
                        Log.d("DEBUG", "Fetch timeline error: " + e.toString());
                    }
                }
            });
        }
}
