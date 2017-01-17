package com.framgia.mixrecorder.data.model;

public class ItemMenu {
    private int mImageResourceId ;
    private String mTitle;

    public ItemMenu() {
    }

    public ItemMenu(int imageResourceId, String title) {
        mImageResourceId = imageResourceId;
        mTitle = title;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        mImageResourceId = imageResourceId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
