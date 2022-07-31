package com.example.mymovies.api;

import com.example.mymovies.pojos.MoviesResult;
import com.example.mymovies.pojos.ReviewResult;
import com.example.mymovies.pojos.TrailersResult;

import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // vidoes link = https://api.themoviedb.org/3/movie/361743/videos?api_key=c04e9d2cd0bd48b727a09adffca7e5ef&language=en
    // reviews link = https://api.themoviedb.org/3/review/718789?language=en&api_key=c04e9d2cd0bd48b727a09adffca7e5ef
    // https://api.themoviedb.org/3/movie/718789/reviews?api_key=c04e9d2cd0bd48b727a09adffca7e5ef&language=en-US&page=1

    @GET("discover/movie?api_key=c04e9d2cd0bd48b727a09adffca7e5ef")
    Observable<MoviesResult> getMovieResult(@Query("language") String language, @Query("sort_by") String sortBy, @Query("page") String page, @Query("vote_count.gte") String voteCount,@Query("vote_average.gte") String average_vote);

    @GET("movie/{movie_id}/videos?api_key=c04e9d2cd0bd48b727a09adffca7e5ef")
    Observable<TrailersResult> getTrailersResult(@Path("movie_id")String movieId, @Query("language") String language);

    @GET("movie/{movie_id}/reviews?api_key=c04e9d2cd0bd48b727a09adffca7e5ef")
    Observable<ReviewResult> getReviewsResult(@Path("movie_id") String movieId, @Query("language") String language);

}
