package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-31.
 */

public enum TextSpanEntry {
    BOLD(Boolean.class),
    ITALIC(Boolean.class),
    UNDERLINE(Boolean.class),
    COLOR(Integer.class),
    ;

    public final Class<?> var;

    private TextSpanEntry(Class<?> v) {
        var = v;
    }
}