package com.framgia.mixrecorder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.data.model.Song;
import com.framgia.mixrecorder.ui.activity.MenuActivity;

import java.util.List;

/**
 * Created by GIAKHANH on 1/12/2017.
 */
public class LoadItemAdapter extends RecyclerView.Adapter<LoadItemAdapter.ViewHolder> {
    private OnRecyclerInteractListener mListener;
    private Context mContext;
    private List<Song> mSongList;
    private LayoutInflater mInflater;

    public LoadItemAdapter(Context context, List<Song> songList,
                           OnRecyclerInteractListener listener) {
        mContext = context;
        mSongList = songList;
        mInflater = LayoutInflater.from(mContext);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.card_item_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binData(position);
    }

    @Override
    public int getItemCount() {
        return mSongList != null ? mSongList.size() : 0;
    }

    public void updateData(List<Song> listSong) {
        mSongList = listSong;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImgvMenu;
        private TextView mTvName;
        private TextView mTvTimeModifed;
        private RelativeLayout mRelativeMain;

        public ViewHolder(View view) {
            super(view);
            mImgvMenu = (ImageView) view.findViewById(R.id.image_menu);
            mTvName = (TextView) view.findViewById(R.id.text_name);
            mTvTimeModifed = (TextView) view.findViewById(R.id.text_time);
            mRelativeMain = (RelativeLayout) view.findViewById(R.id.relative_main_item);
            mRelativeMain.setOnClickListener(this);
            mImgvMenu.setOnClickListener(this);
        }

        public void binData(int position) {
            Song song = mSongList.get(getAdapterPosition());
            if (song == null) return;
            mTvName.setText(song.getName());
            mTvTimeModifed.setText(song.getStringLastModified());
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.relative_main_item:
                    mListener.onRecyclerInteract(mSongList.get(getAdapterPosition()));
                    break;
                case R.id.image_menu:
                    showMenuDialog();
                    break;
                default:
                    break;
            }
        }

        private void showMenuDialog() {
            mContext.startActivity(
                MenuActivity.getMenuIntent(mContext, mSongList.get(getAdapterPosition())));
        }
    }

    public interface OnRecyclerInteractListener {
        void onRecyclerInteract(Song song);
    }
}