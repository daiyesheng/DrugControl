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
import com.example.drugcontrol.ui.adapter.QuickTreatmentAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class RecycleListTreatmentActivity extends ToolbarActivity {

    protected RecyclerView rvBaseRecycler;
    protected SmartRefreshLayout srlBaseHttpRecycler;

    public static void start(Context context) {
        Intent starter = new Intent(context, RecycleListTreatmentActivity.class);
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
        initTitle(getResources().getString(R.string.my_treatment));
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
            date.project = "心理疾病及疑似吸毒心理抑郁等相关疾" +
                    "病及咨询服务；心理疾病及疑似吸毒心" +
                    "理抑郁等相关疾病及咨询服务。" + i;
            date.des = "des" + i;
            date.suggesst = "suggesst心理疾病及疑似吸毒心理抑郁等相关疾" +
                    "病及咨询服务；心理疾病及疑似吸毒心" +
                    "理抑郁等相关疾病及咨询服务；千万不" +
                    "要有侥幸心理，认为自己身体素质好，" +
                    "排毒快，就认为自己肯定没事了。但是" +
                    "这不是你说了算的，是毒品尿检板说了" +
                    "算的。" + i;
            date.date = "2019.09.0" + i;
            list.add(date);
        }
        QuickTreatmentAdapter adapter = new QuickTreatmentAdapter(R.layout.item_my_treatment, list);
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
        public String des;
        public String suggesst;
        public String date;
    }
}
