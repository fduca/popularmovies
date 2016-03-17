package com.cudaf.myappportfolio.view.fragment;

import com.cudaf.myappportfolio.R;
import com.cudaf.myappportfolio.model.Movie;
import com.cudaf.myappportfolio.model.Review;
import com.cudaf.myappportfolio.model.Video;
import com.cudaf.myappportfolio.view.MoviesApplication;
import com.cudaf.myappportfolio.view.activity.DetailActivity;
import com.cudaf.myappportfolio.view.adapter.ReviewArrayAdapter;
import com.cudaf.myappportfolio.view.adapter.VideoArrayAdapter;
import com.cudaf.myappportfolio.view.async.FavoriteTask;
import com.cudaf.myappportfolio.view.async.ReviewTask;
import com.cudaf.myappportfolio.view.async.VideoTask;
import com.squareup.picasso.Picasso;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.noties.storm.DatabaseManager;
import ru.noties.storm.Storm;
import ru.noties.storm.exc.StormException;
import ru.noties.storm.query.Selection;


public class DetailFragment extends Fragment implements OnTaskCompleted<Video>, OnDbSearchItemCallback {

    Movie mMovie;
    Video firstVideo;
    @Bind(R.id.movie_title)
    TextView mMovieTitle;
    @Bind(R.id.reviews_view)
    ListView mReviewsView;
    ReviewArrayAdapter mReviewArrayAdapter;
    ShareActionProvider mShareActionProvider;
    RecyclerView mRecyclerViewVideo;
    RecyclerView.LayoutManager mLayoutManagerVideo;
    private VideoArrayAdapter mAdapterVideo;
    private OnTaskCompleted<Review> mReviewOnTaskCompleted = new OnTaskCompleted<Review>() {
        @Override
        public void onTaskCompleted(List<Review> results) {
            if (results != null && results.size() > 0) {
                mReviewArrayAdapter.addAll(results);
                mReviewArrayAdapter.notifyDataSetChanged();
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        if (getArguments() != null && getArguments().containsKey(DetailActivity.MOVIE)) {
            mMovie = (Movie) getArguments().getSerializable(DetailActivity.MOVIE);
        } else {
            mMovie = (Movie) getActivity().getIntent().getSerializableExtra(DetailActivity.MOVIE);
        }
        if (mMovie != null) {
            new VideoTask(getActivity().getApplicationContext(), this).execute(mMovie.getId());
            new ReviewTask(getActivity().getApplicationContext(), mReviewOnTaskCompleted).execute(mMovie.getId());
            new FavoriteTask(getActivity().getApplicationContext(), this).execute(mMovie.getId());
        }
        mReviewArrayAdapter = new ReviewArrayAdapter(getActivity().getApplicationContext(), R.layout.item_review);
        mReviewsView.setAdapter(mReviewArrayAdapter);
        View header = LayoutInflater.from(getActivity().getApplicationContext()).inflate(
            R.layout.header_details, null, false);
        mReviewsView.addHeaderView(header);
        mRecyclerViewVideo = (RecyclerView) header.findViewById(R.id.my_recycler_view);
        mRecyclerViewVideo.setHasFixedSize(true);
        mLayoutManagerVideo = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewVideo.setLayoutManager(mLayoutManagerVideo);
        mAdapterVideo = new VideoArrayAdapter(new OnItemClickListener() {
            @Override
            public void onItemClick(Video item) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(String.format(getResources().getString(R.string.youtube), item.getKey())));
                startActivity(i);
            }
        });
        mRecyclerViewVideo.setAdapter(mAdapterVideo);
        mMovieTitle.setText(mMovie.getOriginalTitle());
        ImageView mMovieImage = (ImageView) header.findViewById(R.id.movie_image);
        Picasso.with(getActivity().getApplicationContext())
            .load(mMovie.getFullPosterPath()).error(R.drawable.error).placeholder(R.drawable.placeholder).into(mMovieImage);
        TextView mMovieSynopsis = (TextView) header.findViewById(R.id.movie_synopsis);
        mMovieSynopsis.setText(mMovie.getOverview());
        TextView mMovieRating = (TextView) header.findViewById(R.id.movie_rating);
        mMovieRating.setText(String.format(getResources().getString(R.string.movie_rating), mMovie.getVoteAverage()));
        TextView mMovieYear = (TextView) header.findViewById(R.id.movie_year);
        mMovieYear.setText(String.format(getResources().getString(R.string.movie_date), getYear(mMovie.getReleaseDate())));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mMovie.isFavorite()) {
            menu.getItem(0).setIcon(R.drawable.ic_star_white_48dp);
        }
        MenuItem item = menu.findItem(R.id.action_share);
        String videoUrl = firstVideo != null ? String.format(getResources().getString(R.string.youtube), firstVideo.getKey()) : mMovie.getOriginalTitle();
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Create the share Intent
        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
            .setType("text/plain").setText(videoUrl).getIntent();
        // Set the share Intent
        mShareActionProvider.setShareIntent(shareIntent);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favourite:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseManager databaseManager = MoviesApplication.getDbManager();
                        try {
                            databaseManager.open();
                            if (!mMovie.isFavorite()) {
                                Storm.newDelete(databaseManager).delete(Movie.class, Selection.eq("id", mMovie.getId()));
                            } else {
                                Storm.newInsert(databaseManager).insert(mMovie);
                            }
                        } catch (SQLiteException exception) {
                            Log.e("MovieApp", exception.toString());
                        } catch (StormException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                mMovie.setFavorite(!mMovie.isFavorite());
                if (mMovie.isFavorite()) {
                    item.setIcon(R.drawable.ic_star_white_48dp);
                } else {
                    item.setIcon(R.drawable.ic_action_star);
                }
                break;
            case R.id.action_share:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getYear(String releaseDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse;
        try {
            parse = sdf.parse(releaseDate);
            Calendar c = Calendar.getInstance();
            c.setTime(parse);
            return "" + c.get(Calendar.YEAR);
        } catch (ParseException e) {
            return "";
        }
    }

    @Override
    public void onTaskCompleted(List<Video> results) {
        if (results != null && results.size() > 0) {
            firstVideo = results.get(0);
            mAdapterVideo.setVideos(results);
            mAdapterVideo.notifyItemInserted(mAdapterVideo.getItemCount() - 1);
        }
    }

    @Override
    public void onItemDb(Boolean exist) {
        mMovie.setFavorite(exist);
        getActivity().invalidateOptionsMenu();
    }

    public void setMovie(Movie movie) {
        mMovie = movie;
    }
}
