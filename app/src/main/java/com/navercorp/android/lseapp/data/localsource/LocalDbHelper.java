package com.navercorp.android.lseapp.data.localsource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by NAVER on 2017-07-26.
 */

public class LocalDbHelper extends SQLiteOpenHelper {

    public static final String LOCAL_SAVED_DATABASE_NAME = "lse_saved.db";
    public static final int LOCAL_SAVED_DATABASE_VERSION = 1;

    public LocalDbHelper(Context context) {
        super(context, LOCAL_SAVED_DATABASE_NAME, null, LOCAL_SAVED_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String sqlDropTableArticle = "DROP TABLE IF EXISTS ScreenArticle;";
        final String sqlDropTableObject = "DROP TABLE IF EXISTS Object;";
        final String sqlCreateTableArticle = "CREATE TABLE IF NOT EXISTS ScreenArticle (UtcTimestamp INTEGER NOT NULL, ObjectRef TEXT NOT NULL);";
        final String sqlCreateTableObject = "CREATE TABLE IF NOT EXISTS Object (Sha1sum TEXT NOT NULL, ContentType INTEGER NOT NULL, ContentValue BLOB NOT NULL);";

        db.execSQL(sqlDropTableArticle);
        db.execSQL(sqlDropTableObject);
        db.execSQL(sqlCreateTableArticle);
        db.execSQL(sqlCreateTableObject);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }
}
