package com.navercorp.android.lseapp.app.selectsavedarticles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.base.BasePresenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class SelectSavedArticleActivity
        extends AppCompatActivity
        implements
        SelectSavedArticleContract.View,
        ListViewCompat.OnItemClickListener,
        ListViewCompat.OnItemLongClickListener {

    private SelectSavedArticleContract.Presenter mPresenter;

    private ListViewCompat mListView;
    private AppCompatTextView mListEmptyTextView;

    private SavedArticlesListAdapter mAdapter;

    private int mLastLongClickPosition;

    public static final String EXTRA_STRING_KEY_SHA1SUM = "Sha1sum";

    @Override // AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_saved_articles);

        mListView = (ListViewCompat) findViewById(R.id.activity_select_saved_articles_list);
        mListEmptyTextView = (AppCompatTextView) findViewById(R.id.activity_select_saved_articles_list_empty);

        setResult(RESULT_CANCELED);

        mPresenter = new SelectSavedArticlePresenter(SelectSavedArticleActivity.this);
        mPresenter.start();
    }

    @Override // SelectSavedArticleContract.View
    public BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override // SelectSavedArticleContract.View
    public Context getContext() {
        return getApplicationContext();
    }

    @Override // SelectSavedArticleContract.View
    public void initListView() {
        mAdapter = new SavedArticlesListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mListEmptyTextView);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        List<SavedArticleEntry> savedArticles = new ArrayList<>();
        Iterator<Map.Entry<String, String>> entryIterator = mPresenter.listArticlesIterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, String> entry = entryIterator.next();
            String timeSaved = entry.getKey();
            String sha1sum = entry.getValue();
            String title = mPresenter.getArticleTitle(sha1sum);
            SavedArticleEntry savedArticle = new SavedArticleEntry(timeSaved, sha1sum, title);
            savedArticles.add(savedArticle);
        }

        mAdapter.clear();
        mAdapter.addAll(savedArticles);
    }

    @Override // ListViewCompat.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SavedArticleEntry savedArticle = mAdapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_STRING_KEY_SHA1SUM, savedArticle.sha1sum);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override // ListViewCompat.OnItemLongClickListener
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mLastLongClickPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("지우시겠습니까?")
                .setCancelable(true)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SavedArticleEntry savedArticle = mAdapter.getItem(mLastLongClickPosition);
                        if (mPresenter.deleteArticle(savedArticle.sha1sum)) {
                            mAdapter.remove(mAdapter.getItem(mLastLongClickPosition));
                        }
                    }
                })
                .show();
        return true;
    }
}
