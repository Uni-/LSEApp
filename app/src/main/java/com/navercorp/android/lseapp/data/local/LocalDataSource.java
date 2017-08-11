package com.navercorp.android.lseapp.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.navercorp.android.lseapp.data.DataSource;
import com.navercorp.android.lseapp.data.DbObject;

/**
 * Created by NAVER on 2017-07-26.
 */

public enum LocalDataSource implements DataSource {

    INSTANCE;

    private LocalDbHelper mLocalDbHelper;

    @Override
    public void resetHelper(Context context) {
        mLocalDbHelper.close();
        mLocalDbHelper = new LocalDbHelper(context);
    }

    @Override
    public void foo() {
        SQLiteDatabase db = mLocalDbHelper.getReadableDatabase();
        String table = "ScreenArticle";
        String[] projection = new String[] {"UtcTimestamp", "ObjectRef"};
        Cursor cursor = db.query(table, projection, null, null, null, null, null);
        int row = 0;
        cursor.moveToFirst();
        while (cursor.moveToPosition(row)) {
            int utcTimestamp = cursor.getInt(0);
            String objectRef = cursor.getString(1);
            // TODO
        }
    }

}
