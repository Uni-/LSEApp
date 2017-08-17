package com.navercorp.android.lseapp.model;

import com.navercorp.android.lseapp.util.Hash;

import java.util.Arrays;

/**
 * Created by NAVER on 2017-08-11.
 */

public final class ObjectValue {

    private final int mContentType;
    private final byte[] mContentValue;

    public ObjectValue(int contentType, byte[] contentValue) {
        mContentType = contentType;
        mContentValue = Arrays.copyOf(contentValue, contentValue.length);
    }

    public String getSha1sum() {
        return Hash.sha1sumString(mContentValue);
    }

    public int getContentType() {
        return mContentType;
    }

    public byte[] getContentValue() {
        return Arrays.copyOf(mContentValue, mContentValue.length);
    }
}
