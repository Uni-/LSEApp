package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by NAVER on 2017-07-31.
 */

public class SensitiveEditText extends AppCompatEditText {

    private OnSelectionChangeListener mOnSelectionChangeListener;

    public SensitiveEditText(Context context) {
        super(context);
    }

    public SensitiveEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SensitiveEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSelectionChangeListener(OnSelectionChangeListener onSelectionChangeListener) {
        mOnSelectionChangeListener = onSelectionChangeListener;
    }

    @Override // AppCompatEditText
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        if (mOnSelectionChangeListener != null) {
            mOnSelectionChangeListener.onSelectionChange(selStart, selEnd);
        }
    }

    public interface OnSelectionChangeListener {
        void onSelectionChange(int selStart, int selEnd);
    }
}
