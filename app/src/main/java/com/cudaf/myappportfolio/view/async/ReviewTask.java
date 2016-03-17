package com.cudaf.myappportfolio.view.async;

import com.cudaf.myappportfolio.R;
import com.cudaf.myappportfolio.data.remote.MovieAPIService;
import com.cudaf.myappportfolio.data.remote.MovieAPIServiceClient;
import com.cudaf.myappportfolio.model.Response;
import com.cudaf.myappportfolio.model.Review;
import com.cudaf.myappportfolio.util.Utility;
import com.cudaf.myappportfolio.view.fragment.OnTaskCompleted;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

/**
 * Created by cudaf on 17/02/2016.
 */
public class ReviewTask extends AsyncTask<Object, Review, List<Review>> {

    private Integer mMovieId;
    private Context mContext;
    private OnTaskCompleted mOnTaskCompleted;

    public ReviewTask(Context context, OnTaskCompleted onTaskCompleted) {
        mContext = context;
        mOnTaskCompleted = onTaskCompleted;
    }

    @Override
    protected List<Review> doInBackground(Object... params) {
        List<Review> videosResult = null;
        if (params.length > 0) {
            mMovieId = (Integer) params[0];
        }
        if (Utility.isConnected(mContext)) {
            MovieAPIService movieAPIService = MovieAPIServiceClient.createService(MovieAPIService.class);
            Call<Response<Review>> call = movieAPIService.getMovieReviews(mMovieId,
                mContext.getResources().getString(R.string.api_key));
            try {
                videosResult = call.clone().execute().body().getResults();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return videosResult;
    }

    @Override
    protected void onPostExecute(List<Review> videos) {
        mOnTaskCompleted.onTaskCompleted(videos);

    }
}