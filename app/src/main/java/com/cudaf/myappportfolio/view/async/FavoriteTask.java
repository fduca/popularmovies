package com.cudaf.myappportfolio.view.async;

import com.cudaf.myappportfolio.model.Movie;
import com.cudaf.myappportfolio.view.MoviesApplication;
import com.cudaf.myappportfolio.view.fragment.OnDbSearchItemCallback;

import android.content.Context;
import android.os.AsyncTask;

import ru.noties.storm.DatabaseManager;
import ru.noties.storm.Storm;
import ru.noties.storm.query.Selection;

/**
 * Created by cudaf on 05/03/2016.
 */
public class FavoriteTask extends AsyncTask<Object, Boolean, Boolean> {

    Integer mMovieId;
    Context mContext;
    private OnDbSearchItemCallback mOnDbSearchItemCallback;

    public FavoriteTask(Context context, OnDbSearchItemCallback onDbSearchItemCallback) {
        mContext = context;
        mOnDbSearchItemCallback = onDbSearchItemCallback;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        if (params.length > 0) {
            mMovieId = (Integer) params[0];
            DatabaseManager mManager = MoviesApplication.getDbManager();
            mManager.open();
            Movie m = Storm.newSelect(mManager).query(Movie.class, Selection.eq("id", mMovieId));
            if (m != null) {
                return true;
            }
            mManager.close();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mOnDbSearchItemCallback.onItemDb(aBoolean);
    }
}