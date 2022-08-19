package com.sheyx.mymovies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sheyx.mymovies.pojos.FavouriteMovie;
import com.sheyx.mymovies.pojos.Movie;
import com.sheyx.mymovies.pojos.Review;
import com.sheyx.mymovies.pojos.SearchResult;
import com.sheyx.mymovies.pojos.Trailer;

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

    @Query("DELETE FROM reviews")
    void deleteAllReviews();

    @Query("DELETE FROM trailers")
    void deleteAllTrailers();

    @Query("SELECT * FROM list_of_movies WHERE title == :movieName")
    Movie getMovieByName(String movieName);

    @Query("SELECT * FROM search_result")
    LiveData<SearchResult> getSearchResult();

    @Insert()
    void insertSearchResult(SearchResult result);

    @Query("DELETE FROM search_result")
    void deleteSearchResult();

}