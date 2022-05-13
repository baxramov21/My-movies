package com.example.mymovies.data;

public class Trailer {

    private String movieTitle;
    private String videoKey;

    public Trailer(String movieTitle, String videoKey) {
        this.movieTitle = movieTitle;
        this.videoKey = videoKey;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }
}
