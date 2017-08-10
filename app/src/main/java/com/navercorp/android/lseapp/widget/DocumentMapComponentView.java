package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentMapValue;

public class DocumentMapComponentView
        extends RelativeLayout
        implements
        DocumentComponentView<DocumentMapComponentView, DocumentMapValue>,
        View.OnFocusChangeListener,
        DocumentComponentModifierView.OnComponentAddListener {

    private DocumentMapValue mValue;

    private LinearLayoutCompat mContainerLayout;
    private AppCompatImageView mImageView;
    private AppCompatTextView mTitleTextView;
    private AppCompatTextView mAddressTextView;
    private DocumentComponentModifierView mComponentAdderView;

    private OnContentFocusChangeListener mOnContentFocusChangeListener;
    private OnInsertComponentListener mOnInsertComponentListener;

    public DocumentMapComponentView(Context context) {
        super(context);
        init();
    }

    public DocumentMapComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocumentMapComponentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override // DocumentComponentView
    public DocumentMapComponentView getView() {
        return this;
    }

    @Override // DocumentComponentView
    public void setValue(DocumentMapValue value) {
        mValue = value;

        int locationKatechX = mValue.getLocationKatechX();
        int locationKatechY = mValue.getLocationKatechY();
        String locationTitle = mValue.getLocationTitle();
        String locationAddress = mValue.getLocationAddress();
        String staticNaverMapUrl = String.format("https://openapi.naver.com/v1/map/staticmap.bin?clientId=kaA9LCofEDxiOmtLTsFT&url=naver.com&crs=NHN:128&center=%d,%d&level=3&w=300&h=300&baselayer=default&markers=%d,%d", locationKatechX, locationKatechY, locationKatechX, locationKatechY);

        Glide.with(getContext()).load(staticNaverMapUrl).into(mImageView);
        mTitleTextView.setText(Html.fromHtml(locationTitle));
        mAddressTextView.setText(locationAddress);
    }

    @Override // DocumentComponentView
    public DocumentMapValue getValue() {
        return mValue;
    }

    @Override // DocumentComponentView
    public boolean requestContentFocus() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mContainerLayout.getWindowToken(), 0);
        return mContainerLayout.requestFocus();
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
        inflate(getContext(), R.layout.view_document_map_component, this);

        mContainerLayout = (LinearLayoutCompat) findViewById(R.id.view_document_map_container);
        mImageView = (AppCompatImageView) findViewById(R.id.view_document_map_image);
        mTitleTextView = (AppCompatTextView) findViewById(R.id.view_document_map_text_title);
        mAddressTextView = (AppCompatTextView) findViewById(R.id.view_document_map_text_address);
        mComponentAdderView = (DocumentComponentModifierView) findViewById(R.id.view_document_map_document_component_adder);

        mContainerLayout.setOnFocusChangeListener(DocumentMapComponentView.this);
        mContainerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mContainerLayout.getWindowToken(), 0);
                mContainerLayout.requestFocus();
            }
        });
        mComponentAdderView.setOnComponentAddListener(DocumentMapComponentView.this);
    }
}
