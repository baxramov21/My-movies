package com.example.mymovies.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.mymovies.R;
import com.example.mymovies.adapter.ReviewAdapter;
import com.example.mymovies.adapter.TrailerAdapter;
import com.example.mymovies.api.ApiFactory;
import com.example.mymovies.api.ApiService;
import com.example.mymovies.data.FavouriteMovie;
import com.example.mymovies.data.Reviews;
import com.example.mymovies.pojos.Movie;

import com.example.mymovies.data.MovieViewModel;
import com.example.mymovies.data.Trailer;
import com.example.mymovies.pojos.ReviewResult;
import com.example.mymovies.pojos.TrailersResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    private String methodOfSort;
    private String page;
    private static final String VOTE_COUNT = "1000";
    private static final String AVERAGE_VOTE = "7";
    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE = "w185";
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
//        if (trailers != null) {
//            TrailerAdapter trailerAdapter = new TrailerAdapter();
//            recyclerViewTrailers.setAdapter(trailerAdapter);
//            trailerAdapter.setTrailers(trailers);
//
//            trailerAdapter.setOnClickPlayVideo(url -> {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//            });
//        }

        getReviews();
        getTrailers();
        scrollViewInfo.smoothScrollTo(0, 0);
    }

    private void getReviews() {
       Disposable disposable =  apiService.getReviewsResult(String.valueOf(currentMovie.getId()),lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReviewResult>() {
                    @Override
                    public void accept(ReviewResult reviewResult) throws Exception {
                        Log.d("problem","Is in adapter");
                        ArrayList<Reviews> reviews = (ArrayList<Reviews>) reviewResult.getResults();
                        ReviewAdapter reviewAdapter = new ReviewAdapter();
                        recyclerViewReviews.setAdapter(reviewAdapter);
                        reviewAdapter.setTrailers(reviews);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(DetailActivity.this, "Error bro", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getTrailers() {
        Disposable disposable = apiService.getTrailersResult(String.valueOf(currentMovie.getId()), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TrailersResult>() {
                    @Override
                    public void accept(TrailersResult result) throws Exception {
                        ArrayList<Trailer> trailers = (ArrayList<Trailer>) result.getResults();
                        Log.d("size",trailers.size() + " size");
                        recyclerViewTrailers.setAdapter(trailerAdapter);
                        trailerAdapter.setTrailers(trailers);

                        trailerAdapter.setOnClickPlayVideo(url -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW,  Uri.parse( "https://www.youtube.com/watch?v=" + url));
                            startActivity(intent);
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
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