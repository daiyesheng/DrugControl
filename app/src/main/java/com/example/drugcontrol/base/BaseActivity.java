package com.example.drugcontrol.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;

import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.drugcontrol.R;
import com.example.drugcontrol.presenter.BasePresenter;
import com.example.drugcontrol.utils.SharedFileUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class BaseActivity<V, P extends BasePresenter<V, ?>> extends AppCompatActivity implements IBaseView {

    protected BaseActivity mActivity;
    protected View mContentView;
    private View errorView;
    private View viewLoading;
    protected SharedFileUtils sharedFileUtils;

    /**
     * 定义presenter,持有Model,以及解绑持有的model
     * 在onCreate中初始化presenter
     * 在onResume中attach绑定
     * 在onDestory中dettach解绑
     */
    protected P presenter;
    private OptionsPickerView pickerView;   //PickView
    private Dialog dialog;//网络加载对话框
    private Animation animationFadeOut;
    private ProgressDialog mDialogUpdateProgress;
    private List<View> mEditTextViews;  //当前Activity的所有Edittext
    private NotificationCompat.Builder notification;
    private String mChannelId = "wxMakerUpdate";
    private LoadingPopupView loadingView;
    protected HashMap<String, String> valueMap = new HashMap<String, String>();

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onDebouncingClick(v);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        getSharedFileUtils();
        BarUtils.setStatusBarLightMode(this, true);
        initValueFromPrePage();
        if (ActivityUtils.isActivityExistsInStack(this)) { //调试一下 Utils是否需要init
            LogUtils.d("haha" + this.getClass().getName());
        }
        initData(getIntent().getExtras());
        setRootLayout(bindLayout());
        initView(savedInstanceState, mContentView);
        presenter = initPresenter();
        if (presenter != null) {
            presenter.attach((V) this);
        }
        doBusiness();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.dispose();
            presenter.dettach();
        }
        if (loadingView != null) {
            loadingView = null;
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void setRootLayout(@LayoutRes int layoutId) {
        if (layoutId <= 0) return;
        setContentView(mContentView = LayoutInflater.from(this).inflate(layoutId, null));
    }

    public SharedFileUtils getSharedFileUtils() {
        if (sharedFileUtils == null) {
            sharedFileUtils = SharedFileUtils.getInstance(BaseActivity.this);
        }
        return sharedFileUtils;
    }

    @Override
    public void onShowLoading() {
        if (loadingView == null) {
            loadingView = new XPopup.Builder(this).asLoading("加载中");
        }
        loadingView.show();
    }

    @Override
    public void onHideLoading() {//&& loadingView.isShow()
        if (loadingView != null ) {
            loadingView.dismiss();
        }
    }

    @Override
    public void showMessage(@NonNull @NotNull String message) {

    }

    @NotNull
    @Override
    public <X> AutoDisposeConverter<X> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }

    /**
     * @param v     列表布局（内容）
     * @param strId 提示语
     * @param imgId 图标
     */
    @Override
    public void showErrorPage(View v, int strId, int imgId, OnClickListener listener) {
        ViewGroup parent = (ViewGroup) v.getParent();
        int index = parent.indexOfChild(v); //view在parent中的position 便于后面创建FrameLayout后插入位置
        ViewGroup.LayoutParams paramsNormal = v.getLayoutParams();
        if (errorView == null) {
            errorView = getLayoutInflater().inflate(R.layout.network_error_layout, null);
        }
        if (strId != 0) {
            ((TextView) errorView.findViewById(R.id.tvError)).setText(getResources().getString(strId));
        }
        if (imgId != 0) {
            ((ImageView) errorView.findViewById(R.id.imgError)).setImageResource(imgId);
        }
        //检查是否拥有别的父View
        if (errorView.getParent() != null) {
            ((ViewGroup) errorView.getParent()).removeView(errorView);
        }
        if (parent instanceof FrameLayout) {
            parent.addView(errorView);
        } else {
            FrameLayout frameLayout = new FrameLayout(getBaseContext());
            parent.removeView(v);
            frameLayout.addView(v, paramsNormal);
            frameLayout.addView(errorView, paramsNormal);
            parent.addView(frameLayout, index, paramsNormal);  //在view的原位置插入
        }
        errorView.setOnClickListener(listener);
    }

    //加载成功后手动调这个方法去掉errorView
    @Override
    public void removeErrorPage() {
        if (errorView == null) {
            return;
        }
        if (errorView.getParent() == null) {
            return;
        }
        ((ViewGroup) errorView.getParent()).removeView(errorView);
    }


    /**
     * 将上个页面传递过来的参数值全部放到valueMap 中
     */
    public void initValueFromPrePage() {
        if (getIntent() == null) {
            return;
        }
        if (getIntent().getExtras() == null) {
            return;
        }
        Bundle extBundle = getIntent().getExtras();
        if (extBundle != null) {
            Iterator<String> it = extBundle.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (key == null) {
                    continue;
                }
                String value = extBundle.getString(key);
                valueMap.put(key, value);
            }
        }
    }

    @Override
    public void onDebouncingClick(@NonNull View view) {
    }

    public void applyDebouncingClickListener(View... views) {
        ClickUtils.applyGlobalDebouncing(views, mClickListener);
        ClickUtils.applyScale(views);
    }

}
