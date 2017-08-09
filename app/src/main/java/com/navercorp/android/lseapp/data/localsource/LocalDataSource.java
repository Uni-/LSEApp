package com.navercorp.android.lseapp.data.localsource;

import android.content.Context;

import com.navercorp.android.lseapp.data.DataSource;

/**
 * Created by NAVER on 2017-07-26.
 */

public enum LocalDataSource implements DataSource {

    INSTANCE;

    private LocalDbHelper mLocalDbHelper;

    public void resetDbHelper(Context context) {
        mLocalDbHelper.close();
        mLocalDbHelper = new LocalDbHelper(context);
    }
}
