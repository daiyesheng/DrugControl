package com.example.drugcontrol.presenter;


import com.example.drugcontrol.model.BaseModel;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这个基类的Presenter的主要作用是 将当前的Presenter持有的view在合适的时候(Activity onDestory())清除掉
 */
public abstract class BasePresenter<V, M extends BaseModel> {

    private WeakReference<V> mViewRef;
    //使用CompositeSubscription来收集Subscription
    private CompositeDisposable mDisposables;
    protected M mModel; //具体的model

    public BasePresenter() {
        mModel = initModel();   //不能用向下转型的做法
    }

    //由子类去覆盖该方法 返回子类需要的具体对象
    protected abstract M initModel();

    /**
     * 关联
     *
     * @param view
     */
    public void attach(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    /**
     * 解除关联
     */
    public void dettach() {
        if (mViewRef != null) {
            mViewRef.clear();
        }
    }

    protected V getView() {
        return mViewRef.get();
    }

    public void dispose(Disposable subscription) {
        if (mDisposables != null) {
            mDisposables.remove(subscription);
        }
    }

    //取消所有的订阅
    public void dispose() {
        if (mDisposables != null) {
            mDisposables.clear();
            // Using clear will clear all, but can accept new disposable  disposables.clear();
            // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable  disposables.dispose();
        }
    }

    //添加订阅
    public void addSubscription(Disposable subscription) {
        if (subscription == null) return;
        if (mDisposables == null) {
            mDisposables = new CompositeDisposable();
        }
        mDisposables.add(subscription);
    }

    /**
     * RxJava线程调度
     * 与Activity的生命周期绑定（在Destroy方法中取消订阅） RxLifecycle
     * * subscribeOn指定观察者代码运行的线程
     * observerOn()指定订阅者运行的线程* <p>* 提供给它一个Observable它会返回给你另一个Observable
     * 目前网上对RxJava的内存泄漏有几种方案：
     * 1、通过封装，手动为RxJava的每一次订阅进行控制，在指定的时机进行取消订阅；
     * 2、使用 Daniel Lew 的 RxLifecycle ，通过监听Activity、Fragment的生命周期，来自动断开subscription以防止内存泄漏。
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                Observable<T> mObservable = (Observable<T>) observable
//                        .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))//onDestory方法中取消请求
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                return mObservable;
            }
        };
    }

}
