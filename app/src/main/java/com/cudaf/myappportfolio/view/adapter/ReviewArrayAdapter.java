package com.cudaf.myappportfolio.view.adapter;

import com.cudaf.myappportfolio.R;
import com.cudaf.myappportfolio.model.Review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ReviewArrayAdapter extends ArrayAdapter<Review> {

    private static final String LOG_TAG = MovieArrayAdapter.class.getSimpleName();

    Context mContext;
    int mLayoutResource;


    public ReviewArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
        this.mLayoutResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);
        MovieViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MovieViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_review, parent, false);
            viewHolder.mAuthorTextView = (TextView) convertView.findViewById(R.id.author);
            viewHolder.mReviewTextView = (TextView) convertView.findViewById(R.id.content);
            viewHolder.mReview = review;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MovieViewHolder) convertView.getTag();
        }
        viewHolder.mAuthorTextView.setText(review.getAuthor());
        viewHolder.mReviewTextView.setText(review.getContent());
        return convertView;
    }

    public class MovieViewHolder {
        public TextView mAuthorTextView;
        public TextView mReviewTextView;
        public Review mReview;
    }


    /*extends RecyclerView.Adapter<ReviewArrayAdapter.ViewHolder>  {

    private List<Review> mReviews = new ArrayList<>();

    public ReviewArrayAdapter() {
    }


    public void setReviews(List<Review> videos) {
        this.mReviews.addAll(videos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_review, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextViewContent.setText(mReviews.get(position).getContent());
        holder.mTextViewAuthor.setText(mReviews.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewContent;
        public TextView mTextViewAuthor;

        public ViewHolder(View v) {
            super(v);
            mTextViewContent = (TextView) v.findViewById(R.id.content);
            mTextViewAuthor = (TextView) v.findViewById(R.id.author);

        }

    }*/

}
