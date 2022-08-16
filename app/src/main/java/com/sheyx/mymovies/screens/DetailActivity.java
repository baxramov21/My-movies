package com.sheyx.mymovies.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sheyx.mymovies.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sheyx.mymovies.adapter.ReviewAdapter;
import com.sheyx.mymovies.adapter.TrailerAdapter;
import com.sheyx.mymovies.api.ApiFactory;
import com.sheyx.mymovies.api.ApiService;
import com.sheyx.mymovies.pojos.FavouriteMovie;
import com.sheyx.mymovies.pojos.Review;
import com.sheyx.mymovies.pojos.Movie;

import com.sheyx.mymovies.pojos.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
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
    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String BIG_POSTER_SIZE = "w780";

    private  ApiFactory apiFactory;
    private ApiService apiService;

    private TrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

         apiFactory = ApiFactory.getInstance();
         apiService = apiFactory.getApiService();

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
        Picasso.get().load(BASE_POSTER_URL + BIG_POSTER_SIZE + currentMovie.getPosterPath()).placeholder(R.drawable.landscape).into(imageViewBigPoster);
        tv_originalTitle.setText(currentMovie.getOriginalTitle());
        tv_title.setText(currentMovie.getTitle());
        tv_overview.setText(currentMovie.getOverview());
        tv_rating.setText(String.format(Locale.getDefault(), "%s", currentMovie.getVoteAverage()));
        tv_realise_date.setText(currentMovie.getReleaseDate());
        favouriteAddTo();
        ArrayList<Trailer> trailerArrayList = new ArrayList<>();
        recyclerViewReviews = findViewById(R.id.recyclerview_reviews);
        recyclerViewTrailers = findViewById(R.id.recycler_view_trailers);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        trailerAdapter = new TrailerAdapter();
        trailerAdapter.setTrailers(trailerArrayList);

        LiveData<List<Trailer>> trailers =  viewModel.getTrailers();
        trailers.observe(this, trailers12 -> {
            ArrayList<Trailer> trailers1 = (ArrayList<Trailer>) trailers12;
            recyclerViewTrailers.setAdapter(trailerAdapter);
                    trailerAdapter.setTrailers(trailers1);

                    trailerAdapter.setOnClickPlayVideo(url -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW,  Uri.parse( "https://www.youtube.com/watch?v=" + url));
                        Log.d("url","https://www.youtube.com/watch?v=" + url);
                        startActivity(intent);
                    });
        });

        LiveData<List<Review>> reviews = viewModel.getReviews();
        reviews.observe(this, reviews1 -> {
            ArrayList<Review> reviews2 = (ArrayList<Review>) reviews1;
            ReviewAdapter reviewAdapter = new ReviewAdapter();
            recyclerViewReviews.setAdapter(reviewAdapter);
            reviewAdapter.setTrailers(reviews2);
        });

        viewModel.downloadTrailers(currentMovie.getId(),lang);
        viewModel.downloadReviews(currentMovie.getId(),lang);
        scrollViewInfo.smoothScrollTo(0, 0);
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