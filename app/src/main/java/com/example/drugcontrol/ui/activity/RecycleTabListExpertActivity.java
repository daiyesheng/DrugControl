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
import com.example.drugcontrol.utils.Constants;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class RecycleTabListExpertActivity extends ToolbarActivity {


    private TabLayout tabLayout;
    protected RecyclerView rvBaseRecycler;
    protected SmartRefreshLayout srlBaseHttpRecycler;
    private String currentTab;

    public static void start(Context context) {
        Intent starter = new Intent(context, RecycleTabListExpertActivity.class);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_expert_tab_list;
    }


    @Override
    public Object initPresenter() {
        return null;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        super.initView(savedInstanceState, contentView);
        initTitle(getResources().getString(R.string.my_experter));
        initBackClick(NO_RES, view -> finish());
        tabLayout = findViewById(R.id.tabLayout);
        initTabView();
        rvBaseRecycler = findViewById(R.id.rvBaseRecycler);
        rvBaseRecycler.setLayoutManager(new LinearLayoutManager(this));
        srlBaseHttpRecycler = findViewById(R.id.srlBaseHttpRecycler);
        List<RecycleListConActivity.Data> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            RecycleListConActivity.Data date = new RecycleListConActivity.Data();
            date.name = "name" + i;
            date.tag = "tag" + i;
            date.address = "address" + i;
            date.profession = "profession" + i;
            date.patient = "patient" + i;
            date.date = "2019.09.0" + i;
            list.add(date);
        }
        QuickConAdapter adapter = new QuickConAdapter(R.layout.item_my_consultation, list,true);
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

    private void initTabView() {
        String[] titles = getResources().getStringArray(R.array.my_expert_tab_status);
        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setTag(titles[i]).setText(titles[i]), i);
        }
        tabLayout.setTabTextColors(getResources().getColor(R.color.item_tv_color_02),getResources().getColor(R.color.item_tv_color_01));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.item_tv_color_01));
        tabLayout.setTabIndicatorFullWidth(false);
        tabLayout.getTabAt(2).select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tag = (String) tab.getTag();
                if (tag.equals("所有")) {
                    currentTab = Constants.STR_ALL;
                } else if (tag.equals("收藏")) {
                    currentTab = Constants.STR_COLLECTION;
                } else if (tag.equals("已预约")) {
                    currentTab = Constants.STR_ORDER;
                } else if (tag.equals("已就诊")) {
                    currentTab = Constants.STR_FINISHED;
                }
                rvBaseRecycler.smoothScrollToPosition(0);
                srlBaseHttpRecycler.autoRefresh();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                rvBaseRecycler.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void doBusiness() {

    }
}
