package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentImageStripValue;

public class DocumentImageStripComponentView
        extends RelativeLayout
        implements
        DocumentComponentView<DocumentImageStripComponentView, DocumentImageStripValue>,
        View.OnFocusChangeListener,
        DocumentComponentModifierView.OnComponentAddListener {

    private String mPath;
    private AppCompatImageView mImageView;
    private DocumentComponentModifierView mComponentAdderView;

    private OnEnterKeyListener mOnEnterKeyListener;
    private OnContentFocusChangeListener mOnContentFocusChangeListener;
    private OnInsertComponentListener mOnInsertComponentListener;

    public DocumentImageStripComponentView(Context context) {
        super(context);
        init();
    }

    public DocumentImageStripComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocumentImageStripComponentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override // DocumentComponentView
    public DocumentImageStripComponentView getView() {
        return this;
    }

    @Override // DocumentComponentView
    public void setValue(DocumentImageStripValue value) {
        mPath = value.getPath();
        Glide.with(this).load(mPath).into(mImageView);
    }

    @Override // DocumentComponentView
    public DocumentImageStripValue getValue() {
        return new DocumentImageStripValue(mPath);
    }

    @Override // DocumentComponentView
    public boolean requestContentFocus() {
        return mImageView.requestFocus();
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
        inflate(getContext(), R.layout.view_document_imagestrip_component, this);

        mImageView = (AppCompatImageView) findViewById(R.id.view_document_imagestrip_image);
        mComponentAdderView = (DocumentComponentModifierView) findViewById(R.id.view_document_imagestrip_document_component_adder);

        mImageView.setOnFocusChangeListener(DocumentImageStripComponentView.this);
        mComponentAdderView.setOnComponentAddListener(DocumentImageStripComponentView.this);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.requestFocus();
            }
        });
    }
}
