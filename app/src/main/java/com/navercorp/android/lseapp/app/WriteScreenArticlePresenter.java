package com.navercorp.android.lseapp.app;

import com.navercorp.android.lseapp.data.ScreenArticleRepository;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentTextValue;
import com.navercorp.android.lseapp.model.DocumentTitleValue;

import java.util.ArrayList;

/**
 * Created by NAVER on 2017-07-20.
 */

public class WriteScreenArticlePresenter implements WriteScreenArticleContract.Presenter {

    private WriteScreenArticleContract.View mView;
    private ScreenArticleRepository mModel;
    private ArrayList<DocumentComponentValue> mElementsList;

    public WriteScreenArticlePresenter(WriteScreenArticleContract.View view) {
        mView = view;
        mModel = new ScreenArticleRepository();
        mElementsList = mModel.getArticle().getDocument().getElementsList();
    }

    @Override
    public void start() {
        insertTitle(0);
    }

    @Override
    public void end() {
        mView = null;
    }

    @Override
    public ArrayList<DocumentComponentValue> getComponentValuesList() {
        return mElementsList;
    }

    @Override
    public boolean createIfNotExistsFirstText() {
        if (mElementsList.size() > 1) {
            if (mElementsList.get(1).componentType() != DocumentComponentType.TEXT) {
                insertText(1);
                return true;
            } else {
                return false;
            }
        } else {
            insertText(1); // append
            return true;
        }
    }

    @Override
    public void notifyGotFocus(int index) {
        // do nothing
    }

    @Override
    public void notifyLostFocus(int index) {
        final DocumentComponentValue value = mElementsList.get(index);
        if (value.componentType() == DocumentComponentType.TEXT && ((DocumentTextValue) value).isEmpty()) {
            mElementsList.remove(index);
            mView.notifyValuesChanged(-index);
        }
    }

    @Override
    public void insertElementValue(int index, DocumentComponentType type) {
        switch (type) {
            case TITLE: {
                insertTitle(index);
                break;
            }
            case TEXT: {
                insertText(index);
                break;
            }
            default:
        }
    }

    private void insertTitle(int index) {
        DocumentComponentValue value = new DocumentTitleValue();
        mElementsList.add(index, value);
        mView.notifyValuesChanged(index);
    }

    private void insertText(int index) {
        DocumentComponentValue value = new DocumentTextValue();
        mElementsList.add(index, value);
        mView.notifyValuesChanged(index);
    }
}
