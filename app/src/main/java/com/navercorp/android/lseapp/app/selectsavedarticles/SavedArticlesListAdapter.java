package com.navercorp.android.lseapp.app.selectsavedarticles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.navercorp.android.lseapp.R;

/**
 * Created by NAVER on 2017-08-14.
 */

public class SavedArticlesListAdapter extends ArrayAdapter<SavedArticleEntry> {

    private static final int layout = R.layout.item_activity_select_saved_articles_list;

    public SavedArticlesListAdapter(Context context) {
        super(context, layout);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (convertView != null) ? convertView : layoutInflater.inflate(layout, null, false);
        AppCompatTextView mTitleTextView = (AppCompatTextView) view.findViewById(R.id.item_activity_select_saved_articles_list_title);
        AppCompatTextView mTimeTextView = (AppCompatTextView) view.findViewById(R.id.item_activity_select_saved_articles_list_time);
        SavedArticleEntry item = getItem(position);

        mTitleTextView.setText((!item.title.isEmpty()) ? item.title : "(제목 없음)");
        mTimeTextView.setText(item.timeSaved);

        return view;
    }
}
