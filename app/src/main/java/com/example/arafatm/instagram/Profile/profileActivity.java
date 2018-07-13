package com.example.arafatm.instagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arafatm.instagram.Authentification.LoginActivity;
import com.example.arafatm.instagram.Home.DetailsActivity;
import com.example.arafatm.instagram.Model.Post;
import com.example.arafatm.instagram.R;
import com.example.arafatm.instagram.Utils.BottomNavigationViewHelper;
import com.example.arafatm.instagram.Utils.ImageAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.arafatm.instagram.Share.shareActivity.CAPTURE_IMAGE_ACTIVITY;

public class profileActivity extends AppCompatActivity {
    private static final String TAG = "profileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context context = profileActivity.this;
    private TextView posts;
    private TextView followers;

    int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 6;
    private TextView following;
    private TextView name;
    private TextView settings;
    private TextView mainName;
    private ImageView logout;
    private ImageView image;
    private ArrayList<String> postsAll;
    ImageAdapter imageAdapter;
    ArrayList<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupBttomNavigationView();

        images = new ArrayList<>();
        posts = (TextView) findViewById(R.id.tvPosts);
        followers = (TextView) findViewById(R.id.tvFollowers);
        following = (TextView) findViewById(R.id.tvFollow);
        image = (ImageView) findViewById(R.id.profile_image);
        mainName = (TextView) findViewById(R.id.p_profileName);
        name = (TextView) findViewById(R.id.display_name);
        settings = (TextView) findViewById(R.id.settings);
        logout = (ImageView) findViewById(R.id.profileMenu);

        ParseUser.getCurrentUser().fetchInBackground();
        //checks if user is logged in or not
        ParseUser currentUser = ParseUser.getCurrentUser();

        posts.setText(currentUser.getString("NumPosts"));
        String str = currentUser.getString("followers");
        followers.setText(str);
        following.setText(currentUser.getString("following"));
        mainName.setText(currentUser.getUsername());
        name.setText(currentUser.getString("name"));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent for picking a photo from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Bring up gallery to select a photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(profileActivity.this, "Logging out",
                        Toast.LENGTH_SHORT).show();
                ParseUser.logOut();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });

        ParseFile file = ParseUser.getCurrentUser().getParseFile("image");
        if (file != null) {
            Glide.with(context).load(file.getUrl()).into(image);
        }

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, settingsActivity.class);
                startActivity(intent);
            }
        });
        loadTopPosts();
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

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser().getPostsForUser(ParseUser.getCurrentUser());
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(final List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Post post = objects.get(i);
                        String url = post.getImage().getUrl();
                        images.add(url);
                    }
                    try {
                        imageAdapter = new ImageAdapter(context, images);
                        final GridView gridview = (GridView) findViewById(R.id.gridView);
                        gridview.setAdapter(imageAdapter);
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                Intent intent = new Intent(context, DetailsActivity.class);
                                Post selectedPost = objects.get(position);
                                intent.putExtra("caption", selectedPost.getDescription());
                                intent.putExtra("time", selectedPost.getCreatedAt().toString());
                                intent.putExtra("image", selectedPost.getImage().getUrl());
                                context.startActivity(intent);
                                Toast.makeText(profileActivity.this, "" + position,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri photoUri = data.getData();
                    // Do something with the photo based on Uri
                    Bitmap selectedImage = null;
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    byte[] Data = stream.toByteArray();
                    ParseFile imageFile = new ParseFile("mynew.png", Data);
                    ParseUser.getCurrentUser().put("image", imageFile);
                    imageFile.saveInBackground();


                    // Load the selected image into a preview
                    ImageView ivPreview = (ImageView) findViewById(R.id.profile_image);
                    ivPreview.setImageBitmap(selectedImage);
                }
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
