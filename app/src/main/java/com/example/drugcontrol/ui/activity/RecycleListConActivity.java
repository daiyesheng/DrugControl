package com.example.drugcontrol.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.drugcontrol.R;
import com.example.drugcontrol.ui.adapter.QuickConAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class RecycleListConActivity extends ToolbarActivity {

    protected RecyclerView rvBaseRecycler;
    protected SmartRefreshLayout srlBaseHttpRecycler;

    public static void start(Context context) {
        Intent starter = new Intent(context, RecycleListConActivity.class);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_recycle_list_con;
    }

    @Override
    public Object initPresenter() {
        return null;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        super.initView(savedInstanceState, contentView);
        initTitle(getResources().getString(R.string.my_consultation));
        initBackClick(NO_RES, view -> finish());
        rvBaseRecycler = findViewById(R.id.rvBaseRecycler);
        rvBaseRecycler.setLayoutManager(new LinearLayoutManager(this));
        srlBaseHttpRecycler = findViewById(R.id.srlBaseHttpRecycler);
        List<Data> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Data date = new Data();
            date.name = "name" + i;
            date.tag = "tag" + i;
            date.address = "address" + i;
            date.profession = "profession" + i;
            date.patient = "patient" + i;
            date.date = "2019.09.0" + i;
            list.add(date);
        }
        QuickConAdapter adapter = new QuickConAdapter(R.layout.item_my_consultation, list,false);
        adapter.openLoadAnimation();// 一行代码搞定（默认为渐显效果）
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.d("onItemClick: ");
                ToastUtils.showShort("onItemClick" + position);
            }
        });

        rvBaseRecycler.setAdapter(adapter);

        srlBaseHttpRecycler.autoRefresh();
    }

    @Override
    public void doBusiness() {


    }

    public static class Data {
        public String name;
        public String tag;
        public String address;
        public String profession;
        public String patient;
        public String date;
    }
}
