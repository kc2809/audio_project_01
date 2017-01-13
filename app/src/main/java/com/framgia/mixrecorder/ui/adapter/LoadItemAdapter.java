package com.framgia.mixrecorder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.data.model.Song;

import java.util.List;

/**
 * Created by GIAKHANH on 1/12/2017.
 */
public class LoadItemAdapter extends RecyclerView.Adapter<LoadItemAdapter.ViewHolder> {
    private Context mContext;
    private List<Song> mSongList;
    private LayoutInflater mInflater;

    public LoadItemAdapter(Context context, List<Song> songList) {
        mContext = context;
        mSongList = songList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.card_item_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binData(mSongList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongList != null ? mSongList.size() : 0;
    }

    public void updateData(List<Song> listSong) {
        mSongList = listSong;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgvMenu;
        private TextView mTvName;
        private TextView mTvTimeModifed;

        public ViewHolder(View view) {
            super(view);
            mImgvMenu = (ImageView) view.findViewById(R.id.image_menu);
            mTvName = (TextView) view.findViewById(R.id.text_name);
            mTvTimeModifed = (TextView) view.findViewById(R.id.text_time);
        }

        public void binData(Song song) {
            if (song == null) return;
            mTvName.setText(song.getName());
            mTvTimeModifed.setText(song.getStringLastModified());
        }
    }
}
