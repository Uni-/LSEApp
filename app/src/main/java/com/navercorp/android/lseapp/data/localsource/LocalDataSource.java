package com.navercorp.android.lseapp.data.localsource;

import android.content.Context;

/**
 * Created by NAVER on 2017-07-26.
 */

public enum LocalDataSource {
    INSTANCE;

    private LocalDbHelper mLocalDbHelper;

    public void resetDbHelper(Context context) {
        mLocalDbHelper.close();
        mLocalDbHelper = new LocalDbHelper(context);
    }
}
