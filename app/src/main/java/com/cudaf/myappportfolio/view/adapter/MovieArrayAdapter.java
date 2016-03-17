package com.cudaf.myappportfolio.view.adapter;

import com.cudaf.myappportfolio.R;
import com.cudaf.myappportfolio.model.Movie;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by cudaf on 29/01/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieArrayAdapter.class.getSimpleName();

    Context mContext;
    int mLayoutResource;


    public MovieArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
        this.mLayoutResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        MovieViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MovieViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.poster_img);
            viewHolder.movie = movie;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MovieViewHolder) convertView.getTag();
        }
        Picasso.with(mContext)
            .load(movie.getFullPosterPath()).into(viewHolder.mImageView);
        return convertView;
    }

    public class MovieViewHolder {
        public ImageView mImageView;
        public Movie movie;
    }
}
