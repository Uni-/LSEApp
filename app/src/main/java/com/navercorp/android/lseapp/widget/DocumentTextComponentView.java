package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentTextValue;

public class DocumentTextComponentView
        extends RelativeLayout
        implements
        DocumentComponentView<DocumentTextComponentView, DocumentTextValue>,
        View.OnFocusChangeListener,
        DocumentComponentAdderView.OnComponentAddListener {

    private AppCompatEditText mEditText;
    private DocumentComponentAdderView mComponentAdderView;

    private OnContentFocusChangeListener mOnContentFocusChangeListener;
    private OnInsertComponentListener mOnInsertComponentListener;

    public DocumentTextComponentView(Context context) {
        super(context);
        init();
    }

    public DocumentTextComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocumentTextComponentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override // DocumentComponentView
    public DocumentTextComponentView getView() {
        return this;
    }

    @Override // DocumentComponentView
    public void setValue(DocumentTextValue value) {
        mEditText.setText(value.getText());
    }

    @Override // DocumentComponentView
    public DocumentTextValue getValue() {
        return new DocumentTextValue(mEditText.getText().toString());
    }

    @Override // DocumentComponentView
    public boolean requestContentFocus() {
        return mEditText.requestFocus();
    }

    @Override // DocumentComponentView
    public void setComponentAdderVisibility(boolean visible) {
        mComponentAdderView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
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

    public void setOnContentFocusChangeListener(OnContentFocusChangeListener onContentFocusChangeListener) {
        mOnContentFocusChangeListener = onContentFocusChangeListener;
    }

    public void setOnInsertComponentListener(OnInsertComponentListener onInsertComponentListener) {
        mOnInsertComponentListener = onInsertComponentListener;
    }

    private void init() {
        inflate(getContext(), R.layout.view_document_text_component, this);

        mEditText = (AppCompatEditText) findViewById(R.id.view_document_text_edit_text);
        mComponentAdderView = (DocumentComponentAdderView) findViewById(R.id.view_document_text_document_component_adder);

        mEditText.setOnFocusChangeListener(DocumentTextComponentView.this);
        mComponentAdderView.setOnComponentAddListener(DocumentTextComponentView.this);
    }
}
