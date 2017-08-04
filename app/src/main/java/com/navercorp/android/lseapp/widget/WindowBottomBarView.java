package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentTextValue;
import com.navercorp.android.lseapp.model.TextProperty;
import com.navercorp.android.lseapp.model.TextSpan;
import com.navercorp.android.lseapp.model.TextSpanSet;
import com.navercorp.android.lseapp.util.ColorPickerDialogBuilder;
import com.navercorp.android.lseapp.util.IntegerStepperDialogBuilder;
import com.navercorp.android.lseapp.util.Interval;

public class WindowBottomBarView
        extends RelativeLayout
        implements
        View.OnClickListener,
        AppCompatCheckBox.OnCheckedChangeListener,
        ColorPickerDialogBuilder.OnDecideColorListener,
        IntegerStepperDialogBuilder.OnDecideValueListener {

    private AppCompatButton mTitleBackgroundButton;
    private AppCompatCheckBox mTextBoldCheckBox;
    private AppCompatCheckBox mTextItalicCheckBox;
    private AppCompatCheckBox mTextUnderlineCheckBox;
    private AppCompatButton mTextSizeButton;
    private AppCompatButton mTextColorButton;
    private AppCompatButton mComponentRemoveButton;

    private boolean mTextBold;
    private boolean mTextItalic;
    private boolean mTextUnderline;
    private int mTextFontSize;
    private int mTextColor;

    private ActionListener mActionListener;

    public WindowBottomBarView(Context context) {
        super(context);
        init();
    }

    public WindowBottomBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WindowBottomBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override // View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_window_bottom_bar_button_title_background: {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getContext().startActivity(intent); ;
                break;
            }
            case R.id.view_window_bottom_bar_checkbox_text_bold: {
                mTextBold = getTextBoldButtonChecked();
                dispatchTextBoldChange();
                break;
            }
            case R.id.view_window_bottom_bar_checkbox_text_italic: {
                mTextItalic = getTextItalicButtonChecked();
                dispatchTextItalicChange();
                break;
            }
            case R.id.view_window_bottom_bar_checkbox_text_underline: {
                mTextUnderline = getTextUnderlineButtonChecked();
                dispatchTextUnderlineChange();
                break;
            }
            case R.id.view_window_bottom_bar_button_text_size: {
                IntegerStepperDialogBuilder builder = new IntegerStepperDialogBuilder(getContext());
                builder.value(mTextFontSize);
                builder.setOnDecideValueListener(this);
                builder.show();
                break;
            }
            case R.id.view_window_bottom_button_bar_text_color: {
                ColorPickerDialogBuilder builder = new ColorPickerDialogBuilder(getContext());
                builder.oldColor(mTextColor).newColor(mTextColor);
                builder.setOnDecideColorListener(this);
                builder.show();
                break;
            }
            case R.id.view_window_bottom_bar_button_component_remove: {
                dispatchRemoveComponent();
                break;
            }
            default:
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO
    }

    @Override // ColorPickerDialogBuilder.OnDecideColorListener
    public void onDecideColor(DialogInterface dialog, int color) {
        setTextColorButtonValue(color);
        mTextColor = color;
        dispatchTextColorChange();
    }

    @Override // IntegerStepperDialogBuilder.OnDecideValueListener
    public void onDecideValue(DialogInterface dialog, int value) {
        setTextFontSizeButtonValue(value);
        mTextFontSize = value;
        dispatchTextFontSizeChange();
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void updateButtons(DocumentComponentValue componentValue, Interval interval) {
        DocumentComponentType componentType = componentValue.componentType();
        InnerViewsPolicy policy = InnerViewsPolicy.findInstanceByComponentType(componentType);
        policy.arrangeButtons(this);
        policy.setButtonValues(this, componentValue, interval);
    }

    private void init() {
        inflate(getContext(), R.layout.view_window_bottom_bar, this);

        mTitleBackgroundButton = (AppCompatButton) findViewById(R.id.view_window_bottom_bar_button_title_background);
        mTextBoldCheckBox = (AppCompatCheckBox) findViewById(R.id.view_window_bottom_bar_checkbox_text_bold);
        mTextItalicCheckBox = (AppCompatCheckBox) findViewById(R.id.view_window_bottom_bar_checkbox_text_italic);
        mTextUnderlineCheckBox = (AppCompatCheckBox) findViewById(R.id.view_window_bottom_bar_checkbox_text_underline);
        mTextSizeButton = (AppCompatButton) findViewById(R.id.view_window_bottom_bar_button_text_size);
        mTextColorButton = (AppCompatButton) findViewById(R.id.view_window_bottom_button_bar_text_color);
        mComponentRemoveButton = (AppCompatButton) findViewById(R.id.view_window_bottom_bar_button_component_remove);

        mTitleBackgroundButton.setOnClickListener(this);
        mTextBoldCheckBox.setOnClickListener(this);
        mTextItalicCheckBox.setOnClickListener(this);
        mTextUnderlineCheckBox.setOnClickListener(this);
        mTextSizeButton.setOnClickListener(this);
        mTextColorButton.setOnClickListener(this);
        mComponentRemoveButton.setOnClickListener(this);
    }

    private void setTextBoldButtonValue(boolean bold) {
        mTextBoldCheckBox.setChecked(bold);
    }

    private boolean getTextBoldButtonChecked() {
        return mTextBoldCheckBox.isChecked();
    }

    private void setTextItalicButtonValue(boolean italic) {
        mTextItalicCheckBox.setChecked(italic);
    }

    private boolean getTextItalicButtonChecked() {
        return mTextItalicCheckBox.isChecked();
    }

    private void setTextUnderlineButtonValue(boolean underline) {
        mTextUnderlineCheckBox.setChecked(underline);
    }

    private boolean getTextUnderlineButtonChecked() {
        return mTextUnderlineCheckBox.isChecked();
    }

    private void setTextFontSizeButtonValue(int fontSize) {
        mTextSizeButton.setText(String.valueOf(fontSize));
    }

    private void setTextColorButtonValue(int textColor) {
        mTextColorButton.setTextColor(textColor);
    }

    private void dispatchTextBoldChange() {
        if (mActionListener != null) {
            mActionListener.onTextPropertyChange(TextProperty.BOLD, mTextBold);
        }
    }

    private void dispatchTextItalicChange() {
        if (mActionListener != null) {
            mActionListener.onTextPropertyChange(TextProperty.ITALIC, mTextItalic);
        }
    }

    private void dispatchTextUnderlineChange() {
        if (mActionListener != null) {
            mActionListener.onTextPropertyChange(TextProperty.UNDERLINE, mTextUnderline);
        }
    }

    private void dispatchTextFontSizeChange() {
        if (mActionListener != null) {
            mActionListener.onTextPropertyChange(TextProperty.SIZE, mTextFontSize);
        }
    }

    private void dispatchTextColorChange() {
        if (mActionListener != null) {
            mActionListener.onTextPropertyChange(TextProperty.COLOR, mTextColor);
        }
    }

    private void dispatchRemoveComponent() {
        if (mActionListener != null) {
            mActionListener.onRemoveComponent();
        }
    }

    public interface ActionListener {
        void onTextPropertyChange(TextProperty textProperty, Object o);

        void onRemoveComponent();
    }

    private enum InnerViewsPolicy {

        TITLE(new Delegate() {
            @Override
            public void arrangeButtons(WindowBottomBarView view) {
                view.mTitleBackgroundButton.setVisibility(View.VISIBLE);
                view.mTextBoldCheckBox.setVisibility(View.GONE);
                view.mTextItalicCheckBox.setVisibility(View.GONE);
                view.mTextUnderlineCheckBox.setVisibility(View.GONE);
                view.mTextSizeButton.setVisibility(View.GONE);
                view.mTextColorButton.setVisibility(View.GONE);
                view.mComponentRemoveButton.setVisibility(View.GONE);
            }

            @Override
            public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Interval interval) {
                // do nothing
            }
        }),
        TEXT(new Delegate() {
            @Override
            public void arrangeButtons(WindowBottomBarView view) {
                view.mTitleBackgroundButton.setVisibility(View.GONE);
                view.mTextBoldCheckBox.setVisibility(View.VISIBLE);
                view.mTextItalicCheckBox.setVisibility(View.VISIBLE);
                view.mTextUnderlineCheckBox.setVisibility(View.VISIBLE);
                view.mTextSizeButton.setVisibility(View.VISIBLE);
                view.mTextColorButton.setVisibility(View.VISIBLE);
                view.mComponentRemoveButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Interval interval) {
                DocumentTextValue textValue = (DocumentTextValue) value;
                TextSpanSet spans = textValue.getTextSpans(interval);
                TextSpan span;
                if (spans.size() != 0) {
                    TextSpan spanKey = new TextSpan(interval);
                    span = spans.floor(spanKey) != null ? spans.floor(spanKey) : spans.ceiling(spanKey); // At least, one is not null
                } else {
                    span = new TextSpan();
                }
                view.setTextBoldButtonValue(view.mTextBold = span.isBold());
                view.setTextItalicButtonValue(view.mTextItalic = span.isItalic());
                view.setTextUnderlineButtonValue(view.mTextUnderline = span.isUnderlined());
                view.setTextFontSizeButtonValue(view.mTextFontSize = span.getFontSize());
                view.setTextColorButtonValue(view.mTextColor = span.getFontColor());
            }
        }),
        IMAGE(new Delegate() {
            @Override
            public void arrangeButtons(WindowBottomBarView view) {
                view.mTitleBackgroundButton.setVisibility(View.GONE);
                view.mTextBoldCheckBox.setVisibility(View.GONE);
                view.mTextItalicCheckBox.setVisibility(View.GONE);
                view.mTextUnderlineCheckBox.setVisibility(View.GONE);
                view.mTextSizeButton.setVisibility(View.GONE);
                view.mTextColorButton.setVisibility(View.GONE);
                view.mComponentRemoveButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Interval interval) {
                // TODO
            }
        }),
        MAP(new Delegate() {
            @Override
            public void arrangeButtons(WindowBottomBarView view) {
                view.mTitleBackgroundButton.setVisibility(View.GONE);
                view.mTextBoldCheckBox.setVisibility(View.GONE);
                view.mTextItalicCheckBox.setVisibility(View.GONE);
                view.mTextUnderlineCheckBox.setVisibility(View.GONE);
                view.mTextSizeButton.setVisibility(View.GONE);
                view.mTextColorButton.setVisibility(View.GONE);
                view.mComponentRemoveButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Interval interval) {
                // TODO
            }
        }),
        @Deprecated
        UNUSED(null);

        private Delegate mDelegate;

        private InnerViewsPolicy(Delegate delegate) {
            mDelegate = delegate;
        }

        public void arrangeButtons(WindowBottomBarView view) {
            mDelegate.arrangeButtons(view);
        }

        public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Interval interval) {
            mDelegate.setButtonValues(view, value, interval);
        }

        public static InnerViewsPolicy findInstanceByComponentType(DocumentComponentType componentType) {
            switch (componentType) {
                case TITLE:
                    return TITLE;
                case TEXT:
                    return TEXT;
                case IMAGE:
                    return IMAGE;
                case MAP:
                    return MAP;
                default:
                    return null;
            }
        }

        private interface Delegate {
            void arrangeButtons(WindowBottomBarView view);

            void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Interval interval);
        }
    }
}
