package com.navercorp.android.lseapp.widget;

import android.view.View;

/**
 * Created by NAVER on 2017-07-21.
 */

public interface DocumentComponentView<ViewType extends View & DocumentComponentView<ViewType>> {

    ViewType getView();

    void setData(byte[] bytes);
}
