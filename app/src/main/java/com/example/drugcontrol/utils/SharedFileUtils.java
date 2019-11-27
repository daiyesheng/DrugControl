package com.example.drugcontrol.utils;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;
import com.example.drugcontrol.App;

import net.grandcentrix.tray.AppPreferences;

public class SharedFileUtils {

    public static final String IS_LOGIN = "isLogin";
    public static final String ID = "studentId";    //学生id

    private AppPreferences mPreference;

    private static volatile SharedFileUtils instance;

    private SharedFileUtils(Context context) {
        mPreference = new AppPreferences(context);
    }

    public static SharedFileUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (SharedFileUtils.class) {
                if (instance == null) {
                    instance = new SharedFileUtils(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void putString(String key, String value) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        mPreference.put(key, value);
    }

    public String getString(String key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        return mPreference.getString(key, "");
    }

    public void putInt(String key, int value) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        mPreference.put(key, value);
    }

    public int getInt(String key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        return mPreference.getInt(key, 0);
    }

    public void putBoolean(String key, boolean value) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        mPreference.put(key, value);
    }

    public boolean getBoolean(String key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        return mPreference.getBoolean(key, false);
    }

    public void remove(String key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        mPreference.remove(key);
    }

    //clear all data
    public void clear() {
        mPreference.clear();
    }

    public boolean isContainsKey(String key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        return mPreference.contains(key);
    }

    public void putLong(String key, long value) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        mPreference.put(key, value);
    }

    public long getLong(String key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key can't be null or empty string");
        }
        return mPreference.getLong(key, 0);
    }

    /**
     * 获取存储本地的学生id
     */
    public String getId() {
        return getString(SharedFileUtils.ID);
    }

    //获取是否登录标志位
    public boolean isLogin() {
        return getBoolean(SharedFileUtils.IS_LOGIN);
    }

    /**
     * 学生端退出登录统一调这个方法
     */
    public void removeAllLoginInfo() {
        remove(SharedFileUtils.ID);
        CookieUtil.remove(false);
        DataCleanManager.clearWebViewCache(App.getInstance());
    }

}
