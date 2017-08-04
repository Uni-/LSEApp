package com.navercorp.android.lseapp.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.WindowManager;

import com.navercorp.android.lseapp.R;

/**
 * Created by NAVER on 2017-07-28.
 */

public class IntegerStepperDialogBuilder
        implements
        DialogInterface.OnClickListener,
        View.OnClickListener {

    private Context mContext;
    private AlertDialog mAlertDialog;

    private AppCompatButton mMinusButton;
    private AppCompatButton mPlusButton;
    private AppCompatTextView mValueTextView;

    private int mValue;
    private OnDecideValueListener mOnDecideValueListener;

    public IntegerStepperDialogBuilder(Context context) {
        mContext = context;
    }

    public void value(int value) {
        mValue = value;
    }

    public Dialog show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(R.string.dialog_integer_stepper_title);
        builder.setView(R.layout.dialog_integer_stepper);
        builder.setPositiveButton(R.string.dialog_color_picker_ok, this);
        builder.setNegativeButton(R.string.dialog_color_picker_cancel, this);
        builder.setCancelable(false);

        mAlertDialog = builder.show();

        mMinusButton = (AppCompatButton) mAlertDialog.findViewById(R.id.dialog_integer_stepper_button_minus);
        mPlusButton = (AppCompatButton) mAlertDialog.findViewById(R.id.dialog_integer_stepper_button_plus);
        mValueTextView = (AppCompatTextView) mAlertDialog.findViewById(R.id.dialog_integer_stepper_textview_value);

        mMinusButton.setOnClickListener(this);
        mPlusButton.setOnClickListener(this);

        showValueText();

        return mAlertDialog;
    }

    @Override // DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            dispatchOnDecideValue(mValue);
        }
    }

    @Override // View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_integer_stepper_button_minus: {
                mValue--;
                showValueText();
            }
            case R.id.dialog_integer_stepper_button_plus: {
                mValue++;
                showValueText();
            }
        }
    }

    public void setOnDecideValueListener(OnDecideValueListener onDecideValueListener) {
        mOnDecideValueListener = onDecideValueListener;
    }

    private void showValueText() {
        mValueTextView.setText(String.valueOf(mValue));
    }

    private void dispatchOnDecideValue(int value) {
        if (mOnDecideValueListener != null) {
            mOnDecideValueListener.onDecideValue(mAlertDialog, value);
        }
    }

    public interface OnDecideValueListener {
        void onDecideValue(DialogInterface dialog, int value);
    }
}
