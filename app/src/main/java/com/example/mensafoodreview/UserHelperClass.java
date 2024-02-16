package com.example.mensafoodreview;
public class UserHelperClass {
        String fullname, email, reviewDescription, mensa_name, dish_name, imageUrl;
        int myRating;

    public UserHelperClass(String fullname, String email, String reviewDescription, String mensa_name, String dish_name, int myRating, String imageUrl) {
        this.fullname = fullname;
        this.email = email;
        this.reviewDescription = reviewDescription;
        this.dish_name = dish_name;
        this.mensa_name = mensa_name;
        this.myRating = myRating;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getMyRating() { return myRating; }

    public void setMyRating(int myRating) { this.myRating = myRating; }

    public String getFullname() {
        return fullname;
    }

    public String getMensa_name() { return mensa_name; }

    public void setMensa_name(String mensa_name) {
        this.mensa_name = mensa_name;
    }

    public String getDish_name() { return dish_name; }

    public void setDish_name(String dish_name) { this.dish_name = dish_name; }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReviewDescription() { return reviewDescription; }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public UserHelperClass() {
    }
}
