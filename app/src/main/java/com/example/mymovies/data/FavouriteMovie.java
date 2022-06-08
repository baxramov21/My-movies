package com.example.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie {


    public FavouriteMovie(int uniqueId, int id, int voteCount, double voteAverage, String title, String overView, String bigPosterPath, String posterPath, String releaseDate, String backdropPath, String originalTitle) {
        super(uniqueId, id, voteCount, voteAverage, title, overView, bigPosterPath, posterPath, releaseDate, backdropPath, originalTitle);
    }

    @Ignore
    public FavouriteMovie(Movie movie) {
        super(movie.getUniqueId(), movie.getId(), movie.getVoteCount(), movie.getVoteAverage(), movie.getTitle(), movie.getOverView(), movie.getBigPosterPath(), movie.getPosterPath(), movie.getReleaseDate(), movie.getBackdropPath(), movie.getOriginalTitle());
    }
}
