package com.example.arafatm.instagram.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> posts;

    public ImageAdapter(Context c, ArrayList<String> images) throws IOException {
        mContext = c;
        posts = images;
    }

    public int getCount() {
        return posts.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
       public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        // imageView.setImageResource(mThumbIds[position]);
        Glide.with(parent.getContext())
                .load(posts.get(position))
                .into(imageView);
        return imageView;
    }

    // Add a list of items -- change to type used
    public void addAll(List<String> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

}