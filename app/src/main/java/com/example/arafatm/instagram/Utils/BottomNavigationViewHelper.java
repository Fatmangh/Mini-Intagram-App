package com.example.arafatm.instagram.Utils;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.arafatm.instagram.R;
import com.example.arafatm.instagram.Home.TimelineActivity;
import com.example.arafatm.instagram.following_and_likes.followActivity;
import com.example.arafatm.instagram.Profile.profileActivity;
import com.example.arafatm.instagram.Search.searchActivity;
import com.example.arafatm.instagram.Share.shareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupButtomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "Setting up bottomNavView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle presses on the action bar items
                switch (item.getItemId()) {
                    case R.id.ic_house:
                        goToHomePage(context);
                        return true;
                    case R.id.ic_search:
                        goToSearchPage(context);
                        return true;
                    case R.id.ic_add:
                        goToSharePage(context);
                        return true;
                    case R.id.ic_profile:
                        goToProfilePage(context);
                        return true;
                    case R.id.ic_like:
                        goToLikesPage(context);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private static void goToLikesPage(Context context) {
        Intent intent = new Intent(context, followActivity.class);
        context.startActivity(intent);
    }

    private static void goToHomePage(Context context) {
        Intent intent = new Intent(context, TimelineActivity.class);
        context.startActivity(intent);
    }

    private static void goToSharePage(Context context) {
        Intent intent = new Intent(context, shareActivity.class);
        context.startActivity(intent);
    }

    private static void goToSearchPage(Context context) {
        Intent intent = new Intent(context, searchActivity.class);
        context.startActivity(intent);
    }

    private static void goToProfilePage(Context context) {
        Intent intent = new Intent(context, profileActivity.class);
        context.startActivity(intent);
    }
}
