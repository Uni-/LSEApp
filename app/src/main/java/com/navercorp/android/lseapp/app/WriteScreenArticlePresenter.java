package com.navercorp.android.lseapp.app;

import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentTextValue;

import java.util.ArrayList;

/**
 * Created by NAVER on 2017-07-20.
 */

public class WriteScreenArticlePresenter implements WriteScreenArticleContract.Presenter {

    private WriteScreenArticleContract.View mView;

    private ArrayList<DocumentComponentValue> mValuesList;

    public WriteScreenArticlePresenter(WriteScreenArticleContract.View view) {
        mView = view;
        mValuesList = new ArrayList<>();
    }

    @Override
    public void start() {
        DocumentTextValue textValue = new DocumentTextValue();
        textValue.setDataFromBytes(new byte[]{});
        mValuesList.add(textValue);
    }

    @Override
    public void end() {
        mView = null;
    }

    @Override
    public ArrayList<DocumentComponentValue> getComponentValuesList() {
        return mValuesList;
    }
}
