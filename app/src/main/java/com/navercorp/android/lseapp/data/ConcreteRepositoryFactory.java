package com.navercorp.android.lseapp.data;

import com.navercorp.android.lseapp.model.Article;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.ScreenArticle;

import java.util.List;

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
    }
}
