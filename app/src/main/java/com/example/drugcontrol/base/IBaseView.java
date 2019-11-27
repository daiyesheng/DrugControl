package com.example.drugcontrol.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.uber.autodispose.AutoDisposeConverter;

public interface IBaseView {

    void initData(@Nullable Bundle bundle);

    int bindLayout();

    void setRootLayout(@LayoutRes int layoutId);

    void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView);

    void doBusiness();

    void onDebouncingClick(@NonNull View view);

    void onShowLoading();

    void onHideLoading();

    void showMessage(@NonNull String message);

    <P> P initPresenter();

    void showErrorPage(View view, int strId, int imgId, View.OnClickListener listener);

    void removeErrorPage();

    /**
     * 绑定生命周期
     */
    <T> AutoDisposeConverter<T> bindAutoDispose();
}
