package com.sheyx.mymovies.screens;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sheyx.mymovies.api.ApiFactory;
import com.sheyx.mymovies.api.ApiService;
import com.sheyx.mymovies.db.MoviesDatabase;
import com.sheyx.mymovies.pojos.Movie;
import com.sheyx.mymovies.pojos.Review;
import com.sheyx.mymovies.pojos.Trailer;
import com.sheyx.mymovies.pojos.TrailersResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends AndroidViewModel {

    private static final String VOTE_COUNT = "900";
    private static final String AVERAGE_VOTE = "7";
    public static final int POPULARITY = 0;
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_AVERAGE_VOTES = "vote_average.desc";
    private final String language = Locale.getDefault().getLanguage();

    private ApiFactory apiFactory;
    private ApiService apiService;

    private CompositeDisposable compositeDisposable;

    public static MoviesDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<Review>> reviews;
    private LiveData<List<Trailer>> trailers;

    public LiveData<List<Trailer>> getTrailers() {
        return trailers;
    }

    public MovieViewModel(@NonNull Application application) {
        super(application);
        database = MoviesDatabase.getInstance(getApplication());
        movies = database.moviesDao().getAllMovies();
        compositeDisposable = new CompositeDisposable();
        trailers = database.moviesDao().getTrailer();
        reviews = database.moviesDao().getReviews();
        apiFactory = ApiFactory.getInstance();
        apiService = apiFactory.getApiService();
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


    public void deleteAll() {
        new DeleteAllMoviesTask().execute();
    }

    @SuppressWarnings("unchecked")
    private void insertMovie(List<Movie> movies) {
        new InsertMovieTask().execute(movies);
    }


    @SuppressWarnings("unchecked")
    private void insertTrailers(List<Trailer> trailers) {
        new InsertTrailerTask().execute(trailers);
    }

    @SuppressWarnings("unchecked")
    private void insertReviews(List<Review> reviews) {
        new InsertReviewsTask().execute(reviews);
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

    public void downloadMovies(int methodOfSort, int page) {
        String howtoSort = methodOfSort(methodOfSort);
        Disposable disposable = apiService.getMovieResult(language, howtoSort, String.valueOf(page), VOTE_COUNT, AVERAGE_VOTE)
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

    public void downloadReviews(int id) {
        Disposable disposable = apiService.getReviewsResult(String.valueOf(id), language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviewObject -> {
                    ArrayList<Review> reviews = (ArrayList<Review>) reviewObject.getReviews();
                    insertReviews(reviews);
                }, throwable -> Log.d("error_in_reviews", "Error occurred: " + throwable.getMessage()));
        compositeDisposable.add(disposable);
    }

    public void downloadTrailers(int movieId) {
        Disposable disposable = apiService.getTrailersResult(String.valueOf(movieId), language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    ArrayList<Trailer> trailers = (ArrayList<Trailer>) result.getResults();
                    Log.d("size", trailers.size() + " size");
                    insertTrailers(trailers);
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
                database.moviesDao().deleteAllTrailers();
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
                database.moviesDao().deleteAllReviews();
                database.moviesDao().insertReviews(lists[0]);
            }
            return null;
        }
    }

    public void deleteReviews() {
        new DeleteAllReviews().execute();
    }

    private static class DeleteAllReviews extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            database.moviesDao().deleteAllReviews();
            return null;
        }
    }

    public void deleteTrailers() {
        new DeleteAllTrailers().execute();
    }

    private static class DeleteAllTrailers extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            database.moviesDao().deleteAllTrailers();
            return null;
        }
    }

}