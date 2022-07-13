package com.example.mymovies.api;

import com.example.mymovies.pojos.MoviesResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie?api_key=c04e9d2cd0bd48b727a09adffca7e5ef")
    Observable<MoviesResult> getMovieResult(@Query("language") String language, @Query("sort_by") String sortBy, @Query("page") String page, @Query("vote_count.gte") String voteCount,@Query("vote_average.gte") String average_vote);
}
