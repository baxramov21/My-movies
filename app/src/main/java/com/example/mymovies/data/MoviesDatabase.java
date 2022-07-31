package com.example.mymovies.data;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.mymovies.pojos.Movie;

@Database(entities = {Movie.class , FavouriteMovie.class} , version = 5 , exportSchema = false)
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
