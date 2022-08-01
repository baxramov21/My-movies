package com.example.mymovies.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mymovies.R;
import com.example.mymovies.adapter.MovieAdapter;
import com.example.mymovies.pojos.Movie;

import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewPopularity;
    private TextView textViewTopRated;
    private MovieViewModel viewModel;
    private ProgressBar progressBarLoading;

    private CompositeDisposable compositeDisposable;
    private int page = 1;
    private int methodOfSort;
    private boolean isLoading = false;

    private String lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compositeDisposable = new CompositeDisposable();
        switchSort = findViewById(R.id.switchSort);
        textViewPopularity = findViewById(R.id.textViewMostPopular);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        lang = Locale.getDefault().getLanguage();
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, getWindowWidth()));
        movieAdapter = new MovieAdapter();
        recyclerViewPosters.setAdapter(movieAdapter);

        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        switchSort.setChecked(true);
        onCheckedChanged();
        methodOfSort(false);
        onPosterClick();

        movieAdapter.setOnReachEndListener(() -> {
            if (!isLoading) {
                page++;
                viewModel.downloadMovies(lang, methodOfSort, page);
            }
        });

        onDataChanged();
    }

    private void onCheckedChanged() {
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                page = 1;
                methodOfSort(isChecked);
            }
        });

        switchSort.setChecked(false);
    }

    private void onPosterClick() {
        movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClick() {
            @Override
            public void onPosterClick(int position) {
                Movie clickedMovie = movieAdapter.getMovies().get(position);
                Intent intentToMovieDetail = new Intent(MainActivity.this, DetailActivity.class);
                intentToMovieDetail.putExtra("ID", clickedMovie.getId());
                startActivity(intentToMovieDetail);
            }
        });
    }

    private void onDataChanged() {
        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, movies -> {
            if (movies != null) {
//                    if (page == 1) {
                movieAdapter.clear();
//                    }
                movieAdapter.addMovies(movies);
                page++;
            }
            isLoading = false;
            progressBarLoading.setVisibility(View.INVISIBLE);
        });

        if (isLoading) {
            viewModel.downloadMovies(lang, methodOfSort, page);
        }
    }

    private int getWindowWidth() {
        DisplayMetrics displayManager = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayManager);
        int windowWidth = (int) (displayManager.widthPixels / displayManager.density);
        return windowWidth / 185 > 2 ? windowWidth / 185 : 2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.main_movies_menu_item:
                Intent intent_to_MA = new Intent(this, MainActivity.class);
                startActivity(intent_to_MA);

            case R.id.favourite_movies_menu_item:
                Intent intent_to_favourite_activity = new Intent(this, FavouriteActivity.class);
                startActivity(intent_to_favourite_activity);
        }
        return super.onOptionsItemSelected(item);
    }

//    private void getMovies() {
//        isLoading = true;
//        progressBarLoading.setVisibility(View.VISIBLE);
//
//        ApiFactory apiFactory = ApiFactory.getInstance();
//        ApiService apiService = apiFactory.getApiService();
//        Disposable disposable = apiService.getMovieResult(lang,whichOne(methodOfSort), String.valueOf(page),VOTE_COUNT,AVERAGE_VOTE)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<MoviesResult>() {
//                    @Override
//                    public void accept(MoviesResult moviesResult) throws Exception {
//                        ArrayList<Movie> movies = (ArrayList<Movie>) moviesResult.getMovies();
//                        if (movies != null && !movies.isEmpty()) {
//                            if (page == 1) {
//                                viewModel.deleteAll();
//                                movieAdapter.clear();
//                            }
//                            for (Movie movie :
//                                    movies) {
//                                viewModel.insertMovie(movie);
//                            }
//                            movieAdapter.addMovies(movies);
//                            page++;
//                        }
//                        isLoading = false;
//                        progressBarLoading.setVisibility(View.INVISIBLE);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(MainActivity.this, "Error bro: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//        compositeDisposable.add(disposable);
//    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void methodOfSort(boolean isChecked) {
        if (isChecked) {
            textViewTopRated.setTextColor(getResources().getColor(R.color.purple));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
            methodOfSort = 1;  // Average votes
        } else {
            textViewPopularity.setTextColor(getResources().getColor(R.color.purple));
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
            methodOfSort = 0;  // Popularity
        }

        viewModel.downloadMovies(lang, methodOfSort, page);
    }

    public void onClickMostPopular(View view) {
        methodOfSort(false);
        switchSort.setChecked(false);

    }

    public void onClickTopRated(View view) {
        methodOfSort(true);
        switchSort.setChecked(true);
    }
}