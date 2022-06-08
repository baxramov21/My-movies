package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.adapter.ReviewAdapter;
import com.example.mymovies.adapter.TrailerAdapter;
import com.example.mymovies.data.FavouriteMovie;
import com.example.mymovies.data.Movie;

import com.example.mymovies.data.MovieViewModel;
import com.example.mymovies.data.Reviews;
import com.example.mymovies.data.Trailer;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewAddToFavourite;
    private ImageView imageViewBigPoster;
    private TextView tv_originalTitle, tv_title, tv_rating, tv_overview, tv_realise_date;
    private RecyclerView recyclerViewTrailers, recyclerViewReviews;
    private ScrollView scrollViewInfo;

    private int ID;
    private Movie currentMovie;
    private FavouriteMovie favouriteMovie;
    private MovieViewModel viewModel;

    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavourite);
        tv_originalTitle = findViewById(R.id.textViewOriginalTitle);
        tv_title = findViewById(R.id.textViewTitle);
        tv_rating = findViewById(R.id.textViewRating);
        lang = Locale.getDefault().getLanguage();
        tv_overview = findViewById(R.id.textViewOverView);
        tv_realise_date = findViewById(R.id.textViewDateOfRealise);
        scrollViewInfo = findViewById(R.id.scrollViewInfo);

        Intent intent_from_MA = getIntent();
        if (intent_from_MA != null && intent_from_MA.hasExtra("ID")) {
            ID = intent_from_MA.getIntExtra("ID", -1);
        } else {
            finish();
        }
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        currentMovie = viewModel.getMovieById(ID);
        Picasso.get().load(currentMovie.getBigPosterPath()).placeholder(R.drawable.landscape).into(imageViewBigPoster);
        tv_originalTitle.setText(currentMovie.getOriginalTitle());
        tv_title.setText(currentMovie.getTitle());
        tv_overview.setText(currentMovie.getOverView());
        tv_rating.setText(Double.toString(currentMovie.getVoteAverage()));
        tv_realise_date.setText(currentMovie.getReleaseDate());
        favouriteAddTo();
        recyclerViewReviews = findViewById(R.id.recyclerview_reviews);
        recyclerViewTrailers = findViewById(R.id.recycler_view_trailers);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        TrailerAdapter trailerAdapter = new TrailerAdapter();
        recyclerViewTrailers.setAdapter(trailerAdapter);
        trailerAdapter.setOnClickPlayVideo(url -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        JSONObject jsonObjectForVideos = NetworkUtils.getJSONObjectForVideos(currentMovie.getId(),lang);
        ArrayList<Trailer> trailers = JSONUtils.getTrailersFromJSON(jsonObjectForVideos);
        trailerAdapter.setTrailers(trailers);
        scrollViewInfo.smoothScrollTo(0,0);
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

    public void onClickChangeFavourite(View view) {
        if (favouriteMovie == null) {
            viewModel.insertFavouriteMovie(new FavouriteMovie(currentMovie));
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, "Удалено из избраного", Toast.LENGTH_SHORT).show();
        }
        favouriteAddTo();
    }

    private void favouriteAddTo() {
        favouriteMovie = viewModel.getFavouriteMovieById(ID);
        if (favouriteMovie == null) {
            imageViewAddToFavourite.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            imageViewAddToFavourite.setImageResource(android.R.drawable.btn_star_big_on);
        }
    }
}