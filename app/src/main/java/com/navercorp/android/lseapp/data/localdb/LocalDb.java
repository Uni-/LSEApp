package com.navercorp.android.lseapp.data.localdb;

import android.content.Context;

import com.navercorp.android.lseapp.model.Article;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by NAVER on 2017-08-04.
 */

public interface LocalDb {

    void resetHelper(Context context);

    boolean saveArticle(Article article);

    Iterator<Map.Entry<String, String>> listArticlesIterator();

    Article getArticle(String sha1sumKey);

    boolean deleteArticle(String sha1sumKey);
}
