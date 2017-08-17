package com.navercorp.android.lseapp.data.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.navercorp.android.lseapp.model.Article;
import com.navercorp.android.lseapp.model.ArticleType;
import com.navercorp.android.lseapp.model.ObjectValue;
import com.navercorp.android.lseapp.model.ScreenArticle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 * Created by NAVER on 2017-07-26.
 */

public enum LocalDataSource implements LocalDb {

    INSTANCE;

    private LocalDbHelper mLocalDbHelper;

    @Override
    public void resetHelper(Context context) {
        if (mLocalDbHelper != null) {
            mLocalDbHelper.close();
        }
        mLocalDbHelper = new LocalDbHelper(context);
    }

    @Override
    public boolean saveArticle(Article article) {
        switch (article.type()) {
            case SCREEN: {
                boolean success = true;
                ScreenArticle screenArticle = (ScreenArticle) article;

                SQLiteDatabase db = mLocalDbHelper.getWritableDatabase();

                ObjectValue[] objectValues = ScreenArticle.toObjectValues(screenArticle);

                do {
                    String table = "Object";

                    for (ObjectValue objectValue : objectValues) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Sha1sum", objectValue.getSha1sum());
                        contentValues.put("ContentType", objectValue.getContentType());
                        contentValues.put("ContentValue", objectValue.getContentValue());
                        success &= db.insert(table, null, contentValues) != -1L;
                    }
                } while (false);

                do {
                    String table = "Article";

                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    long utcTimestamp = cal.getTimeInMillis();
                    String objectRef = objectValues[0].getSha1sum();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("UtcTimestamp", utcTimestamp);
                    contentValues.put("ObjectRef", objectRef);
                    success &= db.insert(table, null, contentValues) != -1L;
                } while (false);

                db.close();

                return success;
            }
        }
        return false;
    }

    @Override
    public Iterator<Map.Entry<String, String>> listArticlesIterator() {
        Map<String, String> articles = new TreeMap<>();

        SQLiteDatabase db = mLocalDbHelper.getReadableDatabase();
        String table = "Article";
        String[] projection = new String[]{"UtcTimestamp", "ObjectRef"};
        Cursor cursor = db.query(table, projection, null, null, null, null, null);
        int row = 0;
        cursor.moveToFirst();
        Log.v("listArticlesIterator", String.valueOf(cursor.getCount()));
        while (cursor.moveToPosition(row++)) {
            int utcTimestamp = cursor.getInt(0);
            String objectRef = cursor.getString(1);

            Log.v("listArticlesIterator", String.format("UtcTimestamp=%d,ObjectRef=%s", utcTimestamp, objectRef));

            Date date = new Date(utcTimestamp);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateStr = dateFormat.format(date);

            articles.put(dateStr, objectRef);
        }
        cursor.close();
        db.close();

        return articles.entrySet().iterator();
    }

    @Override
    public Article getArticle(String sha1sumKey) {
        Article article;

        SQLiteDatabase db = mLocalDbHelper.getReadableDatabase();

        do {
            String table = "Article";
            String[] projection = new String[]{"UtcTimestamp", "ObjectRef"};
            String selection = "ObjectRef = ?";
            String[] selectionArgs = {sha1sumKey};
            Cursor cursor = db.query(table, projection, selection, selectionArgs, null, null, null);
            if (!cursor.moveToFirst()) {
                return null;
            }
        } while (false);

        do {
            Map<String, ObjectValue> objectValues = new TreeMap<>();
            String table = "Object";
            String[] projection = new String[]{"Sha1sum", "ContentType", "ContentValue"};
            Cursor cursor = db.query(table, projection, null, null, null, null, null);
            int row = 0;
            cursor.moveToFirst();
            while (cursor.moveToPosition(row++)) {
                String sha1sum = cursor.getString(0);
                int contentType = cursor.getInt(1);
                byte[] contentValue = cursor.getBlob(2);

                objectValues.put(sha1sum, new ObjectValue(contentType, contentValue));
            }
            if (objectValues.get(sha1sumKey).getContentType() == ArticleType.SCREEN.getGlobalTypeSerial()) {
                article = ScreenArticle.fromObjectValues(sha1sumKey, objectValues);
            } else {
                throw new UnsupportedOperationException();
            }
        } while (false);

        db.close();

        return article;
    }

    @Override
    public boolean deleteArticle(String sha1sumKey) {
        boolean success = true;

        SQLiteDatabase db = mLocalDbHelper.getWritableDatabase();

        do {
            String table = "Article";
            String whereClause = "ObjectRef = ?";
            String[] whereArgs = {sha1sumKey};
            success &= db.delete(table, whereClause, whereArgs) != 0;
        } while (false);

        db.close();

        return success;
    }
}
