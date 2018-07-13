package com.example.arafatm.instagram.Share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arafatm.instagram.Home.TimelineActivity;
import com.example.arafatm.instagram.Model.Post;
import com.example.arafatm.instagram.R;
import com.example.arafatm.instagram.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class shareActivity extends AppCompatActivity {
    private static final String TAG = "shareActivity";
    private static final int REQUEST_CODE = 5;
    public final String APP_TAG = "Instagram";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int CAPTURE_IMAGE_ACTIVITY = 104;
    public String photoFileName = "photo.jpg";
    private static final int ACTIVITY_NUM = 2;
    private Context context = shareActivity.this;
    private EditText description;
    private Button postButton;
    private Button uploadButton;
    private Button cameraButton;
    private ImageView image;
    private String path = "";

    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setupBttomNavigationView();

        description = (EditText) findViewById(R.id.et_description);
        postButton = (Button) findViewById(R.id.bt_post);
        cameraButton = (Button) findViewById(R.id.bt_takePhot);
        uploadButton = (Button) findViewById(R.id.bt_upload);
        image = (ImageView) findViewById(R.id.iv_image);

        //get image uploaded
        uploadButton.setOnClickListener(new View.OnClickListener() {
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

        //go to camera page
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create a File reference to access to future access
                photoFile = getPhotoFileUri(photoFileName);

                // wrap File object into a content provider
                // required for API >= 24
                // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher


                Uri fileProvider = FileProvider.getUriForFile(shareActivity.this, "com.codepath.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Description = description.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                final File file = new File(path);
                final ParseFile parseFile = new ParseFile(file);

                createPost(Description, parseFile, user);
            }
        });

       //TODO
        //Enable post deletion
        //Enable likes, comment, save
        //display saved and posted picture on profile page
        //do details view
        //Make it look good

    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
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

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("ShareView", "post successfully created");
                    Intent intent = new Intent(context, TimelineActivity.class);
                    intent.putExtra("post", Parcels.wrap(newPost));
                    setResult(REQUEST_CODE, intent);
                    startActivity(intent);
                } else {
                    Log.d("ShareView", "post successfully failed");
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
                // by this point we have the camera photo on disk
                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // Load the taken image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.iv_image);
                ivPreview.setImageBitmap(resizeImage(resizeImage(rawTakenImage)));
            }

            } else if (requestCode == CAPTURE_IMAGE_ACTIVITY) {
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
                        // Load the selected image into a preview
                        ImageView ivPreview = (ImageView) findViewById(R.id.iv_image);
                        ivPreview.setImageBitmap(resizeImage(resizeImage(selectedImage)));
                    }
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // RESIZE BITMAP, see section below
    // by this point we have the camera photo on disk
    private Bitmap resizeImage(Bitmap rawTakenImage) {
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 50);
        // Configure byte output stream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // Compress the image further
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
        Uri resizedUri = Uri.fromFile(getPhotoFileUri(photoFileName + "_resized"));
        path = resizedUri.getPath();
        File resizedFile = new File(resizedUri.getPath());
        try {
            resizedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(resizedFile);
            // Write the bytes of the bitmap to file
            fos.write(bytes.toByteArray());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rawTakenImage;
    }


}