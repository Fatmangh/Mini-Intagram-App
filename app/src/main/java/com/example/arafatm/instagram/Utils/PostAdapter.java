package com.example.arafatm.instagram.Utils;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arafatm.instagram.Model.Post;
import com.example.arafatm.instagram.R;
import com.parse.ParseUser;

import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> mPost;
    private Context context;

    //pass in the Posts array in the constructor
    public PostAdapter(List<Post> Posts) {
        mPost = Posts;
    }


    //for each row, inflate the layout and cache reference into ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_post, parent, false);
        return new ViewHolder(movieView);
    }



    //bind the values based on the position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the data according to position
        Post Post = mPost.get(position);
        //populate the view according to this data
        holder.tvUsername.setText(ParseUser.getCurrentUser().getUsername());
        holder.tvDiscription.setText(Post.getDescription());



        Glide.with(context).load(ParseUser.getCurrentUser().getParseFile("image").getUrl()).into(holder.ivProfileImage);
        Glide.with(context).load(Post.getImage().getUrl()).into(holder.postPic);
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    //create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvDiscription;
        public ImageView postPic;


        public ViewHolder(View itemView) {
            super(itemView);
            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.t_image);
            tvUsername = (TextView) itemView.findViewById(R.id.t_username);
            tvDiscription = (TextView) itemView.findViewById(R.id.t_body);
            postPic = (ImageView) itemView.findViewById(R.id.t_postImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

                Post Post = mPost.get(position);

//                Intent intent = new Intent(context, DetailsActivity.class);
//
//                intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(Post));
//
//                context.startActivity(intent);
            }
        }
    }



    // Clean all elements of the recycler
    public void clear() {
        mPost.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPost.addAll(list);
        notifyDataSetChanged();
    }
}
