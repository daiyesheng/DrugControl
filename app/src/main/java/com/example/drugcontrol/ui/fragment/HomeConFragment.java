package com.example.drugcontrol.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugcontrol.R;
import com.example.drugcontrol.base.BaseFragment;
import com.example.drugcontrol.ui.activity.AnimteSVGAActivity;
import com.example.drugcontrol.ui.activity.SettingMachineActivity;
import com.example.drugcontrol.ui.activity.VideoPlayActivity;

public class HomeConFragment extends BaseFragment {

    private volatile static HomeConFragment instance = null;

    public static HomeConFragment getInstance() {
        if (instance == null) {
            instance = new HomeConFragment();
        }
        return instance;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_home_con;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        applyDebouncingClickListener(
                contentView.findViewById(R.id.tvChat),
                contentView.findViewById(R.id.ivSetting),
                contentView.findViewById(R.id.ivMachine)
        );

    }

    @Override
    public void doBusiness() {

    }

    @Override
    public <P> P initPresenter() {
        return null;
    }

    @Override
    public void onDebouncingClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.tvChat:
                VideoPlayActivity.start(mActivity);
                break;
            case R.id.ivSetting:
                SettingMachineActivity.start(mActivity);
                break;
            case R.id.ivMachine:
                AnimteSVGAActivity.start(mActivity);
                break;

            default:
                break;
        }
    }
}
