package com.cudaf.myappportfolio.view.adapter;

import com.cudaf.myappportfolio.R;
import com.cudaf.myappportfolio.model.Video;
import com.cudaf.myappportfolio.view.fragment.OnItemClickListener;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cudaf on 15/02/2016.
 */
public class VideoArrayAdapter extends RecyclerView.Adapter<VideoArrayAdapter.ViewHolder> {

    private final OnItemClickListener mOnItemClickListener;
    private List<Video> mVideos = new ArrayList<>();

    public VideoArrayAdapter(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public void setVideos(List<Video> videos) {
        this.mVideos.addAll(videos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_video, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mVideos.get(position), mOnItemClickListener);
        holder.mTextView.setText(mVideos.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.video);
        }

        public void bind(final Video item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}
