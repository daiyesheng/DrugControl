package com.example.drugcontrol.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.example.drugcontrol.R;

public class UserInfoActivity extends ToolbarActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, UserInfoActivity.class);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_user_info;
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        super.initView(savedInstanceState, contentView);
        initTitle(getResources().getString(R.string.my_experter));
        initBackClick(NO_RES, view -> finish());
        initMenuClick(0,null,null,0,"编辑",view -> {
            ToastUtils.showShort("编辑");
        });


    }

    @Override
    public <P> P initPresenter() {
        return null;
    }
}
