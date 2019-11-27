package com.example.drugcontrol.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.example.drugcontrol.R;
import com.example.drugcontrol.base.BaseActivity;
import com.example.drugcontrol.utils.CountDownUtils;
import com.example.drugcontrol.widget.ClearEditText;

public class LoginActivity extends BaseActivity {

    private ClearEditText etAccount;
    private CountDownUtils countDownUtils;
    private TextView btnMsgCode;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    public Object initPresenter() {
        return null;
    }

    public void getCodeStatus(boolean isSuccess) {
        if (isSuccess) {
            if (countDownUtils == null) {
                countDownUtils = new CountDownUtils(60 * 1000, 200); //构造CountDownTimer对象(60s)
                countDownUtils.setTextView(btnMsgCode);
            }
            //开始60s倒计时
            countDownUtils.begin();
            ToastUtils.showShort("验证码已发送");
        } else {
            btnMsgCode.performClick();
        }
    }


    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        etAccount = findViewById(R.id.etAccount);
        btnMsgCode = findViewById(R.id.btnMsgCode);
//        findViewById(R.id.btnIgnore).setOnClickListener(this);
//        findViewById(R.id.btnMsgCode).setOnClickListener(this);
//        findViewById(R.id.btnLogin).setOnClickListener(this);
//        findViewById(R.id.btnRegiste).setOnClickListener(this);
//        findViewById(R.id.llLoginByWechat).setOnClickListener(this);
//        findViewById(R.id.llLoginByQQ).setOnClickListener(this);

        applyDebouncingClickListener(
                findViewById(R.id.btnIgnore),
                findViewById(R.id.btnMsgCode),
                findViewById(R.id.btnLogin),
                findViewById(R.id.btnRegiste),
                findViewById(R.id.llLoginByWechat),
                findViewById(R.id.llLoginByQQ)
        );
    }

    @Override
    public void onDebouncingClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btnIgnore:
                break;
            case R.id.btnMsgCode:
                if (TextUtils.isEmpty(etAccount.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.toast_account_not_empty));
                    etAccount.requestFocus();
                    return;
                }
//                presenter.getImgCode(etAccount.getText().toString().trim());
                getCodeStatus(true);
                break;
            case R.id.btnLogin:
                break;
            case R.id.btnRegiste:
                RegisteActivity.start(this);
                break;
            case R.id.llLoginByWechat:
                break;
            case R.id.llLoginByQQ:
                break;
            default:
                break;
        }
    }

    @Override
    public void doBusiness() {

    }
}
