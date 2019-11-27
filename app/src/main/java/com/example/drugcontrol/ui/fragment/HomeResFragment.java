package com.example.drugcontrol.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drugcontrol.R;
import com.example.drugcontrol.base.BaseFragment;

public class HomeResFragment extends BaseFragment {

    private static HomeResFragment instance = null;

    public static HomeResFragment getInstance() {
        if (instance == null) {
            instance = new HomeResFragment();
        }
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_home_res;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        applyDebouncingClickListener(
                contentView.findViewById(R.id.cl_psy),
                contentView.findViewById(R.id.cl_medical),
                contentView.findViewById(R.id.cl_legal),
                contentView.findViewById(R.id.cl_worker)
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
            case R.id.cl_psy:
                break;
            case R.id.cl_medical:
                break;
            case R.id.cl_legal:
                break;
            case R.id.cl_worker:
                break;
        }
    }
}
