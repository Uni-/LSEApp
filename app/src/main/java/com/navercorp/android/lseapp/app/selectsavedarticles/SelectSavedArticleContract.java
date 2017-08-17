package com.navercorp.android.lseapp.app.selectsavedarticles;

import android.content.Context;

import com.navercorp.android.lseapp.base.BasePresenter;
import com.navercorp.android.lseapp.base.BaseView;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by NAVER on 2017-07-20.
 */

public interface SelectSavedArticleContract {

    interface View extends BaseView {

        Context getContext();

        void initListView();
    }

    interface Presenter extends BasePresenter {

        Iterator<Map.Entry<String, String>> listArticlesIterator();

        String getArticleTitle(String sha1sumKey);

        boolean deleteArticle(String sha1sumKey);
    }
}
