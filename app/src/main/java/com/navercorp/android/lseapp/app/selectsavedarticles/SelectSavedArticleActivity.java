package com.navercorp.android.lseapp.app.selectsavedarticles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.base.BasePresenter;

public final class SelectSavedArticleActivity
        extends AppCompatActivity
        implements
        SelectSavedArticleContract.View {

    SelectSavedArticleContract.Presenter mPresenter;

    @Override // AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_saved_articles);

        mPresenter = new SelectSavedArticlePresenter(SelectSavedArticleActivity.this);
        mPresenter.start();
    }

    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }
}
