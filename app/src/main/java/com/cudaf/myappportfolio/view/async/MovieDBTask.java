package com.cudaf.myappportfolio.view.async;

import com.cudaf.myappportfolio.R;
import com.cudaf.myappportfolio.data.remote.MovieAPIService;
import com.cudaf.myappportfolio.data.remote.MovieAPIServiceClient;
import com.cudaf.myappportfolio.model.Movie;
import com.cudaf.myappportfolio.model.Response;
import com.cudaf.myappportfolio.util.SortOption;
import com.cudaf.myappportfolio.util.Utility;
import com.cudaf.myappportfolio.view.MoviesApplication;
import com.cudaf.myappportfolio.view.fragment.OnTaskCompleted;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import ru.noties.storm.DatabaseManager;
import ru.noties.storm.Storm;
import ru.noties.storm.StormIterator;
import ru.noties.storm.op.Select;


public class MovieDBTask extends AsyncTask<Object, Movie, List<Movie>> {

    String sorting = SortOption.MOST_POPULAR.getValue();
    DatabaseManager mManager;
    private OnTaskCompleted mOnTaskCompleted;
    private Context mContext;

    public MovieDBTask(Context context, OnTaskCompleted onTaskCompleted) {
        this.mOnTaskCompleted = onTaskCompleted;
        this.mContext = context;
    }

    @Override
    protected List<Movie> doInBackground(Object... params) {
        List<Movie> moviesResult = new ArrayList<>();
        if (params.length > 0) {
            sorting = (String) params[0];
        }
        if (!sorting.equalsIgnoreCase(SortOption.FAVORITES.getValue()) && Utility.isConnected(mContext)) {
            MovieAPIService movieAPIService = MovieAPIServiceClient.createService(MovieAPIService.class);
            Call<Response<Movie>> call = movieAPIService.getMovies(sorting,
                mContext.getResources().getString(R.string.api_key));
            try {
                moviesResult = call.clone().execute().body().getResults();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mManager = MoviesApplication.getDbManager();
            mManager.open();
            Select select = Storm.newSelect(mManager);
            final StormIterator<Movie> iterator = select.queryAllIterator(Movie.class);
            if (iterator != null) {
                for (int i = 0; i < iterator.getCount(); i++) {
                    moviesResult.add(iterator.get(i));
                }
            }
            mManager.close();
        }
        return moviesResult;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        mOnTaskCompleted.onTaskCompleted(movies);

    }
}
