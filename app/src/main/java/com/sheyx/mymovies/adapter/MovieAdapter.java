package com.sheyx.mymovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sheyx.mymovies.R;
import com.sheyx.mymovies.pojos.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesViewHolder> {

    private List<Movie> movies;
    private OnPosterClick onPosterClickListener;
    private OnReachEndListener onReachEndListener;
    private Context context;

    public MovieAdapter(Context context) {
        movies = new ArrayList<>();
        this.context = context;
    }

    public interface OnPosterClick {
        void onPosterClick(int position);
    }

    public void setOnPosterClickListener(OnPosterClick onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    public interface OnReachEndListener {
        void onReachEnd();
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {

        final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
        final String SMALL_POSTER_SIZE = "w185";

        if (movies.size() >= 20 && position >= movies.size() - 8 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }

        Movie movie = movies.get(position);
        String imageUrl = BASE_POSTER_URL + SMALL_POSTER_SIZE + movie.getPosterPath();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.landscape)
                .into(holder.imageViewSmallPoster);
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewSmallPoster;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.small_movie_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPosterClickListener != null) {
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }
}