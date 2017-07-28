package com.navercorp.android.lseapp.app.writescreenarticle;

import com.navercorp.android.lseapp.data.Repository;
import com.navercorp.android.lseapp.model.ScreenArticle;

/**
 * Created by NAVER on 2017-07-20.
 */

public class WriteScreenArticlePresenter implements WriteScreenArticleContract.Presenter {

    private WriteScreenArticleContract.View mView;
    private Repository mRepository;

    public WriteScreenArticlePresenter(WriteScreenArticleContract.View view) {
        mView = view;
        mRepository = new Repository();
    }

    @Override
    public void start() {
        final ScreenArticle article = new ScreenArticle();
        mView.setArticle(article);
    }

    @Override
    public void end() {
        mView = null;
    }

}
