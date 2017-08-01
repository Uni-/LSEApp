package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentTitleValue;

public class DocumentTitleComponentView
        extends LinearLayout
        implements
        DocumentComponentView<DocumentTitleComponentView, DocumentTitleValue>,
        View.OnKeyListener,
        View.OnFocusChangeListener,
        DocumentComponentAdderView.OnComponentAddListener {

    private AppCompatEditText mEditText;
    private DocumentComponentAdderView mComponentAdderView;

    private OnEnterKeyListener mOnEnterKeyListener;
    private OnContentFocusChangeListener mOnContentFocusChangeListener;
    private OnInsertComponentListener mOnInsertComponentListener;

    public DocumentTitleComponentView(Context context) {
        super(context);
        init();
    }

    public DocumentTitleComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocumentTitleComponentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override // DocumentComponentView
    public DocumentTitleComponentView getView() {
        return this;
    }

    @Override // DocumentComponentView
    public void setValue(DocumentTitleValue value) {
        mEditText.setText(value.getText());
    }

    @Override // DocumentComponentView
    public DocumentTitleValue getValue() {
        return new DocumentTitleValue(mEditText.getText().toString());
    }

    @Override // DocumentComponentView
    public boolean requestContentFocus() {
        return mEditText.requestFocus();
    }

    @Override // DocumentComponentView
    public void setComponentAdderVisibility(boolean visible) {
        mComponentAdderView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override // View.OnKeyListener
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mOnEnterKeyListener != null) {
                mOnEnterKeyListener.onEnterKey(this);
            }
            return true;
        } else {
            return false;
        }

    }

    @Override // View.OnFocusChangeListener
    public void onFocusChange(View v, boolean hasFocus) {
        if (mOnContentFocusChangeListener != null) {
            mOnContentFocusChangeListener.onContentFocusChange(this, hasFocus);
        }
    }

    @Override // DocumentComponentAdderView.OnComponentAddListener
    public void onAddComponent(View v, DocumentComponentType componentType) {
        if (mOnInsertComponentListener != null) {
            mOnInsertComponentListener.onInsertComponent(this, componentType);
        }
    }

    public void setOnEnterKeyListener(OnEnterKeyListener onEnterKeyListener) {
        mOnEnterKeyListener = onEnterKeyListener;
    }

    public void setOnContentFocusChangeListener(OnContentFocusChangeListener onContentFocusChangeListener) {
        mOnContentFocusChangeListener = onContentFocusChangeListener;
    }

    public void setOnInsertComponentListener(OnInsertComponentListener onInsertComponentListener) {
        mOnInsertComponentListener = onInsertComponentListener;
    }

    private void init() {
        inflate(getContext(), R.layout.view_document_title_component, this);

        mEditText = (AppCompatEditText) findViewById(R.id.view_document_title_edit_text);
        mComponentAdderView = (DocumentComponentAdderView) findViewById(R.id.view_document_title_document_component_adder);

        mEditText.setOnKeyListener(DocumentTitleComponentView.this);
        mEditText.setOnFocusChangeListener(DocumentTitleComponentView.this);
        mComponentAdderView.setOnComponentAddListener(DocumentTitleComponentView.this);
    }
}
