package com.example.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.mymovies.api.ApiFactory;
import com.example.mymovies.api.ApiService;
import com.example.mymovies.pojos.Movie;
import com.example.mymovies.pojos.MoviesResult;

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


    private CompositeDisposable compositeDisposable;

    public static MoviesDatabase database;
    private LiveData<List<Movie>> movies;
    private LiveData<List<FavouriteMovie>> favouriteMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        database = MoviesDatabase.getInstance(getApplication());
        movies = database.moviesDao().getAllMovies();
        favouriteMovies = database.moviesDao().getAllFavouriteMovies();
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public Movie getMovieById(int id) {
        try {
            return new GetMovieByIdTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FavouriteMovie getFavouriteMovieById(int id) {
        try {
            return new GetFavouriteMovieByIdTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void deleteAll() {
        new DeleteAllMoviesTask().execute();
    }

    public void insertMovie(List<Movie> movies) {
        new InsertMovieTask().execute(movies);
    }

    public void deleteMovie(Movie movie) {
        new DeleteMovieTask().execute(movie);
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void insertFavouriteMovie(FavouriteMovie movie) {
        new InsertFavouriteMovieTask().execute(movie);
    }

    public void deleteFavouriteMovie(FavouriteMovie movie) {
        new DeleteFavouriteMovieTask().execute(movie);
    }

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
                .subscribe(new Consumer<MoviesResult>() {
                    @Override
                    public void accept(MoviesResult moviesResult) throws Exception {
                        ArrayList<Movie> movies = (ArrayList<Movie>) moviesResult.getMovies();
                        if (movies != null && !movies.isEmpty()) {
                            if (page == 1) {
                                deleteAll();
//                              movieAdapter.clear();
                            }

                            insertMovie(movies);
//                            movieAdapter.addMovies(movies);
//                            page++;
                        }
//                        isLoading = false;
//                        progressBarLoading.setVisibility(View.INVISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(MainActivity.this, "Error bro: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

    private static class GetFavouriteMovieByIdTask extends AsyncTask<Integer, Void, FavouriteMovie> {

        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.moviesDao().getFavouriteMovieById(integers[0]);
            }
            return null;
        }
    }

    private static class DeleteAllMoviesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... integers) {
            database.moviesDao().deleteAllMovies();
            return null;
        }
    }

    private static class InsertMovieTask extends AsyncTask<List<Movie>, Void, Void> {
        @Override
        protected Void doInBackground(List<Movie>... movies) {
            if (movies != null && movies.length > 0)
                database.moviesDao().insertMovie(movies[0]);
            return null;
        }
    }

    private static class DeleteMovieTask extends AsyncTask<Movie, Void, Void> {
        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0)
                database.moviesDao().deleteMovie(movies[0]);
            return null;
        }
    }

    private static class InsertFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {
        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if (movies != null && movies.length > 0)
                database.moviesDao().insertFavouriteMovie(movies[0]);
            return null;
        }
    }

    private static class DeleteFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {
        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if (movies != null && movies.length > 0)
                database.moviesDao().deleteFavouriteMovie(movies[0]);
            return null;
        }
    }
}