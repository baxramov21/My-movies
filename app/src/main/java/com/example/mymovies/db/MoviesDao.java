package com.example.mymovies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mymovies.pojos.FavouriteMovie;
import com.example.mymovies.pojos.Movie;
import com.example.mymovies.pojos.Review;
import com.example.mymovies.pojos.Trailer;

import java.util.List;


@Dao
public interface MoviesDao {
    @Query("SELECT * FROM list_of_movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavouriteMovie>> getAllFavouriteMovies();

    @Query("SELECT * FROM list_of_movies WHERE id == :movieId")
    Movie getMovieById(int movieId);

    @Query("SELECT * FROM favourite_movies WHERE id == :movieId")
    FavouriteMovie getFavouriteMovieById(int movieId);

    @Query("DELETE FROM list_of_movies")
    void deleteAllMovies();

    @Insert
    void insertMovie(List<Movie> movies);

    @Delete
    void deleteMovie(Movie movie);

    @Insert
    void insertFavouriteMovie(FavouriteMovie movie);

    @Delete
    void deleteFavouriteMovie(FavouriteMovie movie);

    @Query("SELECT * FROM trailers")
    LiveData<List<Trailer>> getTrailer();

    @Query("SELECT * FROM reviews")
    LiveData<List<Review>> getReviews();

    @Insert
    void insertTrailers(List<Trailer> trailers);

    @Insert
    void insertReviews(List<Review> reviews);
}