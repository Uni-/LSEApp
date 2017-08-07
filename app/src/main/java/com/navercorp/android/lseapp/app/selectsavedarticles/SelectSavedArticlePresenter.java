package com.navercorp.android.lseapp.app.selectsavedarticles;

import com.navercorp.android.lseapp.data.ConcreteRepositoryFactory;
import com.navercorp.android.lseapp.data.Repository;

/**
 * Created by NAVER on 2017-07-20.
 */

public final class SelectSavedArticlePresenter implements SelectSavedArticleContract.Presenter {

    private SelectSavedArticleContract.View mView;
    private Repository mRepository;

    public SelectSavedArticlePresenter(SelectSavedArticleContract.View view) {
        mView = view;
        mRepository = ConcreteRepositoryFactory.getInstance();
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {
        mView = null;
        mRepository = null;
    }
}
