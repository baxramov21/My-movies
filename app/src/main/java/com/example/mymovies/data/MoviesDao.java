package com.example.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id == :movieID ")
    Movie getMovieByID(int movieID);

    @Query("DELETE FROM movies")
    void deleteAllMovies();

    @Insert
    void addMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavouriteMovie>> getAllFavouriteMovies();

    @Insert
    void addFavouriteMovie(FavouriteMovie movie);

    @Delete
    void deleteFavouriteMovie(FavouriteMovie movie);

    @Query("SELECT * FROM favourite_movies WHERE id == :favourite_movieId ")
    FavouriteMovie getFavouriteMovieByID(int favourite_movieId);
}
