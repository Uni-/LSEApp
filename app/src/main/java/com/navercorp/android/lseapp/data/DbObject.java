package com.navercorp.android.lseapp.data;

import com.navercorp.android.lseapp.util.Hash;

import java.util.Arrays;

/**
 * Created by NAVER on 2017-08-11.
 */

public final class DbObject {

    private final int mContentType;
    private final byte[] mContentValue;

    public DbObject(int contentType, byte[] contentValue) {
        mContentType = contentType;
        mContentValue = Arrays.copyOf(contentValue, contentValue.length);
    }

    private String getSha1sum() {
        return Hash.sha1sumString(mContentValue);
    }

    private int getContentType() {
        return mContentType;
    }

    private byte[] getContentValue() {
        return Arrays.copyOf(mContentValue, mContentValue.length);
    }
}
