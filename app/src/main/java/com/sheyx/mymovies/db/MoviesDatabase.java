package com.sheyx.mymovies.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sheyx.mymovies.pojos.FavouriteMovie;
import com.sheyx.mymovies.pojos.Movie;
import com.sheyx.mymovies.pojos.Review;
import com.sheyx.mymovies.pojos.SearchResult;
import com.sheyx.mymovies.pojos.Trailer;

@Database(entities = {Movie.class , FavouriteMovie.class, Review.class, Trailer.class, SearchResult.class} , version = 8 , exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase moviesDatabase;
    private static final String DB_NAME = "Movie.db";
    private static final Object LOCK = new Object();

    public static MoviesDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (moviesDatabase == null) {
                moviesDatabase = Room.databaseBuilder(context, MoviesDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            }
        }
        return moviesDatabase;
    }

    public abstract MoviesDao moviesDao();
}
