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
import com.example.drugcontrol.ui.adapter.QuickReportAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class RecycleListReportActivity extends ToolbarActivity {

    protected RecyclerView rvBaseRecycler;
    protected SmartRefreshLayout srlBaseHttpRecycler;

    public static void start(Context context) {
        Intent starter = new Intent(context, RecycleListReportActivity.class);
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
        initTitle(getResources().getString(R.string.my_report));
        initBackClick(NO_RES, view -> finish());
        initView();
        srlBaseHttpRecycler.autoRefresh();
    }

    private void initView() {
        rvBaseRecycler = findViewById(R.id.rvBaseRecycler);
        rvBaseRecycler.setLayoutManager(new LinearLayoutManager(this));
        srlBaseHttpRecycler = findViewById(R.id.srlBaseHttpRecycler);
        List<Data> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Data date = new Data();
            date.project = "project" + i;
            date.result = "result" + i;
            date.date = "2019.09.0" + i;
            list.add(date);
        }
        QuickReportAdapter adapter = new QuickReportAdapter(R.layout.item_my_report, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.d("onItemClick: ");
                ToastUtils.showShort("onItemClick" + position);
            }
        });

        rvBaseRecycler.setAdapter(adapter);
    }

    @Override
    public void doBusiness() {

    }

    public class Data {
        public String project;
        public String result;
        public String date;
    }
}
