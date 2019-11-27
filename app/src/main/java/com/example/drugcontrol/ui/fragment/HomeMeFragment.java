package com.example.drugcontrol.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugcontrol.R;
import com.example.drugcontrol.base.BaseFragment;
import com.example.drugcontrol.ui.activity.LoginActivity;
import com.example.drugcontrol.ui.activity.RecycleListConActivity;
import com.example.drugcontrol.ui.activity.RecycleListReportActivity;
import com.example.drugcontrol.ui.activity.RecycleListTreatmentActivity;
import com.example.drugcontrol.ui.activity.RecycleTabListExpertActivity;
import com.example.drugcontrol.ui.activity.SettingActivity;
import com.example.drugcontrol.ui.activity.UserInfoActivity;

public class HomeMeFragment extends BaseFragment {

    private static HomeMeFragment instance = null;

    public static HomeMeFragment getInstance() {
        if (instance == null) {
            instance = new HomeMeFragment();
        }
        return instance;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_home_me;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        applyDebouncingClickListener(
                contentView.findViewById(R.id.ll_avatar),
                contentView.findViewById(R.id.tvUserSetting),
                contentView.findViewById(R.id.ll_login),
                contentView.findViewById(R.id.rb_consultation),
                contentView.findViewById(R.id.rb_experter),
                contentView.findViewById(R.id.rb_reporter),
                contentView.findViewById(R.id.rb_suggestion),
                contentView.findViewById(R.id.rl_myfile),
                contentView.findViewById(R.id.rl_myappointment),
                contentView.findViewById(R.id.rl_myworker),
                contentView.findViewById(R.id.rl_tester)
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
        switch (view.getId()){
            case R.id.ll_avatar:
                break;
            case R.id.tvUserSetting:
                SettingActivity.start(mActivity);
                break;
            case R.id.ll_login:
                LoginActivity.start(mActivity);
                break;
            case R.id.rb_consultation:
                RecycleListConActivity.start(mActivity);
                break;
            case R.id.rb_experter:
                RecycleTabListExpertActivity.start(mActivity);
                break;
            case R.id.rb_reporter:
                RecycleListReportActivity.start(mActivity);
                break;
            case R.id.rb_suggestion:
                RecycleListTreatmentActivity.start(mActivity);
                break;
            case R.id.rl_myfile:
                UserInfoActivity.start(mActivity);
                break;
            case R.id.rl_myappointment:
                break;
            case R.id.rl_myworker:
                break;
            case R.id.rl_tester:
                break;
        }
    }
}
