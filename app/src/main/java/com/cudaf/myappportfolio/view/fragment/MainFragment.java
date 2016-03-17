package com.cudaf.myappportfolio.view.fragment;

import com.cudaf.myappportfolio.R;
import com.cudaf.myappportfolio.model.Movie;
import com.cudaf.myappportfolio.util.SortOption;
import com.cudaf.myappportfolio.view.activity.DetailActivity;
import com.cudaf.myappportfolio.view.adapter.MovieArrayAdapter;
import com.cudaf.myappportfolio.view.async.MovieDBTask;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;


public class MainFragment extends Fragment implements OnTaskCompleted<Movie>, OnSortCallback, AdapterView.OnItemClickListener {

    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "DETAIL_FRAGMENT";
    GridView mGridView;
    List<Movie> mMovies;
    private MovieArrayAdapter mMovieArrayAdapter;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mMovieArrayAdapter = new MovieArrayAdapter(getActivity(), R.layout.item_movie);
        mGridView = (GridView) rootView.findViewById(R.id.gridview);
        mGridView.setAdapter(mMovieArrayAdapter);
        mGridView.setOnItemClickListener(this);
        mTwoPane = getResources().getBoolean(R.bool.twoPane);
        MovieDBTask movieDBTask = new MovieDBTask(this.getActivity(), this);
        movieDBTask.execute();
        return rootView;
    }

    @Override
    public void onTaskCompleted(List<Movie> movies) {
        mMovies = movies;
        mMovieArrayAdapter.clear();
        mMovieArrayAdapter.addAll(movies);
        mMovieArrayAdapter.notifyDataSetChanged();
        if (mMovies != null && mMovies.size() > 0) {
            refreshDetailView(mMovies.get(0));
        }
    }

    @Override
    public void onSort(SortOption option) {
        new MovieDBTask(this.getActivity(), this).execute(option.getValue());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mTwoPane) {
            refreshDetailView(mMovies.get(position));
        } else {
            Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
            detailIntent.putExtra(DetailActivity.MOVIE, mMovies.get(position));
            startActivity(detailIntent);
        }
    }

    private void refreshDetailView(Movie movie) {
        Bundle arg = new Bundle();
        arg.putSerializable(DetailActivity.MOVIE, movie);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(arg);
        getFragmentManager().beginTransaction()
            .replace(R.id.movie_details_container, detailFragment, MOVIE_DETAILS_FRAGMENT_TAG)
            .commit();
    }
}
