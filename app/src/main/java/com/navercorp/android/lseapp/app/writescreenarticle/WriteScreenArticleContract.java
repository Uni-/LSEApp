package com.navercorp.android.lseapp.app.writescreenarticle;

import com.navercorp.android.lseapp.base.BasePresenter;
import com.navercorp.android.lseapp.base.BaseView;
import com.navercorp.android.lseapp.model.ScreenArticle;

/**
 * Created by NAVER on 2017-07-20.
 */

public interface WriteScreenArticleContract {

    interface View extends BaseView {

        void setArticle(ScreenArticle article);

        ScreenArticle getArticle();
    }

    interface Presenter extends BasePresenter {
    }
}
