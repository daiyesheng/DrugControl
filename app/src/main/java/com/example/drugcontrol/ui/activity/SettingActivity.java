package com.example.drugcontrol.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugcontrol.R;

public class SettingActivity extends ToolbarActivity {


    public static void start(Context context) {
        Intent starter = new Intent(context, SettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    public Object initPresenter() {
        return null;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        super.initView(savedInstanceState, contentView);
        initTitle(getResources().getString(R.string.machine_setting));
        initBackClick(NO_RES, view -> finish());

        applyDebouncingClickListener(
                findViewById(R.id.fl_unlock),
                findViewById(R.id.fl_cache),
                findViewById(R.id.fl_version),
                findViewById(R.id.fl_policy),
                findViewById(R.id.fl_feedback)
        );
    }

    @Override
    public void doBusiness() {

    }


    @Override
    public void onDebouncingClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.fl_unlock:
                break;
            case R.id.fl_cache:
                break;
            case R.id.fl_version:
                break;
            case R.id.fl_policy:
                break;
            case R.id.fl_feedback:
                break;

            default:
                break;

        }
    }


}
