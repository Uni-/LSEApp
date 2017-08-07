package com.navercorp.android.lseapp.app.writescreenarticle;

import com.navercorp.android.lseapp.base.BasePresenter;
import com.navercorp.android.lseapp.base.BaseView;
import com.navercorp.android.lseapp.model.DocumentComponentValue;

/**
 * Created by NAVER on 2017-07-20.
 */

public interface WriteScreenArticleContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter<View> {
        DocumentComponentValue getComponentValueItem(int index);
        int getComponentValueItemCount();
        void addComponentValueItem(int index, DocumentComponentValue value);
        void removeComponentValueItem(int index);
        void replaceComponentValueItem(int index, DocumentComponentValue value);
        void moveComponentValueItem(int fromIndex, int toIndex);
    }
}
