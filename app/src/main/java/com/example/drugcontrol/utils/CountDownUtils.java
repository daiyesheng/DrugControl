package com.example.drugcontrol.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;


/**
 * Created by daiyesheng
 * 倒计时 手动调begin方法才倒计时
 */
public class CountDownUtils extends CountDownTimer {

    private TextView textView;
    private long totalCount;

    /**
     * 参数依次为总时长,和计时的时间间隔
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownUtils(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        totalCount = millisInFuture;
    }

    /**
     * 手动调begin方法才倒计时
     */
    public void begin() {
        if (textView == null) {
            LogUtils.d("target view is null");
            return;
        }
        textView.setText(totalCount + "s");
        start();
    }

    @Override
    public void onTick(long millisUntilFinished) {  //计时过程显示
        textView.setEnabled(false);
        textView.setText((int) Math.ceil(millisUntilFinished / 1000f) + "s后重新获取");
    }

    @Override
    public void onFinish() { //计时完毕时触发
        textView.setText("重新获取");
        textView.setEnabled(true);
        cancel();
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

}
