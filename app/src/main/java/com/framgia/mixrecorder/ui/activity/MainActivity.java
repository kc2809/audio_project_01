package com.framgia.mixrecorder.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.framgia.mixrecorder.R;
import com.framgia.mixrecorder.ui.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {
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
}
