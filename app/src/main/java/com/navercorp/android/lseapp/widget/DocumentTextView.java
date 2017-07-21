package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class DocumentTextView
        extends android.support.v7.widget.AppCompatEditText
        implements DocumentComponentView<DocumentTextView> {

    public DocumentTextView(Context context) {
        super(context);
        init();
    }

    public DocumentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocumentTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setBackground(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.white)));
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public DocumentTextView getView() {
        return this;
    }

    @Override
    public void setData(byte[] bytes) {
    }
}
