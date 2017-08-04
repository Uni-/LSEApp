package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentTextValue;
import com.navercorp.android.lseapp.model.TextProperty;
import com.navercorp.android.lseapp.model.TextSpan;
import com.navercorp.android.lseapp.model.TextSpanSet;
import com.navercorp.android.lseapp.util.Interval;
import com.navercorp.android.lseapp.util.WrappedUnderlineSpan;

public class DocumentTextComponentView
        extends LinearLayout
        implements
        DocumentComponentView<DocumentTextComponentView, DocumentTextValue>,
        View.OnFocusChangeListener,
        DocumentComponentAdderView.OnComponentAddListener,
        SensitiveEditText.OnSelectionChangeListener {

    private SensitiveEditText mEditText;
    private DocumentComponentAdderView mComponentAdderView;

    private OnContentFocusChangeListener mOnContentFocusChangeListener;
    private OnInsertComponentListener mOnInsertComponentListener;
    private OnContentSelectionChangeListener mOnContentSelectionChangeListener;

    public DocumentTextComponentView(Context context) {
        super(context);
        init();
    }

    public DocumentTextComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocumentTextComponentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override // DocumentComponentView
    public DocumentTextComponentView getView() {
        return this;
    }

    @Override // DocumentComponentView
    public void setValue(DocumentTextValue value) {

        // (1/4). build a SpannableString

        SpannableString str = new SpannableString(value.getText());

        // (2/4). apply global default span
        // the beginning span must be SPAN_INCLUSIVE_INCLUSIVE

        final int flagsZeroth = Spanned.SPAN_INCLUSIVE_INCLUSIVE;
        if (TextSpan.TEXT_BOLD_DEFAULT) {
            CharacterStyle oStyle = new StyleSpan(Typeface.BOLD);
            str.setSpan(oStyle, 0, str.length(), flagsZeroth);
        }
        if (TextSpan.TEXT_ITALIC_DEFAULT) {
            CharacterStyle oStyle = new StyleSpan(Typeface.ITALIC);
            str.setSpan(oStyle, 0, str.length(), flagsZeroth);
        }
        if (TextSpan.TEXT_UNDERLINED_DEFAULT) {
            CharacterStyle oUnderline = new WrappedUnderlineSpan();
            str.setSpan(oUnderline, 0, str.length(), flagsZeroth);
        }
        CharacterStyle oSize = new AbsoluteSizeSpan(TextSpan.FONT_SIZE_DEFAULT_SP, true);
        str.setSpan(oSize, 0, str.length(), flagsZeroth);
        CharacterStyle oColor = new ForegroundColorSpan(TextSpan.FONT_COLOR_DEFAULT_ARGB);
        str.setSpan(oColor, 0, str.length(), flagsZeroth);

        // (3/4). apply local converted spans
        // local spans must be SPAN_EXCLUSIVE_INCLUSIVE

        for (TextSpan textSpan : value.getTextSpansArray()) {
            final Interval sel = textSpan.getInterval();
            final int flags = Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
            if (textSpan.isBold() != TextSpan.TEXT_BOLD_DEFAULT) {
                CharacterStyle o = new StyleSpan(Typeface.BOLD);
                str.setSpan(o, sel.mLeftBound, sel.mRightBound, flags);
            }
            if (textSpan.isItalic() != TextSpan.TEXT_ITALIC_DEFAULT) {
                CharacterStyle o = new StyleSpan(Typeface.ITALIC);
                str.setSpan(o, sel.mLeftBound, sel.mRightBound, flags);
            }
            if (textSpan.isUnderlined() != TextSpan.TEXT_UNDERLINED_DEFAULT) {
                CharacterStyle o = new WrappedUnderlineSpan();
                str.setSpan(o, sel.mLeftBound, sel.mRightBound, flags);
            }
            if (textSpan.getFontSize() != TextSpan.FONT_SIZE_DEFAULT_SP) {
                CharacterStyle o = new AbsoluteSizeSpan(textSpan.getFontSize());
                str.setSpan(o, sel.mLeftBound, sel.mRightBound, flags);
            }
            if (textSpan.getFontColor() != TextSpan.FONT_COLOR_DEFAULT_ARGB) {
                CharacterStyle o = new ForegroundColorSpan(textSpan.getFontColor());
                str.setSpan(o, sel.mLeftBound, sel.mRightBound, flags);
            }
        }

        // (4/4). set text of SensitiveEditText

        mEditText.setText(str);
    }

    @Override // DocumentComponentView
    public DocumentTextValue getValue() {

        Editable editable = mEditText.getText();

        TextSpanSet textSpanSet = new TextSpanSet();

        for (CharacterStyle cs : editable.getSpans(0, editable.length(), CharacterStyle.class)) {
            if (cs instanceof StyleSpan) {
                if ((((StyleSpan) cs).getStyle() & Typeface.BOLD) != 0) {
                    TextSpan textSpan = new TextSpan(new Interval(editable.getSpanStart(cs), editable.getSpanEnd(cs)));
                    textSpan.setBold(true);
                    textSpanSet.add(textSpan);
                }
                if ((((StyleSpan) cs).getStyle() & Typeface.ITALIC) != 0) {
                    TextSpan textSpan = new TextSpan(new Interval(editable.getSpanStart(cs), editable.getSpanEnd(cs)));
                    textSpan.setItalic(true);
                    textSpanSet.add(textSpan);
                }
            } else if (cs instanceof WrappedUnderlineSpan) {
                TextSpan textSpan = new TextSpan(new Interval(editable.getSpanStart(cs), editable.getSpanEnd(cs)));
                textSpan.setUnderlined(true);
                textSpanSet.add(textSpan);
            } else if (cs instanceof AbsoluteSizeSpan) {
                TextSpan textSpan = new TextSpan(new Interval(editable.getSpanStart(cs), editable.getSpanEnd(cs)));
                textSpan.setFontSize(((AbsoluteSizeSpan) cs).getSize());
                textSpanSet.add(textSpan);
            } else if (cs instanceof ForegroundColorSpan) {
                TextSpan textSpan = new TextSpan(new Interval(editable.getSpanStart(cs), editable.getSpanEnd(cs)));
                textSpan.setFontColor(((ForegroundColorSpan) cs).getForegroundColor());
                textSpanSet.add(textSpan);
            }
        }

        return new DocumentTextValue(editable.toString(), textSpanSet);
    }

    @Override // DocumentComponentView
    public boolean requestContentFocus() {
        return mEditText.requestFocus();
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

    @Override // SensitiveEditText.OnSelectionChangeListener
    public void onSelectionChange(int selStart, int selEnd) {
        Editable editable = mEditText.getText();
        CharacterStyle[] characterStyles = editable.getSpans(selStart, selEnd, CharacterStyle.class);
        for (CharacterStyle cs : characterStyles) {
            Log.v("onSelectionChange", String.format("CharacterStyle %s %d %d", cs.toString(), editable.getSpanStart(cs), editable.getSpanEnd(cs)));
        }
        TextSpanSet textSpanSet = getValue().getTextSpans(new Interval(selStart, selEnd));
        for (TextSpan ts : textSpanSet) {
            Log.v("onSelectionChange", ts.toString());
        }
        if (mOnContentSelectionChangeListener != null) {
            mOnContentSelectionChangeListener.onContentSelectionChange(this, new Interval(selStart, selEnd));
        }
    }

    public void setOnContentFocusChangeListener(OnContentFocusChangeListener onContentFocusChangeListener) {
        mOnContentFocusChangeListener = onContentFocusChangeListener;
    }

    public void setOnContentSelectionChangeListener(OnContentSelectionChangeListener onContentSelectionChangeListener) {
        mOnContentSelectionChangeListener = onContentSelectionChangeListener;
    }

    public void setOnInsertComponentListener(OnInsertComponentListener onInsertComponentListener) {
        mOnInsertComponentListener = onInsertComponentListener;
    }

    public void setSelectedTextProperty(TextProperty textProperty, Object value) {
        final Editable editable = mEditText.getText();
        final int selStart = mEditText.getSelectionStart();
        final int selEnd = mEditText.getSelectionEnd();
        final int flags = Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
        final Interval selInterval = new Interval(selStart, selEnd);

        if (selStart == selEnd) {
            editable.insert(selStart, " ");
            mEditText.setSelection(selStart, selStart + 1);
            setSelectedTextProperty(textProperty, value);
            return;
        }

        CharacterStyle[] characterStyles = editable.getSpans(selStart, selEnd, CharacterStyle.class);

        switch (textProperty) {
            case BOLD: {
                boolean isBold = (boolean) value;
                if (isBold) {
                    CharacterStyle cs = new StyleSpan(Typeface.BOLD);
                    editable.setSpan(cs, selStart, selEnd, flags);
                } else {
                    for (CharacterStyle cs : characterStyles) {
                        if (cs instanceof StyleSpan && (((StyleSpan) cs).getStyle() & Typeface.BOLD) != 0) {
                            Interval csInterval = new Interval(editable.getSpanStart(cs), editable.getSpanEnd(cs));
                            editable.removeSpan(cs);
                            for (Interval interval : Interval.minus(csInterval, selInterval)) {
                                editable.setSpan(new StyleSpan(Typeface.BOLD), interval.mLeftBound, interval.mRightBound, flags);
                            }
                        }
                    }
                }
                break;
            }
            case ITALIC: {
                boolean isItalic = (boolean) value;
                if (isItalic) {
                    CharacterStyle cs = new StyleSpan(Typeface.ITALIC);
                    editable.setSpan(cs, selStart, selEnd, flags);
                } else {
                    for (CharacterStyle cs : characterStyles) {
                        if (cs instanceof StyleSpan && (((StyleSpan) cs).getStyle() & Typeface.ITALIC) != 0) {
                            Interval csInterval = new Interval(editable.getSpanStart(cs), editable.getSpanEnd(cs));
                            editable.removeSpan(cs);
                            for (Interval interval : Interval.minus(csInterval, selInterval)) {
                                editable.setSpan(new StyleSpan(Typeface.ITALIC), interval.mLeftBound, interval.mRightBound, flags);
                            }
                        }
                    }
                }
                break;
            }
            case UNDERLINE: {
                boolean isUnderline = (boolean) value;
                if (isUnderline) {
                    CharacterStyle cs = new WrappedUnderlineSpan();
                    editable.setSpan(cs, selStart, selEnd, flags);
                } else {
                    for (CharacterStyle cs : characterStyles) {
                        if (cs instanceof WrappedUnderlineSpan) {
                            Interval csInterval = new Interval(editable.getSpanStart(cs), editable.getSpanEnd(cs));
                            editable.removeSpan(cs);
                            for (Interval interval : Interval.minus(csInterval, selInterval)) {
                                editable.setSpan(new WrappedUnderlineSpan(), interval.mLeftBound, interval.mRightBound, flags);
                            }
                        }
                    }
                }
                break;
            }
            case SIZE: {
                int size = (int) value;
                CharacterStyle cs = new AbsoluteSizeSpan(size, true);
                editable.setSpan(cs, selStart, selEnd, flags);
                break;
            }
            case COLOR: {
                int color = (int) value;
                CharacterStyle cs = new ForegroundColorSpan(color);
                editable.setSpan(cs, selStart, selEnd, flags);
                break;
            }
        }
    }

    private void init() {
        inflate(getContext(), R.layout.view_document_text_component, this);

        mEditText = (SensitiveEditText) findViewById(R.id.view_document_text_edit_text);
        mComponentAdderView = (DocumentComponentAdderView) findViewById(R.id.view_document_text_document_component_adder);

        mEditText.setOnFocusChangeListener(DocumentTextComponentView.this);
        mComponentAdderView.setOnComponentAddListener(DocumentTextComponentView.this);

        mEditText.setOnSelectionChangeListener(this);
    }

    public interface OnContentSelectionChangeListener {
        void onContentSelectionChange(DocumentTextComponentView v, Interval interval);
    }
}
