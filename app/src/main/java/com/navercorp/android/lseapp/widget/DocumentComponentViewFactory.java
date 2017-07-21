package com.navercorp.android.lseapp.widget;

import android.content.Context;

import com.navercorp.android.lseapp.model.DocumentComponentType;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentComponentViewFactory {

    private final Context mContext;

    public DocumentComponentViewFactory(Context context) {
        mContext = context;
    }

    public DocumentComponentView get(DocumentComponentType type) {
        switch (type) {
            case TEXT:
                return new DocumentTextView(mContext);
            case TITLE:
                return new DocumentTitleView(mContext);
            default:
                return null;
        }
    }
}
