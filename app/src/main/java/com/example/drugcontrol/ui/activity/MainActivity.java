package com.example.drugcontrol.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.example.drugcontrol.R;
import com.example.drugcontrol.base.BaseActivity;
import com.example.drugcontrol.ui.fragment.HomeConFragment;
import com.example.drugcontrol.ui.fragment.HomeMeFragment;
import com.example.drugcontrol.ui.fragment.HomeResFragment;

public class MainActivity extends BaseActivity {

    private RadioButton mRbRes, rbHome, mRbMe;
    private ImageView ivAdd;
    private RadioGroup mRbGroup;
    private long clickTime;
    private Fragment[] fragments;
    private Fragment currentFragment;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    public Object initPresenter() {
        return null;
    }


    public void switchContent(Fragment target) {
        if (currentFragment == target) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.hide((Fragment) currentFragment);
        }
        if (!target.isAdded()) {    // 先判断是否被add过
            transaction.add(R.id.fl_content, target).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.show(target).commit(); // 隐藏当前的fragment，显示下一个
        }
        currentFragment = (Fragment) target;
    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            ToastUtils.showShort(getString(R.string.tip_exit));
            clickTime = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mRbGroup = findViewById(R.id.radioGroup);
        rbHome = findViewById(R.id.rb_home);
        mRbRes = findViewById(R.id.rb_res);
        mRbMe = findViewById(R.id.rb_me);
        ivAdd = findViewById(R.id.rbAdd);

        applyDebouncingClickListener(
                ivAdd
        );

        Drawable dbRes = getResources().getDrawable(R.drawable.home_res_selector);
        dbRes.setBounds(0, 0, 60, 60);
        mRbRes.setCompoundDrawables(null, dbRes, null, null);
        ShapeDrawable shape = new ShapeDrawable();
        shape.setAlpha(0);
        shape.setBounds(0, 0, 60, 60);
        rbHome.setCompoundDrawables(null, shape, null, null);
        Drawable dbMe = getResources().getDrawable(R.drawable.home_me_seletor);
        dbMe.setBounds(0, 0, 60, 60);
        mRbMe.setCompoundDrawables(null, dbMe, null, null);
        fragments = new Fragment[3];
        fragments[0] = HomeResFragment.getInstance();
        fragments[1] = HomeConFragment.getInstance();
        fragments[2] = HomeMeFragment.getInstance();
        ivAdd.performClick();
        mRbGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_res:
                    switchContent(fragments[0]);
                    break;
                case R.id.rb_home:
                    switchContent(fragments[1]);
                    break;
                case R.id.rb_me:
                    switchContent(fragments[2]);
                    break;
                default:
                    break;
            }
        });


    }

    @Override
    public void onDebouncingClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.rbAdd:
                rbHome.performClick();
                switchContent(fragments[1]);
//                PubActivity.show(LikeXianYuActivity.this);
                break;
            default:
                break;
        }
    }

    @Override
    public void doBusiness() {

    }
}
