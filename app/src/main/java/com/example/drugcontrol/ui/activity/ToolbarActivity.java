package com.example.drugcontrol.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;

import com.example.drugcontrol.R;
import com.example.drugcontrol.base.BaseActivity;


public abstract class ToolbarActivity extends BaseActivity {

    public final static int NO_RES = R.id.NO_RES;// 返回键 不修改
    public final static int NO_ICON = R.id.NO_ICON;//返回键 不显示
    protected FrameLayout contentLayout;
    protected TextView tvTitle;
    protected Toolbar toolbar;
    protected ActionMenuView actionMenuView;    //Toolbar上的菜单

    @Override
    public void setContentView(int layoutResID) {
        if (contentLayout == null) {
            super.setContentView(layoutResID);
        } else {
            contentLayout.addView(View.inflate(this, layoutResID, null));
        }
//        View contentView = findViewById(R.id.fl_content);
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
//        params.topMargin = getResources().getDimensionPixelSize(R.dimen.mActionBarSize);
//        contentView.setLayoutParams(params);
    }

    public abstract int getLayoutID();

    protected void initTitle(String str) {
        try {
            tvTitle = (TextView) findViewById(R.id.tv_title);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (tvTitle != null) {
                tvTitle.setText(str);
            }
            if (toolbar != null) {
                toolbar.setTitle("");
                setSupportActionBar(toolbar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initBackClick(int resLeft, View.OnClickListener navigationOnClickListener) {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        if (resLeft == NO_ICON) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (resLeft != NO_RES) {
                toolbar.setNavigationIcon(resLeft);
            } else {
                toolbar.setNavigationIcon(R.drawable.back_btn);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                for (int i = 0; i < toolbar.getChildCount(); i++) {
                    View v = toolbar.getChildAt(i);
                    if (v instanceof ImageView || v instanceof ImageButton) {
                        v.setBackgroundResource(R.drawable.menu_bg_selector);
                    }
                }
            }
            toolbar.setNavigationOnClickListener(navigationOnClickListener);
        }
    }

    /**
     * @param resLeft  左侧菜单图标,NO_RES表示不显示  如只需显示文字，这里传 0
     * @param left     左侧菜单监听器
     * @param resRight 右侧菜单图标,NO_RES表示不显示 如只需显示文字，这里传 0
     * @param right    右侧菜单监听器
     * @Description 有文字菜单设置，对应布局R.layout.header_btn_text_layout
     */
    protected void initMenuClick(int resLeft, String textLeft, View.OnClickListener left, int resRight,
                                 String textRight, View.OnClickListener right) {
        actionMenuView = findViewById(R.id.action_menu_view);
        if (actionMenuView == null) {
            return;
        }
        actionMenuView.getMenu().clear();
        if (resLeft != NO_ICON) {
            if (resLeft == NO_RES) {
                resLeft = R.mipmap.ic_launcher;    //default icon
            }
            MenuItem menuItem = actionMenuView.getMenu().add(textLeft);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            View actionView = getLayoutInflater().inflate(R.layout.header_toolbar_menu, null, false);
            TextView tvMenu = actionView.findViewById(R.id.tvMenu);
            ImageView imgMenu = actionView.findViewById(R.id.imgMenu);
            if (resLeft != 0) { //优先显示图标
                imgMenu.setImageResource(resLeft);
                tvMenu.setVisibility(View.GONE);
                imgMenu.setVisibility(View.VISIBLE);
            } else {
                tvMenu.setText(textLeft);
                tvMenu.setVisibility(View.VISIBLE);
                imgMenu.setVisibility(View.GONE);
            }
            actionView.setOnClickListener(left);
            menuItem.setActionView(actionView);
        }
        if (resRight != NO_ICON) {
            if (resRight == NO_RES) {
                resRight = R.mipmap.ic_launcher;   //default icon
            }
            MenuItem menuItem = actionMenuView.getMenu().add(textRight);
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            View actionView = getLayoutInflater().inflate(R.layout.header_toolbar_menu, null, false);
            TextView tvMenu = actionView.findViewById(R.id.tvMenu);
            ImageView imgMenu = actionView.findViewById(R.id.imgMenu);
            if (resRight != 0) { //优先显示图标
                imgMenu.setImageResource(resRight);
                tvMenu.setVisibility(View.GONE);
                imgMenu.setVisibility(View.VISIBLE);
            } else {
                tvMenu.setText(textRight);
                tvMenu.setVisibility(View.VISIBLE);
                imgMenu.setVisibility(View.GONE);
            }
            actionView.setOnClickListener(right);
            menuItem.setActionView(actionView);
        }
    }

    /**
     * 更新菜单 只负责update, icon和text的显示逻辑交给最开始的 {@link #initMenuClick(int,
     * String, View.OnClickListener, int, String, View.OnClickListener)} ()}
     */
    protected void updateToolbarMenu(int index, int imgRes, String text) {
        if (actionMenuView == null) {
            return;
        }
        MenuItem item = actionMenuView.getMenu().getItem(index);
        if (item == null) {
            return;
        }
        View actionView = item.getActionView();
        if (imgRes != 0) {
            ImageView imgMenu = actionView.findViewById(R.id.imgMenu);
            imgMenu.setImageResource(imgRes);
        }
        if (!TextUtils.isEmpty(text)) {
            TextView tvMenu = actionView.findViewById(R.id.tvMenu);
            tvMenu.setText(text);
        }
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_toolbar;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        contentLayout = findViewById(R.id.fl_content);
        int layoutID = getLayoutID();
        if (layoutID != 0) {
            setContentView(layoutID);
        }
    }

}
