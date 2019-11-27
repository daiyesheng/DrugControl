package com.example.drugcontrol.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.drugcontrol.R;

public class SettingMachineActivity extends ToolbarActivity {


    public static void start(Context context) {
        Intent starter = new Intent(context, SettingMachineActivity.class);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_setting_machine;
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

    }

    @Override
    public void doBusiness() {

    }
}
