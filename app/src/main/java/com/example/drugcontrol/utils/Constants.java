package com.example.drugcontrol.utils;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;

    public static final String STR_ONE = "ONE";
    public static final String STR_TWO = "TWO";
    public static final String STR_THREE = "THREE";
    public static final String STR_FOUR = "FOUR";

    public static final String STR_ALL = "ALL";
    public static final String STR_COLLECTION = "COLLECTION";
    public static final String STR_ORDER = "ORDER";
    public static final String STR_FINISHED = "FINISHED";

    @IntDef({ONE, TWO, THREE, FOUR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NumbersInt {

    }

    @StringDef({STR_ONE, STR_TWO, STR_THREE, STR_FOUR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NumbersString {

    }

    @StringDef({STR_ALL, STR_COLLECTION, STR_ORDER,STR_FINISHED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExperterTab {

    }

}
