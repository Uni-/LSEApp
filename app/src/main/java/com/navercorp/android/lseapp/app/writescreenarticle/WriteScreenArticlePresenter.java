package com.navercorp.android.lseapp.app.writescreenarticle;

import com.navercorp.android.lseapp.data.ConcreteRepositoryFactory;
import com.navercorp.android.lseapp.data.Repository;
import com.navercorp.android.lseapp.model.ScreenArticle;

/**
 * Created by NAVER on 2017-07-20.
 */

public final class WriteScreenArticlePresenter implements WriteScreenArticleContract.Presenter {

    private WriteScreenArticleContract.View mView;
    private Repository mRepository;

    public WriteScreenArticlePresenter(WriteScreenArticleContract.View view) {
        mView = view;
        mRepository = ConcreteRepositoryFactory.getInstance();
    }

    @Override
    public void start() {
        final ScreenArticle article = new ScreenArticle();
        mRepository.setCurrentArticle(article);
    }

    @Override
    public void end() {
        mView = null;
    }

    @Override
    public ScreenArticle getArticle() {
        return (ScreenArticle) mRepository.getCurrentArticle();
    }
}
