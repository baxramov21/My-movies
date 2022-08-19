package com.sheyx.mymovies.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sheyx.mymovies.R;
import com.sheyx.mymovies.adapter.MovieAdapter;
import com.sheyx.mymovies.pojos.Movie;
import com.sheyx.mymovies.pojos.SearchResult;
import com.sheyx.mymovies.screens.DetailActivity;
import com.sheyx.mymovies.screens.FavouriteActivity;
import com.sheyx.mymovies.screens.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

public class MoviesListActivity extends AppCompatActivity {
    private Switch switchSort;
    private TextView textViewPopularity;
    private TextView textViewTopRated;
    private SearchView searchView;
    private ProgressBar progressBarLoading;

    private MovieViewModel viewModel;
    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private int page = 1;
    private int methodOfSort;
    private boolean isLoading = false;

    private String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list_main);
        switchSort = findViewById(R.id.switchSort);
        textViewPopularity = findViewById(R.id.textViewMostPopular);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        language = Locale.getDefault().getLanguage();
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, getWindowWidth()));
        movieAdapter = new MovieAdapter(this);
        recyclerViewPosters.setAdapter(movieAdapter);

        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        switchSort.setChecked(true);
        onCheckedChanged();
        methodOfSort(false);
        onPosterClick();

        movieAdapter.setOnReachEndListener(() -> {
            if (!isLoading) {
                progressBarLoading.setVisibility(View.VISIBLE);
                page++;
                viewModel.downloadMovies(language, methodOfSort, page);
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
                Intent intentToMovieDetail = new Intent(MoviesListActivity.this, DetailActivity.class);
                intentToMovieDetail.putExtra("ID", clickedMovie.getId());
                startActivity(intentToMovieDetail);
            }
        });
    }

    private void onDataChanged() {
        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, movies -> {
            if (movies != null) {
                movieAdapter.clear();
                movieAdapter.addMovies(movies);
                page++;
            }
            isLoading = false;
            progressBarLoading.setVisibility(View.INVISIBLE);
        });

        if (isLoading) {
            viewModel.downloadMovies(language, methodOfSort, page);
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
        if (id == R.id.main_movies_menu_item) {
            Intent intent_to_MA = new Intent(this, MoviesListActivity.class);
            startActivity(intent_to_MA);
        } else {
            Intent intent_to_favourite_activity = new Intent(this, FavouriteActivity.class);
            startActivity(intent_to_favourite_activity);
        }
        return super.onOptionsItemSelected(item);
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

        viewModel.downloadMovies(language, methodOfSort, page);
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