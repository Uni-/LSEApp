package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-31.
 */

public enum TextProperty {
    BOLD(Boolean.class),
    ITALIC(Boolean.class),
    UNDERLINE(Boolean.class),
    SIZE(Integer.class),
    COLOR(Integer.class),
    ;

    public final Class<?> valueType;

    private TextProperty(Class<?> v) {
        valueType = v;
    }
}