package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public enum ArticleType implements ObjectType<ArticleType> {
    SCREEN,
    @Deprecated
    UNUSED;

    private ArticleType() {
    }

    @Override
    public int getGlobalTypeSerial() {
        return 100 + ordinal();
    }
}
