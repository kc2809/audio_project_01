package com.framgia.mixrecorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.ui.adapter.ViewPagerAdapter;
import com.framgia.mixrecorder.ui.fragment.PlayerFragment;
import com.framgia.mixrecorder.ui.fragment.RecordFragment;
import com.framgia.mixrecorder.ui.fragment.RecordingsFragment;
import com.framgia.mixrecorder.utils.Constant;

public class MainActivity extends AppCompatActivity
    implements PlayerFragment.OnPlayerFragInteractListener,
    RecordFragment.OnRecordFragmentListener {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupTablayout();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
    }

    private void setupTablayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentManager manager = getSupportFragmentManager();
        mAdapter = new ViewPagerAdapter(manager, this);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onPlayerFragInteract() {
        RecordingsFragment fragment =
            (RecordingsFragment) mAdapter.getItem(ViewPagerAdapter.FRAGMENT_RECORDINGS);
        fragment.hideFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_OPEN_MENU_ACTIVITY && resultCode == RESULT_OK) {
            refreshRecodingTab();
        }
    }

    private void refreshRecodingTab() {
        RecordingsFragment fragment =
            (RecordingsFragment) mAdapter.getItem(ViewPagerAdapter.FRAGMENT_RECORDINGS);
        fragment.refresh();
    }

    @Override
    public void onRecordFragmentChange() {
        refreshRecodingTab();
    }
}