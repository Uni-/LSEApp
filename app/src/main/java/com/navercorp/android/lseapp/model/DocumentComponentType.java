package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public enum DocumentComponentType implements ObjectType<DocumentComponentType> {
    TITLE,
    TEXT,
    IMAGE,
    MAP,
    @Deprecated
    UNUSED;

    @Override
    public int getGlobalTypeSerial() {
        return 200 + ordinal();
    }
}
