package com.navercorp.android.lseapp.data;

import com.navercorp.android.lseapp.data.local.LocalDataSource;
import com.navercorp.android.lseapp.model.Article;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.ScreenArticle;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by NAVER on 2017-07-26.
 */

public enum ConcreteRepositoryFactory {

    ;

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
        public void setNewScreenArticle() {
            mCurrentArticle = new ScreenArticle();
        }

        @Override
        public DocumentComponentValue getComponentValueItemOfScreenArticle(int index) {
            ScreenArticle screenArticle = (ScreenArticle) mCurrentArticle;
            List<DocumentComponentValue> list = screenArticle.getComponentsList();
            return list.get(index);
        }

        @Override
        public int getComponentValueItemCountOfScreenArticle() {
            ScreenArticle screenArticle = (ScreenArticle) mCurrentArticle;
            List<DocumentComponentValue> list = screenArticle.getComponentsList();
            return list.size();
        }

        @Override
        public void addComponentValueItemToScreenArticle(int index, DocumentComponentValue value) {
            ScreenArticle screenArticle = (ScreenArticle) mCurrentArticle;
            List<DocumentComponentValue> list = screenArticle.getComponentsList();
            list.add(index, value);
        }

        @Override
        public void removeComponentValueItemFromScreenArticle(int index) {
            ScreenArticle screenArticle = (ScreenArticle) mCurrentArticle;
            List<DocumentComponentValue> list = screenArticle.getComponentsList();
            list.remove(index);
        }

        @Override
        public void replaceComponentValueItemOfScreenArticle(int index, DocumentComponentValue value) {
            ScreenArticle screenArticle = (ScreenArticle) mCurrentArticle;
            List<DocumentComponentValue> list = screenArticle.getComponentsList();
            list.set(index, value);
        }

        @Override
        public void moveComponentValueItemOfScreenArticle(int fromIndex, int toIndex) {
            ScreenArticle screenArticle = (ScreenArticle) mCurrentArticle;
            List<DocumentComponentValue> list = screenArticle.getComponentsList();
            DocumentComponentValue value = list.get(fromIndex);
            list.remove(fromIndex);
            list.add(toIndex, value);
        }

        @Override
        public void saveCurrentArticle() {
            checkoutLocalSource();
            // TODO
            checkinLocalSource();
        }

        @Override
        public Iterator<Map.Entry<Integer, String>> listArticlesIterator() {
            checkoutLocalSource();
            return null; // TODO
        }

        @Override
        public String getArticleTitle(String sha1sumKey) {
            checkoutLocalSource();
            return null; // TODO
        }

        @Override
        public void loadArticleAsCurrent(String sha1sumKey) {
            checkoutLocalSource();
            // TODO
        }

        private void checkinLocalSource() {
            DataSource localDataSource = LocalDataSource.INSTANCE;
        }

        private void checkoutLocalSource() {
            DataSource localDataSource = LocalDataSource.INSTANCE;
        }
    }
}
