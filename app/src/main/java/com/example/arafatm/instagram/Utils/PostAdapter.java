package com.example.arafatm.instagram.Utils;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arafatm.instagram.Home.DetailsActivity;
import com.example.arafatm.instagram.Home.commentDetailActivity;
import com.example.arafatm.instagram.Model.Post;
import com.example.arafatm.instagram.Profile.profileActivity;
import com.example.arafatm.instagram.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post> mPost;
    private Context context;
    private boolean selected = false;


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
    public void onBindViewHolder(final PostAdapter.ViewHolder holder, int position) {
        // get the data according to position
        final Post post = mPost.get(position);

        // null placeholders if the PagedList is configured to use them
        // only works for data sets that have total count provided (i.e. PositionalDataSource)
        if (post == null) {
            return;
        }
        //populate the view according to this data
        holder.tvUsername.setText(ParseUser.getCurrentUser().getUsername());
        holder.tvDiscription.setText(post.getDescription());
        final String numLikes = post.getString("Likes");
        holder.numbLike.setText(numLikes);

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // increment number of likes
                if (selected == true) {
                    holder.likes.setImageResource(R.drawable.likes_unselected);
                    selected = false;
                    //TODO
                    //update dataBase
                } else {
                    holder.likes.setImageResource(R.drawable.likes_selected);
                    selected = true;
                    //TODO
                    //update dataBase
                }

                //TODO
                //Update likes
                Toast.makeText(context, "Picture liked!!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected == true) {
                    holder.savePost.setImageResource(R.drawable.save_unselected);
                    selected = false;
                } else {
                    holder.savePost.setImageResource(R.drawable.save_selected);
                    selected = true;
                }
                Toast.makeText(context, "Picture saved!!", Toast.LENGTH_SHORT).show();
            }
        });


        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, commentDetailActivity.class);
                context.startActivity(intent);
                //Enable comments.
                Toast.makeText(context, "Picture commented!", Toast.LENGTH_SHORT).show();
            }
        });

        ParseFile file = ParseUser.getCurrentUser().getParseFile("image");
        if (file != null) {
            Glide.with(context)
                    .load(file.getUrl())
                    .into(holder.ivProfileImage);
        }

        Glide.with(context).load(post.getImage().getUrl()).into(holder.postPic);
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    //create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvDiscription;
        public ImageView postPic;
        private ImageView likes;
        private ImageView comments;
        private ImageView message;
        private TextView numbLike;
        private ImageView savePost;
        private ImageView del;

        public ViewHolder(View itemView) {
            super(itemView);
            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.t_image);
            tvUsername = (TextView) itemView.findViewById(R.id.t_username);
            tvDiscription = (TextView) itemView.findViewById(R.id.t_body);
            postPic = (ImageView) itemView.findViewById(R.id.t_postImage);
            likes = (ImageView) itemView.findViewById(R.id.t_likes);
            comments = (ImageView) itemView.findViewById(R.id.t_comment);
            message = (ImageView) itemView.findViewById(R.id.t_message);
            savePost = (ImageView) itemView.findViewById(R.id.t_fav);
            numbLike = (TextView) itemView.findViewById(R.id.t_numLikes);
            del = (ImageView) itemView.findViewById(R.id.t_delete);

            postPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Post selectedPost = mPost.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("caption", selectedPost.getDescription());
                    intent.putExtra("time", selectedPost.getCreatedAt().toString());
                    intent.putExtra("image", selectedPost.getImage().getUrl());
                    context.startActivity(intent);
                }
            });

            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, profileActivity.class);
                    context.startActivity(intent);
                }
            });

            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Post toBeRemoved = mPost.get(position);
                    mPost.remove(position);
                    notifyDataSetChanged();
                    toBeRemoved.deleteInBackground();
                }
            });
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
        Collections.reverse(mPost);
        notifyDataSetChanged();
    }
}
