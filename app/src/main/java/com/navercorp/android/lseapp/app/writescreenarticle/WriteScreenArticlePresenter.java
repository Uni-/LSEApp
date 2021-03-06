package com.navercorp.android.lseapp.app.writescreenarticle;

import com.navercorp.android.lseapp.data.ConcreteRepositoryFactory;
import com.navercorp.android.lseapp.data.Repository;
import com.navercorp.android.lseapp.model.DocumentComponentValue;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by NAVER on 2017-07-20.
 */

public final class WriteScreenArticlePresenter implements WriteScreenArticleContract.Presenter {

    private WriteScreenArticleContract.View mView;
    private Repository mRepository;

    public WriteScreenArticlePresenter(WriteScreenArticleContract.View view) {
        mView = view;
        mRepository = ConcreteRepositoryFactory.getInstance(mView.getContext());
    }

    @Override // WriteScreenArticleContract.Presenter
    public void start() {
        mRepository.setNewScreenArticle();
    }

    @Override // WriteScreenArticleContract.Presenter
    public void end() {
        mView = null;
        mRepository = null;
    }

    @Override
    public DocumentComponentValue getComponentValueItem(int index) {
        return mRepository.getComponentValueItemOfScreenArticle(index);
    }

    @Override
    public int getComponentValueItemCount() {
        return mRepository.getComponentValueItemCountOfScreenArticle();
    }

    @Override
    public void addComponentValueItem(int index, DocumentComponentValue value) {
        mRepository.addComponentValueItemToScreenArticle(index, value);
    }

    @Override
    public void removeComponentValueItem(int index) {
        mRepository.removeComponentValueItemFromScreenArticle(index);
    }

    @Override
    public void replaceComponentValueItem(int index, DocumentComponentValue value) {
        mRepository.replaceComponentValueItemOfScreenArticle(index, value);
    }

    @Override
    public void moveComponentValueItem(int fromIndex, int toIndex) {
        mRepository.moveComponentValueItemOfScreenArticle(fromIndex, toIndex);
    }

    @Override
    public void saveCurrentArticleAndShowMessage() {
        boolean success = mRepository.saveCurrentArticle();
        if (success) {
            mView.showArticleSaveSuccessMessage();
        } else {
            mView.showArticleSaveFailureMessage();
        }
    }

    @Override
    public void loadArticleAsCurrent(String sha1sumKey) {
        mRepository.loadArticleAsCurrent(sha1sumKey);
        mView.notifyValuesChanged();
    }
}
