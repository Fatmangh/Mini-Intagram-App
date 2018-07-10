package com.example.arafatm.instagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.arafatm.instagram.Model.Post;
import com.example.arafatm.instagram.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class TimelineActivity extends AppCompatActivity {
    public EditText descriptionInput;
    public Button createButton;
    public Button refreshButton;
    public static String imagePath = "path to the picture!";
    private static final String TAG = "TimelineActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context context = TimelineActivity.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupBttomNavigationView();

//////        descriptionInput = (EditText) findViewById(R.id.);
//////        createButton = (Button) findViewById(R.id.);
//////        refreshButton = (Button) findViewById(R.id.);
////
////        createButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                final String description = descriptionInput.getText().toString();
////                final ParseUser user = ParseUser.getCurrentUser();
////
////                final File file = new File(imagePath);
////                final ParseFile parseFile = new ParseFile(file);
////
////                createPost(description, parseFile, user);
////            }
//        });
//        refreshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadTopPosts();
//            }
//        });
//        loadTopPosts();
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("TimelineActivity", "post successfully created");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("TimelineActivity", "Post[" + i + "]" + objects.get(i).getDescription() + "\nusername = " + objects.get(i).getUser().getUsername());
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
}
