package com.example.mymovies.utils;

import android.util.Log;

import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Reviews;
import com.example.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {


    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE = "w185";
    private static final String BIG_POSTER_SIZE = "w780";


    private static final String KEY_RESULTS = "results";

    // For reviews
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";

    // For movie video
    private static final String KEY_VIDEO_KEY = "key";
    private static final String KEY_NAME = "name";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    // For movie info
    private static final String KEY_ID = "id";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_TITLE = "title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_ORIGINAL_TITLE = "original_title";

    public static ArrayList<Reviews> getReviewsFromJSON(JSONObject jsonObject) {
        ArrayList<Reviews> reviewsArrayList = null;
        if (jsonObject == null) {
            return null;
        }
        try {
            JSONArray reviewsJSONArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < reviewsJSONArray.length(); i++) {
                JSONObject currentReviewFromJSON = reviewsJSONArray.getJSONObject(i);
                String author = currentReviewFromJSON.getString(KEY_AUTHOR);
                String reviewContent = currentReviewFromJSON.getString(KEY_CONTENT);
                Reviews review = new Reviews(author, reviewContent);
                reviewsArrayList.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewsArrayList;
    }

    public static ArrayList<Trailer> getTrailersFromJSON(JSONObject jsonObject) {
        ArrayList<Trailer> trailerArrayList = null;
        if (jsonObject == null) {
            return null;
        }
        try {
            trailerArrayList = new ArrayList<>();
            JSONArray reviewsJSONArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < reviewsJSONArray.length(); i++) {
                JSONObject currentTrailerFromJSON = reviewsJSONArray.getJSONObject(i);
                String key = BASE_YOUTUBE_URL + currentTrailerFromJSON.getString(KEY_VIDEO_KEY);
                String video_name = currentTrailerFromJSON.getString(KEY_NAME);
                Trailer trailer = new Trailer(video_name,key);
                trailerArrayList.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerArrayList;
    }


    public static ArrayList<Movie> getAllMoviesFromJSON(JSONObject jsonObject) {
        ArrayList<Movie> moviesList = null;
        if (jsonObject == null) {
            Log.e("Main","JSON object to getAllMoviesFromJSON is null");
            return null;
        }

        try {
            moviesList = new ArrayList<>();
            JSONArray moviesJSONArray = jsonObject.getJSONArray(KEY_RESULTS);
            Log.i("Main", moviesJSONArray.length() + "");
            for (int i = 0; i < moviesJSONArray.length(); i++) {
                JSONObject movieJSON = moviesJSONArray.getJSONObject(i);
                int id = movieJSON.getInt(KEY_ID);
                int voteCount = movieJSON.getInt(KEY_VOTE_COUNT);
                double voteAverage = movieJSON.getDouble(KEY_VOTE_AVERAGE);
                String title = movieJSON.getString(KEY_TITLE);
                String overView = movieJSON.getString(KEY_OVERVIEW);
                String posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + movieJSON.getString(KEY_POSTER_PATH);
                String bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + movieJSON.getString(KEY_POSTER_PATH);
                String releaseDate = movieJSON.getString(KEY_RELEASE_DATE);
                String backdropPath = movieJSON.getString(KEY_BACKDROP_PATH);
                String originalTitle = movieJSON.getString(KEY_ORIGINAL_TITLE);

                Movie currentMovie = new Movie(id, voteCount, voteAverage, title, overView, bigPosterPath, posterPath, releaseDate, backdropPath, originalTitle);
                moviesList.add(currentMovie);
            }
        } catch (JSONException e) {
           Log.e("Main","JSON exception in JSON utils " + e);
        }
        return moviesList;
    }
}
