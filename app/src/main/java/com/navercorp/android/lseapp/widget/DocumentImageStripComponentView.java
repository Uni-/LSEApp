package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    private DocumentImageStripValue mValue;

    private LinearLayoutCompat mImagesLayout;
    private AppCompatImageView mImageView0;
    private AppCompatImageView mImageView1;
    private AppCompatImageView mImageView2;
    private DocumentComponentModifierView mComponentAdderView;

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
        mValue = value;

        String imagePath0 = mValue.getImagePath0();
        if (!imagePath0.isEmpty()) {
            Glide.with(getContext()).load(imagePath0).into(mImageView0);
            mImageView0.setVisibility(View.VISIBLE);
        } else {
            mImageView0.setVisibility(View.GONE);
        }

        String imagePath1 = mValue.getImagePath1();
        if (!imagePath1.isEmpty()) {
            Glide.with(getContext()).load(imagePath1).into(mImageView1);
            mImageView1.setVisibility(View.VISIBLE);
        } else {
            mImageView1.setVisibility(View.GONE);
        }

        String imagePath2 = mValue.getImagePath2();
        if (!imagePath2.isEmpty()) {
            Glide.with(getContext()).load(imagePath2).into(mImageView2);
            mImageView2.setVisibility(View.VISIBLE);
        } else {
            mImageView2.setVisibility(View.GONE);
        }
    }

    @Override // DocumentComponentView
    public DocumentImageStripValue getValue() {
        return mValue;
    }

    @Override // DocumentComponentView
    public boolean requestContentFocus() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mImagesLayout.getWindowToken(), 0);
        return mImagesLayout.requestFocus();
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
        inflate(getContext(), R.layout.view_document_imagestrip_component, this);

        mImagesLayout = (LinearLayoutCompat) findViewById(R.id.view_document_imagestrip_images);
        mImageView0 = (AppCompatImageView) findViewById(R.id.view_document_imagestrip_image_0);
        mImageView1 = (AppCompatImageView) findViewById(R.id.view_document_imagestrip_image_1);
        mImageView2 = (AppCompatImageView) findViewById(R.id.view_document_imagestrip_image_2);
        mComponentAdderView = (DocumentComponentModifierView) findViewById(R.id.view_document_imagestrip_document_component_adder);

        mImagesLayout.setOnFocusChangeListener(DocumentImageStripComponentView.this);
        mImagesLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mImagesLayout.getWindowToken(), 0);
                mImagesLayout.requestFocus();
            }
        });
        mComponentAdderView.setOnComponentAddListener(DocumentImageStripComponentView.this);
    }
}
