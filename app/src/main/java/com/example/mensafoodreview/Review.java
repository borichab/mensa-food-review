package com.example.mensafoodreview;

public class Review {
    String fullname, reviewDescription, mensa_name, dish_name, imageUrl;
    int myRating;

    public int getMyRating() {
        return myRating;
    }

    public String getImageUrl() { return imageUrl; }

    public String getFullname() {
        return fullname;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public String getMensa_name() {
        return mensa_name;
    }

    public String getDish_name() {
        return dish_name;
    }
}
