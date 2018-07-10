package com.example.arafatm.instagram;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context context = ChatActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

//    /**
//     * ButtomView SETUP
//     **/
//    private void setupBttomNavigationView() {
//        Log.d(TAG, "Setting up Nav View");
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupButtomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(context, bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }

}
