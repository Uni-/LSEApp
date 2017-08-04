package com.navercorp.android.lseapp.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.WindowManager;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.navercorp.android.lseapp.R;

/**
 * Created by NAVER on 2017-07-28.
 */

public class ColorPickerDialogBuilder
        implements
        DialogInterface.OnClickListener,
        ColorPicker.OnColorChangedListener,
        SaturationBar.OnSaturationChangedListener,
        ValueBar.OnValueChangedListener {

    private Context mContext;
    private AlertDialog mAlertDialog;

    private ColorPicker mColorPicker;
    private SaturationBar mSaturationBar;
    private ValueBar mValueBar;
    private AppCompatTextView mTextView;

    private int mOldColor;
    private int mNewColor;
    private OnDecideColorListener mOnDecideColorListener;

    public ColorPickerDialogBuilder(Context context) {
        mContext = context;
    }

    public ColorPickerDialogBuilder oldColor(int oldColor) {
        mOldColor = oldColor;
        return this;
    }

    public ColorPickerDialogBuilder newColor(int newColor) {
        mNewColor = newColor;
        return this;
    }

    public Dialog show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(R.string.dialog_color_picker_title);
        builder.setView(R.layout.dialog_color_picker);
        builder.setPositiveButton(R.string.dialog_color_picker_ok, this);
        builder.setNegativeButton(R.string.dialog_color_picker_cancel, this);
        builder.setCancelable(false);

        mAlertDialog = builder.show();

        mColorPicker = (ColorPicker) mAlertDialog.findViewById(R.id.dialog_color_picker_colorpicker);
        mSaturationBar = (SaturationBar) mAlertDialog.findViewById(R.id.dialog_color_picker_saturationbar);
        mValueBar = (ValueBar) mAlertDialog.findViewById(R.id.dialog_color_picker_valuebar);
        mTextView = (AppCompatTextView) mAlertDialog.findViewById(R.id.dialog_color_picker_textview);

        mColorPicker.setOnColorChangedListener(this);
        mSaturationBar.setOnSaturationChangedListener(this);
        mValueBar.setOnValueChangedListener(this);

        mColorPicker.setOldCenterColor(mOldColor);
        mColorPicker.setShowOldCenterColor(false);
        mColorPicker.setColor(mNewColor);
        mColorPicker.setColor(mColorPicker.getColor());
        mSaturationBar.setColor(mColorPicker.getColor());
        mValueBar.setColor(mSaturationBar.getColor());

        showNewColorText();

        return mAlertDialog;
    }

    @Override // DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            int newColor = mValueBar.getColor();
            dispatchOnDecideColor();
        }
    }

    @Override // ColorPicker.OnColorChangedListener
    public void onColorChanged(int color) {
        mSaturationBar.setColor(color);
        mValueBar.setColor(mSaturationBar.getColor());
        mNewColor = mValueBar.getColor();
        showNewColorText();
    }

    @Override // SaturationBar.OnSaturationChangedListener
    public void onSaturationChanged(int saturation) {
        mValueBar.setColor(mSaturationBar.getColor());
        mNewColor = mValueBar.getColor();
        showNewColorText();
    }

    @Override // ValueBar.OnValueChangedListener
    public void onValueChanged(int value) {
        mNewColor = mValueBar.getColor();
        showNewColorText();
    }

    public void setOnDecideColorListener(OnDecideColorListener onDecideColorListener) {
        mOnDecideColorListener = onDecideColorListener;
    }

    private void showNewColorText() {
        float[] colorHSV = new float[3];

        Color.colorToHSV(mNewColor, colorHSV);
        String colorRGBString = "#".concat(String.format("%08x", mNewColor).substring(2));

        float luminance = colorLuminance(mNewColor);

        mTextView.setText(colorRGBString);
        mTextView.setBackgroundColor(mNewColor);
        if (/*colorHSV[1] < 0.8 && colorHSV[2] > 0.8*/ colorLuminance(mNewColor) > 0.6) {
            mTextView.setTextColor(Color.BLACK);
        } else {
            mTextView.setTextColor(Color.WHITE);
        }
    }

    private void dispatchOnDecideColor() {
        if (mOnDecideColorListener != null) {
            mOnDecideColorListener.onDecideColor(mAlertDialog, mNewColor);
        }
    }

    /**
     * from Android SDK; see W3C WCAG definition of relative luminance
     * <br />
     * cf. Color.luminance(int color) is not available in SDK version < 24
     */
    private static float colorLuminance(int color) {
        double red = Color.red(color) / 255.0;
        red = red < 0.03928 ? red / 12.92 : Math.pow((red + 0.055) / 1.055, 2.4);
        double green = Color.green(color) / 255.0;
        green = green < 0.03928 ? green / 12.92 : Math.pow((green + 0.055) / 1.055, 2.4);
        double blue = Color.blue(color) / 255.0;
        blue = blue < 0.03928 ? blue / 12.92 : Math.pow((blue + 0.055) / 1.055, 2.4);
        return (float) ((0.2126 * red) + (0.7152 * green) + (0.0722 * blue));
    }

    public interface OnDecideColorListener {
        void onDecideColor(DialogInterface dialog, int color);
    }
}
