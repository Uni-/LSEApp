package com.navercorp.android.lseapp.widget;

import android.view.View;

import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;

/**
 * Created by NAVER on 2017-07-21.
 */

public interface DocumentComponentView<ViewType extends View & DocumentComponentView<ViewType, ValueType>, ValueType extends DocumentComponentValue> {

    ViewType getView();

    void setValue(ValueType value);

    ValueType getValue();

    boolean requestContentFocus();

    void setComponentAdderVisibility(boolean visible);

    public interface OnEnterKeyListener {
        void onEnterKey(DocumentComponentView v);
    }

    public interface OnContentFocusChangeListener {
        void onContentFocusChange(DocumentComponentView v, boolean hasFocus);
    }

    public interface OnInsertComponentListener {
        void onInsertComponent(DocumentComponentView v, DocumentComponentType componentType);
    }
}
