package com.navercorp.android.lseapp.app.selectsavedarticles;

import com.navercorp.android.lseapp.data.ConcreteRepositoryFactory;
import com.navercorp.android.lseapp.data.Repository;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by NAVER on 2017-07-20.
 */

public final class SelectSavedArticlePresenter implements SelectSavedArticleContract.Presenter {

    private SelectSavedArticleContract.View mView;
    private Repository mRepository;

    public SelectSavedArticlePresenter(SelectSavedArticleContract.View view) {
        mView = view;
        mRepository = ConcreteRepositoryFactory.getInstance(mView.getContext());
    }

    @Override
    public void start() {
        mView.initListView();
    }

    @Override
    public void end() {
        mView = null;
        mRepository = null;
    }

    @Override
    public Iterator<Map.Entry<String, String>> listArticlesIterator() {
        return mRepository.listArticlesIterator();
    }

    @Override
    public String getArticleTitle(String sha1sumKey) {
        return mRepository.getArticleTitle(sha1sumKey);
    }

    @Override
    public boolean deleteArticle(String sha1sumKey) {
        return mRepository.deleteArticle(sha1sumKey);
    }
}
