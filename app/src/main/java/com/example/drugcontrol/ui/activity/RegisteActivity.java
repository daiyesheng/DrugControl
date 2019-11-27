package com.example.drugcontrol.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.example.drugcontrol.R;
import com.example.drugcontrol.base.BaseActivity;
import com.example.drugcontrol.utils.CountDownUtils;
import com.example.drugcontrol.widget.ClearEditText;

public class RegisteActivity extends BaseActivity {

    private ClearEditText etAccount;
    private ClearEditText edMsgCode;
    private TextView tvMsgCode;
    private CheckBox checkBoxProtocol;

    private CountDownUtils countDownUtils;

    public static void start(Context context) {
        Intent starter = new Intent(context, RegisteActivity.class);
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
                countDownUtils.setTextView(tvMsgCode);
            }
            //开始60s倒计时
            countDownUtils.begin();
            ToastUtils.showShort("验证码已发送");
        } else {
            tvMsgCode.performClick();
        }
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_registe;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        etAccount = findViewById(R.id.etAccount);
        edMsgCode = findViewById(R.id.edMsgCode);
        tvMsgCode = findViewById(R.id.tvMsgCode);
        checkBoxProtocol = findViewById(R.id.checkBoxProtocol);

        applyDebouncingClickListener(
                findViewById(R.id.tvCancle),
                findViewById(R.id.tvMsgCode),
                findViewById(R.id.tvProtocol),
                findViewById(R.id.btnRegister)
        );
    }


    @Override
    public void doBusiness() {

    }

    @Override
    public void onDebouncingClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.tvCancle:
                break;
            case R.id.tvMsgCode:
                if (TextUtils.isEmpty(etAccount.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.toast_account_not_empty));
                    etAccount.requestFocus();
                    return;
                }
//                presenter.getImgCode(etAccount.getText().toString().trim());
                getCodeStatus(true);
                break;
            case R.id.tvProtocol:
                break;
            case R.id.btnRegister:
                break;
            default:
                break;
        }
    }
}
