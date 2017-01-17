package com.framgia.mixrecorder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.data.model.ItemMenu;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context mContext;
    private List<ItemMenu> mMenuItemList;
    private OnInteractListener mListener;

    public MenuAdapter(Context context, List<ItemMenu> menuItemList, OnInteractListener listener) {
        mContext = context;
        mMenuItemList = menuItemList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
            LayoutInflater.from(mContext).inflate(R.layout.item_choice_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binData(position);
    }

    @Override
    public int getItemCount() {
        return mMenuItemList != null ? mMenuItemList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageIcon;
        private TextView mTextTitle;

        public ViewHolder(View view) {
            super(view);
            mImageIcon = (ImageView) view.findViewById(R.id.image_icon);
            mTextTitle = (TextView) view.findViewById(R.id.text_title);
            view.setOnClickListener(this);
        }

        public void binData(int position) {
            ItemMenu item = mMenuItemList.get(getAdapterPosition());
            if (item == null) return;
            mImageIcon.setImageResource(item.getImageResourceId());
            mTextTitle.setText(item.getTitle());
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) mListener.onInteract(getAdapterPosition());
        }
    }

    public interface OnInteractListener {
        void onInteract(int action);
    }
}

