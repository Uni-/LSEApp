package com.navercorp.android.lseapp.data;

import com.navercorp.android.lseapp.model.Article;

/**
 * Created by NAVER on 2017-07-26.
 */

public class ConcreteRepositoryFactory {

    private ConcreteRepositoryFactory() {
        throw new UnsupportedOperationException();
    }

    public static Repository getInstance() {
        return ConcreteRepository.INSTANCE;
    }

    private enum ConcreteRepository implements Repository {

        INSTANCE;

        private Article mCurrentArticle;

        private ConcreteRepository() {
            mCurrentArticle = null;
        }

        @Override
        public void setCurrentArticle(Article article) {
            mCurrentArticle = article;
        }

        @Override
        public Article getCurrentArticle() {
            return mCurrentArticle;
        }
    }
}
