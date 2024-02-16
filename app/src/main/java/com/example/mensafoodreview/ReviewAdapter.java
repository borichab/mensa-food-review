package com.example.mensafoodreview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReViewHolder> {

    Context context;
    ArrayList<Review> reviews;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.review_card,parent,false);
        return new ReViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReViewHolder holder, int position) {
        Review review = reviews.get(position);

        Glide.with(context).load(review.getImageUrl()).into(holder.imageView);
        holder.name.setText(review.getFullname());
        holder.dish.setText(review.getDish_name());
        holder.mensa.setText(review.getMensa_name());
        holder.description.setText(review.getReviewDescription());
        holder.ratingBar.setRating(review.getMyRating());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReViewHolder extends RecyclerView.ViewHolder{

        TextView name, description, mensa, dish;
        RatingBar ratingBar;
        ImageView imageView;

        public ReViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.review);
            dish = itemView.findViewById(R.id.dishName);
            mensa = itemView.findViewById(R.id.mensa);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
