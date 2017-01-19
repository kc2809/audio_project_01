package com.framgia.mixrecorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.framgia.mixrecorder.R;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mLinearRecord;
    private LinearLayout mLinearMixAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initViews();
    }

    private void initViews() {
        mLinearMixAudio = (LinearLayout) findViewById(R.id.linear_mix);
        mLinearRecord = (LinearLayout) findViewById(R.id.linear_record);
        mLinearRecord.setOnClickListener(this);
        mLinearMixAudio.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_record:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.linear_mix:
                mixAudio();
                break;
            default:
                break;
        }
    }

    private void mixAudio() {
        //todo mix audio function
    }
}
