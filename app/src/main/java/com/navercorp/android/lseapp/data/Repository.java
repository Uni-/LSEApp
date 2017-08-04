package com.navercorp.android.lseapp.data;

import com.navercorp.android.lseapp.model.Article;

/**
 * Created by NAVER on 2017-08-04.
 */

public interface Repository {

    void setCurrentArticle(Article article);

    Article getCurrentArticle();
}
