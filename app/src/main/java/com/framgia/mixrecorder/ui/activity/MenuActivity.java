package com.framgia.mixrecorder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.data.model.ItemMenu;
import com.framgia.mixrecorder.data.model.Song;
import com.framgia.mixrecorder.ui.adapter.MenuAdapter;
import com.framgia.mixrecorder.ui.fragment.DeleteDialogFragment;
import com.framgia.mixrecorder.ui.fragment.RenameDialogFragment;
import com.framgia.mixrecorder.utils.Constant;
import com.framgia.mixrecorder.utils.MediaStoreProcess;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements MenuAdapter.OnInteractListener,
    RenameDialogFragment.RenameDialogListener, DeleteDialogFragment.DeleteDialogListener {
    public static final int EDIT_FILE_NAME = 0;
    public static final int DELETE_FILE = 1;
    public static final int CROP_AUDIO = 2;
    public static final int SHARE_AUDIO = 3;
    public static final String SONG_DATA = "SONG_DATA";
    public static final String BUNDLE_DATA = "BUNDLE_DATA";
    private RecyclerView mRecyclerView;
    private MenuAdapter mAdapter;
    private List<ItemMenu> mMenuList;
    private Song mSong;

    public static Intent getMenuIntent(Context context, Song song) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SONG_DATA, song);
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra(BUNDLE_DATA, bundle);
        return intent;
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(BUNDLE_DATA);
        mSong = (Song) bundle.getSerializable(SONG_DATA);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_menu);
        initData();
        mAdapter = new MenuAdapter(this, mMenuList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mMenuList = new ArrayList<>();
        mMenuList.add(new ItemMenu(R.drawable.ic_edit, getString(R.string.title_rename)));
        mMenuList.add(new ItemMenu(R.drawable.ic_delete, getString(R.string.title_delete_audio)));
        mMenuList.add(new ItemMenu(R.drawable.ic_share, getString(R.string.title_share_audio)));
        mMenuList.add(new ItemMenu(R.drawable.ic_crop, getString(R.string.title_crop_audio)));
    }

    @Override
    public void onInteract(int action) {
        switch (action) {
            case EDIT_FILE_NAME:
                editFileName();
                break;
            case DELETE_FILE:
                deleteAudioFile();
                break;
            case CROP_AUDIO:
                cropAudio();
                break;
            case SHARE_AUDIO:
                shareAudio();
                break;
            default:
                break;
        }
    }

    private void deleteAudioFile() {
        DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
        FragmentManager manager = getSupportFragmentManager();
        deleteDialogFragment.show(manager, Constant.DELETE_DIALOG_FRAGMENT);
    }

    private void editFileName() {
        RenameDialogFragment renameDialogFragment = RenameDialogFragment.newInstance(mSong
            .getName());
        FragmentManager manager = getSupportFragmentManager();
        renameDialogFragment.show(manager, Constant.RENAME_DIALOG_FRAGMENT);
    }

    @Override
    public void onDialogRenameClick(String newName) {
        if (MediaStoreProcess.renameAudioFile(this, mSong.getPath(), newName)) {
            setResultChange();
            Toast.makeText(this, R.string.msg_rename_success, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.msg_rename_fail, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogPositiveClick() {
        if (MediaStoreProcess.deleteAudioFile(this, mSong.getPath())) {
            setResultChange();
            Toast.makeText(this, R.string.msg_delete_success, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.msg_delete_fail, Toast.LENGTH_LONG).show();
        }
    }

    private void setResultChange() {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void shareAudio() {
        //todo share audio
    }

    private void cropAudio() {
        //todo crop audio
    }
}
