package com.cudaf.myappportfolio.view;

import com.cudaf.myappportfolio.model.Movie;

import android.app.Application;

import ru.noties.storm.DatabaseManager;
import ru.noties.storm.Storm;

/**
 * Created by cudaf on 29/02/2016.
 */
public class MoviesApplication extends Application {


    private static DatabaseManager manager;

    private static MoviesApplication mMoviesApplication;

    public static DatabaseManager getDbManager() {
        return manager;
    }

    public static MoviesApplication getMoviesApplication() {
        return mMoviesApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMoviesApplication = this;
        Storm.getInstance().init(this, true);
        manager = new DatabaseManager(
            this,
            "movies", 1,
            new Class<?>[]{Movie.class});
        //enable debug mode for now
    }

}
