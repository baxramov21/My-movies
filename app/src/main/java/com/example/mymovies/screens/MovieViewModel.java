package com.example.mymovies.screens;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.mymovies.api.ApiFactory;
import com.example.mymovies.api.ApiService;
import com.example.mymovies.pojos.FavouriteMovie;
import com.example.mymovies.pojos.Review;
import com.example.mymovies.pojos.Trailer;
import com.example.mymovies.db.MoviesDatabase;
import com.example.mymovies.pojos.Movie;
import com.example.mymovies.pojos.TrailersResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends AndroidViewModel {

    private static final String VOTE_COUNT = "1000";
    private static final String AVERAGE_VOTE = "7";
    public static final int POPULARITY = 0;
    public static final int AVERAGE_VOTES = 1;
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_AVERAGE_VOTES = "vote_average.desc";


    private ApiFactory apiFactory;
    private ApiService apiService;

    private CompositeDisposable compositeDisposable;

    public static MoviesDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavouriteMovie>> favouriteMovies;
    private LiveData<List<Review>> reviews;
    private LiveData<List<Trailer>> trailers;

    public LiveData<List<Trailer>> getTrailers() {
        return trailers;
    }

    public MovieViewModel(@NonNull Application application) {
        super(application);
        database = MoviesDatabase.getInstance(getApplication());
        movies = database.moviesDao().getAllMovies();
        favouriteMovies = database.moviesDao().getAllFavouriteMovies();
        compositeDisposable = new CompositeDisposable();
        trailers = database.moviesDao().getTrailer();
        reviews = database.moviesDao().getReviews();

        apiFactory = ApiFactory.getInstance();
        apiService = apiFactory.getApiService();
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public Movie getMovieById(int id) {
        try {
            return new GetMovieByIdTask().execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FavouriteMovie getFavouriteMovieById(int id) {
        try {
            return new GetFavouriteMovieByIdTask().execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void deleteAll() {
        new DeleteAllMoviesTask().execute();
    }

    @SuppressWarnings("unchecked")
    private void insertMovie(List<Movie> movies) {
        new InsertMovieTask().execute(movies);
    }


    public void insertFavouriteMovie(FavouriteMovie movie) {
        new InsertFavouriteMovieTask().execute(movie);
    }


    @SuppressWarnings("unchecked")
    private void insertTrailers(List<Trailer> trailers) {
        new InsertTrailerTask().execute(trailers);
    }

    @SuppressWarnings("unchecked")
    private void insertReviews(List<Review> reviews) {
        new InsertReviewsTask().execute(reviews);
    }

    public void deleteFavouriteMovie(FavouriteMovie movie) {
        new DeleteFavouriteMovieTask().execute(movie);
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    @SuppressWarnings("deprecation")
    private static class GetMovieByIdTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.moviesDao().getMovieById(integers[0]);
            }
            return null;
        }
    }

    public void downloadMovies(String lang, int methodOfSort, int page) {
        String howtoSort = methodOfSort(methodOfSort);
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        Disposable disposable = apiService.getMovieResult(lang, howtoSort, String.valueOf(page), VOTE_COUNT, AVERAGE_VOTE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviesResult -> {
                    ArrayList<Movie> movies = (ArrayList<Movie>) moviesResult.getMovies();
                    if (movies != null && !movies.isEmpty()) {
                        if (page == 1) {
                            deleteAll();
                        }

                        insertMovie(movies);
                    }
                }, throwable -> {
                    Log.d("error", "Ooops: " + throwable.getMessage());
                });
        compositeDisposable.add(disposable);
    }

    public void downloadReviews(int id, String language) {
        Disposable disposable = apiService.getReviewsResult(String.valueOf(id), language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviewObject -> {
                    ArrayList<Review> reviews = (ArrayList<Review>) reviewObject.getReviews();
                    insertReviews(reviews);
                }, throwable -> Log.d("error_in_reviews", "Error occurred: " + throwable.getMessage()));
        compositeDisposable.add(disposable);
    }

    public void downloadTrailers(int movieId, String language) {
        Disposable disposable = apiService.getTrailersResult(String.valueOf(movieId), language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TrailersResult>() {
                    @Override
                    public void accept(TrailersResult result) throws Exception {
                        ArrayList<Trailer> trailers = (ArrayList<Trailer>) result.getResults();
                        Log.d("size", trailers.size() + " size");

                        insertTrailers(trailers);
                    }
                }, throwable -> Log.d("error_in_trailers", "Error occurred: " + throwable.getMessage()));
        compositeDisposable.add(disposable);
    }

    private String methodOfSort(int methodOfSort) {
        if (methodOfSort == POPULARITY) {
            return SORT_BY_POPULARITY;
        } else {
            return SORT_BY_AVERAGE_VOTES;
        }
    }

    @Override
    protected void onCleared() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onCleared();
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

    @SuppressWarnings("deprecation")
    private static class DeleteAllMoviesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... integers) {
            database.moviesDao().deleteAllMovies();
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    private static class InsertMovieTask extends AsyncTask<List<Movie>, Void, Void> {
        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Movie>... movies) {
            if (movies != null && movies.length > 0)
                database.moviesDao().insertMovie(movies[0]);
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    private static class InsertTrailerTask extends AsyncTask<List<Trailer>, Void, Void> {

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Trailer>... lists) {
            if (lists != null && lists.length > 0) {
                database.moviesDao().insertTrailers(lists[0]);
            }
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    private static class InsertReviewsTask extends AsyncTask<List<Review>, Void, Void> {

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Review>... lists) {
            if (lists != null && lists.length > 0) {
                database.moviesDao().insertReviews(lists[0]);
            }
            return null;
        }
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
}