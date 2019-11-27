package com.example.drugcontrol.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.drugcontrol.R;
import com.example.drugcontrol.ui.activity.RecycleListConActivity;

import java.util.List;

public class QuickConAdapter extends BaseQuickAdapter<RecycleListConActivity.Data, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener{
    boolean status=false;
    public QuickConAdapter(int layoutResId, @Nullable List data,boolean status) {
        super(layoutResId, data);
        this.status=status;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecycleListConActivity.Data item) {
        helper.setText(R.id.tvName, item.name);
        helper.setText(R.id.tvTag, item.tag);
        helper.setText(R.id.tvCompany, item.address);
        helper.setText(R.id.tvProfession, item.profession);
        helper.setText(R.id.tvPatient, item.patient);
        helper.setText(R.id.tvDate, item.date);
        if (status){
            helper.setVisible(R.id.ivStatus,true);
        }
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
