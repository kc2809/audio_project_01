package com.framgia.mixrecorder.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.data.model.Song;
import com.framgia.mixrecorder.ui.activity.MenuActivity;
import com.framgia.mixrecorder.ui.adapter.LoadItemAdapter;
import com.framgia.mixrecorder.utils.Constant;

import java.util.List;

public class RecordingsFragment extends Fragment
    implements LoadItemAdapter.OnRecyclerInteractListener, SwipeRefreshLayout.OnRefreshListener {
    private final int REQUEST_CODE_ASK_PERMISSIONS = 2;
    private RecyclerView mRecyclerView;
    private LoadItemAdapter mAdapter;
    private RelativeLayout mRelativeFooter;
    private PlayerFragment mPlayerFragment;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public RecordingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recordings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycle_view_recordings);
        mRecyclerView.setHasFixedSize(true);
        requestWriteExternalStoragePermission();
        initRelativeFooter();
        initSwipeRefresh();
    }

    private void initSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
    }

    private void initRelativeFooter() {
        mRelativeFooter = (RelativeLayout) getView().findViewById(R.id.relative_player_footer);
        mRelativeFooter.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        List<Song> listSong = Song.getSongs(getContext());
        mAdapter = new LoadItemAdapter(getActivity(), listSong, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void requestWriteExternalStoragePermission() {
        int hasWriteContactsPermission =
            ContextCompat
                .checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            setupRecyclerView();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission
                .WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void playSong(Song song) {
        if (mPlayerFragment == null) {
            mPlayerFragment = new PlayerFragment();
        }
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.relative_player_footer, mPlayerFragment).commit();
        mRelativeFooter.setVisibility(View.VISIBLE);
        mPlayerFragment.playAudio(song);
    }

    public void hideFragment() {
        mRelativeFooter.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupRecyclerView();
                } else {
                    Toast.makeText(getActivity(),
                        getContext().getString(R.string.permission_denied_string),
                        Toast.LENGTH_SHORT)
                        .show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRecyclerInteract(Song song, int requestCode) {
        if (requestCode == Constant.REQUEST_PLAY_AUDIO) {
            playSong(song);
        } else {
            getActivity().startActivityForResult(MenuActivity.getMenuIntent(getContext(), song),
                Constant.REQUEST_OPEN_MENU_ACTIVITY);
        }
    }

    @Override
    public void onRefresh() {
        refresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void refresh() {
        mAdapter.updateData(Song.getSongs(getContext()));
    }
}
