package com.example.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.mymovies.pojos.Movie;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie {
    public FavouriteMovie(int unique_id,int id, int voteCount, String language, String title, String originalTitle, String overview, String posterPath, String backdropPath, double voteAverage, String releaseDate) {
//        super(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate);
        super(unique_id,false,backdropPath,id,language,originalTitle,overview,7.1,posterPath,releaseDate,title,true,voteAverage,voteCount);
    }


    public FavouriteMovie() {

    }

    @Ignore
    public FavouriteMovie(Movie movie) {
//        super(movie.getId(), movie.getVoteCount(), movie.getTitle(), movie.getOriginalTitle(), movie.getOverview(), movie.getPosterPath(), movie.getBackdropPath(), movie.getVoteAverage(), movie.getReleaseDate());
        super(movie.getObject_id(),false, movie.getBackdropPath(), movie.getId(), movie.getOriginalLanguage(), movie.getOriginalTitle(), movie.getOverview(), movie.getPopularity(), movie.getPosterPath(), movie.getReleaseDate(), movie.getTitle(), movie.isVideo(), movie.getVoteAverage(), movie.getVoteCount());
    }
}
