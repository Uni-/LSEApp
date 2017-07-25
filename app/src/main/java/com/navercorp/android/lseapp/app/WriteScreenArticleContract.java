package com.navercorp.android.lseapp.app;

import com.navercorp.android.lseapp.base.BasePresenter;
import com.navercorp.android.lseapp.base.BaseView;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;

import java.util.ArrayList;

/**
 * Created by NAVER on 2017-07-20.
 */

public interface WriteScreenArticleContract {

    interface View extends BaseView {

        void notifyValuesChanged(int index);
    }

    interface Presenter extends BasePresenter {

        ArrayList<DocumentComponentValue> getComponentValuesList();

        boolean createIfNotExistsFirstText();

        void notifyGotFocus(int index);

        void notifyLostFocus(int index);

        void insertElementValue(int index, DocumentComponentType type);
    }
}
