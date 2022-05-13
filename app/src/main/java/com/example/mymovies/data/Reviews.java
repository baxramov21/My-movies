package com.example.mymovies.data;

public class Reviews {

    private String author;
    private String review_title;

    public Reviews(String author, String review_title) {
        this.author = author;
        this.review_title = review_title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview_title() {
        return review_title;
    }

    public void setReview_title(String review_title) {
        this.review_title = review_title;
    }
}
