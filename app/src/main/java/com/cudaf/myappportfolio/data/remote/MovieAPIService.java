package com.cudaf.myappportfolio.data.remote;

import com.cudaf.myappportfolio.model.Movie;
import com.cudaf.myappportfolio.model.Response;
import com.cudaf.myappportfolio.model.Review;
import com.cudaf.myappportfolio.model.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by cudaf on 13/02/2016.
 */
public interface MovieAPIService {

    String SORT_PARAM = "sort_by";
    String API_KEY_PARAM = "api_key";
    String MOVIES_BASE_URL = "http://api.themoviedb.org/";

    @GET("/3/discover/movie")
    Call<Response<Movie>> getMovies(@Query(SORT_PARAM) String sort, @Query(API_KEY_PARAM) String apiKey);

    @GET("/3/movie/{id}/videos")
    Call<Response<Video>> getMovieVideos(@Path("id") Integer id, @Query(API_KEY_PARAM) String apiKey);

    @GET("/3/movie/{id}/reviews")
    Call<Response<Review>> getMovieReviews(@Path("id") Integer id, @Query(API_KEY_PARAM) String apiKey);
}
