package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.navercorp.android.lseapp.model.TextSpan;
import com.navercorp.android.lseapp.util.ColorPickerDialogBuilder;
import com.navercorp.android.lseapp.util.Selection;
import com.navercorp.android.lseapp.util.IntegerStepperDialogBuilder;

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
                break;
            }
            case R.id.view_window_bottom_bar_button_text_size: {
                IntegerStepperDialogBuilder builder = new IntegerStepperDialogBuilder(getContext());
                builder.value(24);
                builder.setOnDecideValueListener(this);
                builder.show();
                break;
            }
            case R.id.view_window_bottom_button_bar_text_color: {
                ColorPickerDialogBuilder builder = new ColorPickerDialogBuilder(getContext());
                builder.oldColor(Color.BLACK);
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

    @Override // AppCompatCheckBox.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.view_window_bottom_bar_checkbox_text_bold: {
                mTextBold = getTextBoldButtonChecked();
                // TODO
                break;
            }
            case R.id.view_window_bottom_bar_checkbox_text_italic: {
                mTextItalic = getTextItalicButtonChecked();
                // TODO
                break;
            }
            case R.id.view_window_bottom_bar_checkbox_text_underline: {
                mTextUnderline = getTextUnderlineButtonChecked();
                // TODO
                break;
            }
            default:
        }
    }

    @Override // ColorPickerDialogBuilder.OnDecideColorListener
    public void onDecideColor(DialogInterface dialog, int color) {
        setTextColorButtonValue(color);
    }

    @Override // IntegerStepperDialogBuilder.OnDecideValueListener
    public void onDecideValue(DialogInterface dialog, int value) {
        setTextFontSizeButtonValue(value);
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void updateButtons(DocumentComponentValue componentValue, Selection selection) {
        DocumentComponentType componentType = componentValue.componentType();
        InnerViewsPolicy policy = InnerViewsPolicy.findInstanceByComponentType(componentType);
        policy.arrangeButtons(this);
        policy.setButtonValues(this, componentValue, selection);
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
        mTextBoldCheckBox.setOnCheckedChangeListener(this);
        mTextItalicCheckBox.setOnCheckedChangeListener(this);
        mTextUnderlineCheckBox.setOnCheckedChangeListener(this);
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
        mTextSizeButton.setTextSize(fontSize);
    }

    private void setTextColorButtonValue(int textColor) {
        mTextColorButton.setTextColor(textColor);
    }

    private void dispatchRemoveComponent() {
        if (mActionListener != null) {
            mActionListener.onRemoveComponent();
        }
    }

    public interface ActionListener {
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
            public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Selection selection) {
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
            public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Selection selection) {
                DocumentTextValue textValue = (DocumentTextValue) value;
                TextSpan span = textValue.getSpanOverFocus(selection);
                view.setTextBoldButtonValue(span.isBold());
                view.setTextItalicButtonValue(span.isItalic());
                view.setTextUnderlineButtonValue(span.isUnderlined());
                view.setTextFontSizeButtonValue(textValue.getTextFontSize());
                view.setTextColorButtonValue(span.getFontColor());
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
            public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Selection selection) {
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
            public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Selection selection) {
                // TODO
            }
        }),
        ;

        private Delegate mDelegate;

        private InnerViewsPolicy(Delegate delegate) {
            mDelegate = delegate;
        }

        public void arrangeButtons(WindowBottomBarView view) {
            mDelegate.arrangeButtons(view);
        }

        public void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Selection selection) {
            mDelegate.setButtonValues(view, value, selection);
        }

        public static InnerViewsPolicy findInstanceByComponentType(DocumentComponentType componentType) {
            switch (componentType) {
                case TITLE: return TITLE;
                case TEXT: return TEXT;
                case IMAGE: return IMAGE;
                case MAP: return MAP;
                default: return null;
            }
        }

        private interface Delegate {
            void arrangeButtons(WindowBottomBarView view);
            void setButtonValues(WindowBottomBarView view, DocumentComponentValue value, Selection selection);
        }
    }
}
