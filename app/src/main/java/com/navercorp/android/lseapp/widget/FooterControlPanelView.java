package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentImageStripValue;
import com.navercorp.android.lseapp.model.DocumentTextValue;
import com.navercorp.android.lseapp.model.DocumentTitleValue;
import com.navercorp.android.lseapp.model.TextProperty;
import com.navercorp.android.lseapp.model.TextSpan;
import com.navercorp.android.lseapp.model.TextSpanSet;
import com.navercorp.android.lseapp.util.ColorPickerDialogBuilder;
import com.navercorp.android.lseapp.util.IntegerStepperDialogBuilder;
import com.navercorp.android.lseapp.util.Interval;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class FooterControlPanelView
        extends RelativeLayout
        implements
        View.OnClickListener,
        ColorPickerDialogBuilder.OnDecideColorListener,
        IntegerStepperDialogBuilder.OnDecideValueListener {

    private AppCompatButton mTitleSetBackgroundButton;
    private AppCompatButton mTitleUnsetBackgroundButton;
    private AppCompatCheckBox mTextBoldCheckBox;
    private AppCompatCheckBox mTextItalicCheckBox;
    private AppCompatCheckBox mTextUnderlineCheckBox;
    private AppCompatButton mTextSizeButton;
    private AppCompatButton mTextColorButton;
    private AppCompatButton mImageMergeButton;
    private AppCompatButton mImageSplitButton;
    private AppCompatButton mComponentRemoveButton;

    private Map<Control, View> mControlViewMap;

    private boolean mTextBold;
    private boolean mTextItalic;
    private boolean mTextUnderline;
    private int mTextFontSize;
    private int mTextColor;

    private ActionHandler mActionHandler;

    public FooterControlPanelView(Context context) {
        super(context);
        init();
    }

    public FooterControlPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterControlPanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override // View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_footer_control_panel_button_title_set_background: {
                dispatchTitleBackgroundSelect();
                break;
            }
            case R.id.view_footer_control_panel_button_title_unset_background: {
                dispatchTitleBackgroundRemove();
                break;
            }
            case R.id.view_footer_control_panel_checkbox_text_bold: {
                mTextBold = getTextBoldButtonChecked();
                dispatchTextBoldChange();
                break;
            }
            case R.id.view_footer_control_panel_checkbox_text_italic: {
                mTextItalic = getTextItalicButtonChecked();
                dispatchTextItalicChange();
                break;
            }
            case R.id.view_footer_control_panel_checkbox_text_underline: {
                mTextUnderline = getTextUnderlineButtonChecked();
                dispatchTextUnderlineChange();
                break;
            }
            case R.id.view_footer_control_panel_button_text_size: {
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
            case R.id.view_window_bottom_button_bar_image_merge: {
                dispatchImageStripMerge();
                break;
            }
            case R.id.view_window_bottom_button_bar_image_split: {
                dispatchImageStripSplit();
                break;
            }
            case R.id.view_footer_control_panel_button_component_remove: {
                dispatchRemoveComponent();
                break;
            }
            default:
        }
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

    public void setActionListener(ActionHandler actionHandler) {
        mActionHandler = actionHandler;
    }

    public void updateButtonsVisibility(DocumentComponentValue componentValue, DocumentComponentValue nextComponentValue) {
        EnumSet<Control> visibleControls = Control.getVisibleControls(componentValue, nextComponentValue);
        for (Map.Entry<Control, View> controlViewEntry : mControlViewMap.entrySet()) {
            Control control = controlViewEntry.getKey();
            View view = controlViewEntry.getValue();
            if (visibleControls.contains(control)) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public void updateButtonsValue(DocumentComponentValue componentValue, Interval interval) {
        DocumentComponentType componentType = componentValue.componentType();
        Control.ValuePolicy policy = Control.ValuePolicy.findInstanceByComponentType(componentType);
        policy.setButtonValues(this, componentValue, interval);
    }

    private void init() {
        inflate(getContext(), R.layout.view_footer_control_panel, this);

        mTitleSetBackgroundButton = (AppCompatButton) findViewById(R.id.view_footer_control_panel_button_title_set_background);
        mTitleUnsetBackgroundButton = (AppCompatButton) findViewById(R.id.view_footer_control_panel_button_title_unset_background);
        mTextBoldCheckBox = (AppCompatCheckBox) findViewById(R.id.view_footer_control_panel_checkbox_text_bold);
        mTextItalicCheckBox = (AppCompatCheckBox) findViewById(R.id.view_footer_control_panel_checkbox_text_italic);
        mTextUnderlineCheckBox = (AppCompatCheckBox) findViewById(R.id.view_footer_control_panel_checkbox_text_underline);
        mTextSizeButton = (AppCompatButton) findViewById(R.id.view_footer_control_panel_button_text_size);
        mTextColorButton = (AppCompatButton) findViewById(R.id.view_window_bottom_button_bar_text_color);
        mImageMergeButton = (AppCompatButton) findViewById(R.id.view_window_bottom_button_bar_image_merge);
        mImageSplitButton = (AppCompatButton) findViewById(R.id.view_window_bottom_button_bar_image_split);
        mComponentRemoveButton = (AppCompatButton) findViewById(R.id.view_footer_control_panel_button_component_remove);

        mControlViewMap = new EnumMap<Control, View>(Control.class);
        mControlViewMap.put(Control.TITLE_SET_BACKGROUND, mTitleSetBackgroundButton);
        mControlViewMap.put(Control.TITLE_UNSET_BACKGROUND, mTitleUnsetBackgroundButton);
        mControlViewMap.put(Control.TEXT_BOLD, mTextBoldCheckBox);
        mControlViewMap.put(Control.TEXT_ITALIC, mTextItalicCheckBox);
        mControlViewMap.put(Control.TEXT_UNDERLINE, mTextUnderlineCheckBox);
        mControlViewMap.put(Control.TEXT_SIZE, mTextSizeButton);
        mControlViewMap.put(Control.TEXT_COLOR, mTextColorButton);
        mControlViewMap.put(Control.IMAGE_MERGE, mImageMergeButton);
        mControlViewMap.put(Control.IMAGE_SPLIT, mImageSplitButton);
        mControlViewMap.put(Control.COMMON_REMOVE, mComponentRemoveButton);

        mTitleSetBackgroundButton.setOnClickListener(this);
        mTitleUnsetBackgroundButton.setOnClickListener(this);
        mTextBoldCheckBox.setOnClickListener(this);
        mTextItalicCheckBox.setOnClickListener(this);
        mTextUnderlineCheckBox.setOnClickListener(this);
        mTextSizeButton.setOnClickListener(this);
        mTextColorButton.setOnClickListener(this);
        mImageMergeButton.setOnClickListener(this);
        mImageSplitButton.setOnClickListener(this);
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

    private void dispatchTitleBackgroundSelect() {
        if (mActionHandler != null) {
            mActionHandler.onTitleBackgroundSelect();
        }
    }

    private void dispatchTitleBackgroundRemove() {
        if (mActionHandler != null) {
            mActionHandler.onTitleBackgroundRemove();
        }
    }

    private void dispatchTextBoldChange() {
        if (mActionHandler != null) {
            mActionHandler.onTextPropertyChange(TextProperty.BOLD, mTextBold);
        }
    }

    private void dispatchTextItalicChange() {
        if (mActionHandler != null) {
            mActionHandler.onTextPropertyChange(TextProperty.ITALIC, mTextItalic);
        }
    }

    private void dispatchTextUnderlineChange() {
        if (mActionHandler != null) {
            mActionHandler.onTextPropertyChange(TextProperty.UNDERLINE, mTextUnderline);
        }
    }

    private void dispatchTextFontSizeChange() {
        if (mActionHandler != null) {
            mActionHandler.onTextPropertyChange(TextProperty.SIZE, mTextFontSize);
        }
    }

    private void dispatchTextColorChange() {
        if (mActionHandler != null) {
            mActionHandler.onTextPropertyChange(TextProperty.COLOR, mTextColor);
        }
    }

    private void dispatchImageStripMerge() {
        if (mActionHandler != null) {
            mActionHandler.onImageStripMerge();
        }
    }

    private void dispatchImageStripSplit() {
        if (mActionHandler != null) {
            mActionHandler.onImageStripSplit();
        }
    }

    private void dispatchRemoveComponent() {
        if (mActionHandler != null) {
            mActionHandler.onRemoveComponent();
        }
    }

    public interface ActionHandler {

        void onTitleBackgroundSelect();

        void onTitleBackgroundRemove();

        void onTextPropertyChange(TextProperty textProperty, Object o);

        void onImageStripMerge();

        void onImageStripSplit();

        void onRemoveComponent();
    }

    private enum Control {
        TITLE_SET_BACKGROUND,
        TITLE_UNSET_BACKGROUND,
        TEXT_BOLD,
        TEXT_ITALIC,
        TEXT_UNDERLINE,
        TEXT_SIZE,
        TEXT_COLOR,
        IMAGE_MERGE,
        IMAGE_SPLIT,
        COMMON_REMOVE,
        @Deprecated
        UNUSED;

        private enum VisibilityPolicy {

            NOTHING(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return value == null;
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.noneOf(Control.class);
                }
            }),
            TITLE_NOBACKGROUND(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return (value != null) && value.componentType().equals(DocumentComponentType.TITLE) && !((DocumentTitleValue) value).hasBackgroundImage();
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.of(TITLE_SET_BACKGROUND);
                }
            }),
            TITLE_BACKGROUND(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return (value != null) && value.componentType().equals(DocumentComponentType.TITLE) && ((DocumentTitleValue) value).hasBackgroundImage();
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.of(TITLE_SET_BACKGROUND, TITLE_UNSET_BACKGROUND);
                }
            }),
            TEXT(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return (value != null) && value.componentType().equals(DocumentComponentType.TEXT);
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.of(TEXT_BOLD, TEXT_ITALIC, TEXT_UNDERLINE, TEXT_SIZE, TEXT_COLOR, COMMON_REMOVE);
                }
            }),
            IMAGE_STRIP_ONE_AND_SINGLE_IMAGE(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return (value != null) && value.componentType().equals(DocumentComponentType.IMAGE) && (((DocumentImageStripValue) value).count() == 1) && (nextValue != null) && nextValue.componentType().equals(DocumentComponentType.IMAGE) && (((DocumentImageStripValue) nextValue).count() == 1);
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.of(IMAGE_MERGE, COMMON_REMOVE);
                }
            }),
            IMAGE_STRIP_ONE_AND_NOT_SINGLE_IMAGE(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return (value != null) && value.componentType().equals(DocumentComponentType.IMAGE) && (((DocumentImageStripValue) value).count() == 1) && !((nextValue != null) && nextValue.componentType().equals(DocumentComponentType.IMAGE) && (((DocumentImageStripValue) nextValue).count() == 1));
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.of(COMMON_REMOVE);
                }
            }),
            IMAGE_STRIP_TWO_AND_SINGLE_IMAGE(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return (value != null) && value.componentType().equals(DocumentComponentType.IMAGE) && (((DocumentImageStripValue) value).count() == 2) && (nextValue != null) && nextValue.componentType().equals(DocumentComponentType.IMAGE) && (((DocumentImageStripValue) nextValue).count() == 1);
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.of(IMAGE_MERGE, IMAGE_SPLIT, COMMON_REMOVE);
                }
            }),
            IMAGE_STRIP_TWO_AND_NOT_SINGLE_IMAGE(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return (value != null) && value.componentType().equals(DocumentComponentType.IMAGE) && (((DocumentImageStripValue) value).count() == 2) && !((nextValue != null) && nextValue.componentType().equals(DocumentComponentType.IMAGE) && (((DocumentImageStripValue) nextValue).count() == 1));
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.of(IMAGE_SPLIT, COMMON_REMOVE);
                }
            }),
            IMAGE_STRIP_THREE(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return (value != null) && value.componentType().equals(DocumentComponentType.IMAGE) && (((DocumentImageStripValue) value).count() == 3);
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.of(IMAGE_SPLIT, COMMON_REMOVE);
                }
            }),
            MAP(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return (value != null) && value.componentType().equals(DocumentComponentType.MAP);
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.of(COMMON_REMOVE);
                }
            }),
            @Deprecated
            UNUSED(new Delegate() {
                @Override
                public boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue) {
                    return false;
                }

                @Override
                public EnumSet<Control> getVisibleControls() {
                    return EnumSet.noneOf(Control.class);
                }
            });

            private final Delegate mDelegate;

            private VisibilityPolicy(Delegate delegate) {
                mDelegate = delegate;
            }

            private interface Delegate {
                boolean matches(DocumentComponentValue value, DocumentComponentValue nextValue);

                EnumSet<Control> getVisibleControls();
            }
        }

        private enum ValuePolicy {

            TITLE(new Delegate() {
                @Override
                public void setButtonValues(FooterControlPanelView view, DocumentComponentValue value, Interval interval) {
                    // do nothing
                }
            }),
            TEXT(new Delegate() {
                @Override
                public void setButtonValues(FooterControlPanelView view, DocumentComponentValue value, Interval interval) {
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
                public void setButtonValues(FooterControlPanelView view, DocumentComponentValue value, Interval interval) {
                    // do nothing
                }
            }),
            MAP(new Delegate() {
                @Override
                public void setButtonValues(FooterControlPanelView view, DocumentComponentValue value, Interval interval) {
                    // do nothing
                }
            }),
            @Deprecated
            UNUSED(null);

            private Delegate mDelegate;

            private ValuePolicy(Delegate delegate) {
                mDelegate = delegate;
            }

            public void setButtonValues(FooterControlPanelView view, DocumentComponentValue value, Interval interval) {
                mDelegate.setButtonValues(view, value, interval);
            }

            public static ValuePolicy findInstanceByComponentType(DocumentComponentType componentType) {
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

                void setButtonValues(FooterControlPanelView view, DocumentComponentValue value, Interval interval);
            }
        }

        public static EnumSet<Control> getVisibleControls(DocumentComponentValue value, DocumentComponentValue nextValue) {
            for (VisibilityPolicy visibilityPolicy : VisibilityPolicy.values()) {
                if (visibilityPolicy.mDelegate.matches(value, nextValue)) {
                    return visibilityPolicy.mDelegate.getVisibleControls();
                }
            }
            return null;
        }
    }
}
