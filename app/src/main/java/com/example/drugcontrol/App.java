package com.example.drugcontrol;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.lxj.xpopup.XPopup;

public class App extends BaseApplication {

    private static volatile App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initThird();
    }

    private void initThird() {
        XPopup.setPrimaryColor(getResources().getColor(R.color.colorPrimary));

    }


}
