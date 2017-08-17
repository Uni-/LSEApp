package com.navercorp.android.lseapp.data;

import android.content.Context;

import com.navercorp.android.lseapp.data.cache.Cache;
import com.navercorp.android.lseapp.data.cache.CurrentArticleManager;
import com.navercorp.android.lseapp.data.localdb.LocalDataSource;
import com.navercorp.android.lseapp.data.localdb.LocalDb;
import com.navercorp.android.lseapp.model.Article;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentTitleValue;
import com.navercorp.android.lseapp.model.ScreenArticle;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by NAVER on 2017-07-26.
 */

public enum ConcreteRepositoryFactory {

    ;

    private ConcreteRepositoryFactory() {
        throw new UnsupportedOperationException();
    }

    public static Repository getInstance(Context context) {
        ConcreteRepository.INSTANCE.setContext(context);
        return ConcreteRepository.INSTANCE;
    }

    private enum ConcreteRepository implements Repository {

        INSTANCE;

        private final Cache mCache;
        private final LocalDb mLocalDb;

        private ConcreteRepository() {
            mCache = CurrentArticleManager.INSTANCE;
            mLocalDb = LocalDataSource.INSTANCE;
        }

        protected void setContext(Context context) {
            mLocalDb.resetHelper(context);
        }

        @Override
        public void setNewScreenArticle() {
            mCache.setCurrentArticle(new ScreenArticle());
        }

        @Override
        public DocumentComponentValue getComponentValueItemOfScreenArticle(int index) {
            return mCache.getComponentValueItemOfScreenArticle(index);
        }

        @Override
        public int getComponentValueItemCountOfScreenArticle() {
            return mCache.getComponentValueItemCountOfScreenArticle();
        }

        @Override
        public void addComponentValueItemToScreenArticle(int index, DocumentComponentValue value) {
            mCache.addComponentValueItemToScreenArticle(index, value);
        }

        @Override
        public void removeComponentValueItemFromScreenArticle(int index) {
            mCache.removeComponentValueItemFromScreenArticle(index);
        }

        @Override
        public void replaceComponentValueItemOfScreenArticle(int index, DocumentComponentValue value) {
            mCache.replaceComponentValueItemOfScreenArticle(index, value);
        }

        @Override
        public void moveComponentValueItemOfScreenArticle(int fromIndex, int toIndex) {
            mCache.moveComponentValueItemOfScreenArticle(fromIndex, toIndex);
        }

        @Override
        public boolean saveCurrentArticle() {
            return mLocalDb.saveArticle(mCache.getCurrentArticle());
        }

        @Override
        public Iterator<Map.Entry<String, String>> listArticlesIterator() {
            return mLocalDb.listArticlesIterator();
        }

        @Override
        public String getArticleTitle(String sha1sumKey) {
            Article article = mLocalDb.getArticle(sha1sumKey);
            switch (article.type()) {
                case SCREEN:
                    return ((DocumentTitleValue) ((ScreenArticle) article).getComponentsList().get(0)).getText();
                default:
                    throw new UnsupportedOperationException();
            }
        }

        @Override
        public void loadArticleAsCurrent(String sha1sumKey) {
            mCache.setCurrentArticle(mLocalDb.getArticle(sha1sumKey));
        }

        @Override
        public boolean deleteArticle(String sha1sumKey) {
            return mLocalDb.deleteArticle(sha1sumKey);
        }
    }
}
