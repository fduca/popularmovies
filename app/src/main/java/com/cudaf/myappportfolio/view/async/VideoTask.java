package com.cudaf.myappportfolio.view.async;

import com.cudaf.myappportfolio.R;
import com.cudaf.myappportfolio.data.remote.MovieAPIService;
import com.cudaf.myappportfolio.data.remote.MovieAPIServiceClient;
import com.cudaf.myappportfolio.model.Response;
import com.cudaf.myappportfolio.model.Video;
import com.cudaf.myappportfolio.util.Utility;
import com.cudaf.myappportfolio.view.fragment.OnTaskCompleted;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

/**
 * Created by cudaf on 16/02/2016.
 */
public class VideoTask extends AsyncTask<Object, Video, List<Video>> {

    private Integer mMovieId;
    private Context mContext;
    private OnTaskCompleted mOnTaskCompleted;

    public VideoTask(Context context, OnTaskCompleted onTaskCompleted) {
        mContext = context;
        mOnTaskCompleted = onTaskCompleted;
    }

    @Override
    protected List<Video> doInBackground(Object... params) {
        List<Video> videosResult = null;
        if (params.length > 0) {
            mMovieId = (Integer) params[0];
        }
        if (Utility.isConnected(mContext)) {
            MovieAPIService movieAPIService = MovieAPIServiceClient.createService(MovieAPIService.class);
            Call<Response<Video>> call = movieAPIService.getMovieVideos(mMovieId,
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
    protected void onPostExecute(List<Video> videos) {
        mOnTaskCompleted.onTaskCompleted(videos);

    }
}
