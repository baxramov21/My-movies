package com.sheyx.mymovies.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sheyx.mymovies.R;
import com.sheyx.mymovies.adapter.MovieAdapter;
import com.sheyx.mymovies.pojos.FavouriteMovie;
import com.sheyx.mymovies.pojos.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        RecyclerView rv_favourite_movies = findViewById(R.id.recycler_view_favourite_movies);
        movieAdapter = new MovieAdapter(this);
        rv_favourite_movies.setLayoutManager(new GridLayoutManager(this, 2));
        rv_favourite_movies.setAdapter(movieAdapter);
        MovieViewModel viewModelMovies = ViewModelProviders.of(this).get(MovieViewModel.class);
        LiveData<List<FavouriteMovie>> movies = viewModelMovies.getFavouriteMovies();
        movies.observe(this, new Observer<List<FavouriteMovie>>() {
            @Override
            public void onChanged(List<FavouriteMovie> favouriteMovies) {
                if (favouriteMovies != null) {
                    movieAdapter.clear();
                    List<Movie> movieList = new ArrayList<>();
                    movieList.addAll(favouriteMovies);
                    movieAdapter.setMovies(movieList);
                }
                checkAreFavouriteFilmsExists();
            }
        });

        movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClick() {
            @Override
            public void onPosterClick(int position) {
                Movie clickedMovie = movieAdapter.getMovies().get(position);
                Intent intentToMovieDetail = new Intent(FavouriteActivity.this, DetailActivity.class);
                intentToMovieDetail.putExtra("ID", clickedMovie.getId());
                startActivity(intentToMovieDetail);
            }
        });
        checkAreFavouriteFilmsExists();
    }

    private void checkAreFavouriteFilmsExists() {
        if (movieAdapter.getItemCount() <= 0) {
            findViewById(R.id.textViewNoFavouriteMovies).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.textViewNoFavouriteMovies).setVisibility(View.INVISIBLE);
        }
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
}