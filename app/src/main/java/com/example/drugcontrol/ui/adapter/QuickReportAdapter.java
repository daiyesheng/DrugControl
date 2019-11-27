package com.example.drugcontrol.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.drugcontrol.R;
import com.example.drugcontrol.ui.activity.RecycleListReportActivity;

import java.util.List;

public class QuickReportAdapter extends BaseQuickAdapter<RecycleListReportActivity.Data, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener{

    public QuickReportAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecycleListReportActivity.Data item) {
        helper.setText(R.id.tvProject, item.project);
        helper.setText(R.id.tvResult, item.result);
        helper.setText(R.id.tvDate, item.date);
//        helper.addOnClickListener(R.id.btnDetail);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ToastUtils.showShort("childView click");
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        LogUtils.d("嵌套RecycleView item 收到: " + "点击了第 " + position + " 一次");
        ToastUtils.showShort("嵌套RecycleView item 收到: " + "点击了第 " + position + " 一次");
    }

}
