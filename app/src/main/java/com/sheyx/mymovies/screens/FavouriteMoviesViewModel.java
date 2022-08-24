package com.sheyx.mymovies.screens;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sheyx.mymovies.db.MoviesDatabase;
import com.sheyx.mymovies.pojos.FavouriteMovie;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavouriteMoviesViewModel extends AndroidViewModel {

    private LiveData<List<FavouriteMovie>> favouriteMovies;
    public static MoviesDatabase database;

    public FavouriteMoviesViewModel(@NonNull Application application) {
        super(application);
        database = MoviesDatabase.getInstance(getApplication());
        favouriteMovies = database.moviesDao().getAllFavouriteMovies();
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public FavouriteMovie getFavouriteMovieById(int id) {
        try {
            return new GetFavouriteMovieByIdTask().execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertFavouriteMovie(FavouriteMovie movie) {
        new InsertFavouriteMovieTask().execute(movie);
    }


    public void deleteFavouriteMovie(FavouriteMovie movie) {
        new DeleteFavouriteMovieTask().execute(movie);
    }


    @SuppressWarnings("deprecation")
    private static class InsertFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {
        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if (movies != null && movies.length > 0)
                database.moviesDao().insertFavouriteMovie(movies[0]);
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    private static class DeleteFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {
        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if (movies != null && movies.length > 0)
                database.moviesDao().deleteFavouriteMovie(movies[0]);
            return null;
        }
    }


    @SuppressWarnings("deprecation")
    private static class GetFavouriteMovieByIdTask extends AsyncTask<Integer, Void, FavouriteMovie> {

        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.moviesDao().getFavouriteMovieById(integers[0]);
            }
            return null;
        }
    }
}
