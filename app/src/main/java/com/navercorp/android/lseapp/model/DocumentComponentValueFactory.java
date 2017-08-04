package com.navercorp.android.lseapp.model;

import android.content.Context;

import com.navercorp.android.lseapp.widget.DocumentComponentView;
import com.navercorp.android.lseapp.widget.DocumentTextComponentView;
import com.navercorp.android.lseapp.widget.DocumentTitleComponentView;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentComponentValueFactory {

    public DocumentComponentValue create(DocumentComponentType type) {
        switch (type) {
            case TITLE:
                return new DocumentTitleValue();
            case TEXT:
                return new DocumentTextValue();
            default: // TODO
                return null;
        }
    }
}
