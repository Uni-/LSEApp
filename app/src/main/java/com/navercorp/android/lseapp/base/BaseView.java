package com.navercorp.android.lseapp.base;

/**
 * Created by NAVER on 2017-07-20.
 */

public interface BaseView<P extends BasePresenter> {

    P getPresenter();
}
