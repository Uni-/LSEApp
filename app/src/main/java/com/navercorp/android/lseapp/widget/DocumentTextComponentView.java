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
import android.view.View;
import android.widget.LinearLayout;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentTextValue;
import com.navercorp.android.lseapp.model.TextSpan;
import com.navercorp.android.lseapp.util.Selection;

import java.util.ArrayList;
import java.util.List;

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
        CharacterStyle oSize = new AbsoluteSizeSpan(value.getTextFontSize(), true);
        str.setSpan(oSize, 0, str.length(), flagsZeroth);
        if (TextSpan.TEXT_BOLD_DEFAULT || TextSpan.TEXT_ITALIC_DEFAULT) {
            int style = 0;
            if (TextSpan.TEXT_BOLD_DEFAULT) {
                style |= Typeface.BOLD;
            }
            if (TextSpan.TEXT_ITALIC_DEFAULT) {
                style |= Typeface.ITALIC;
            }
            CharacterStyle oStyle = new StyleSpan(style);
            str.setSpan(oStyle, 0, str.length(), flagsZeroth);
        }
        if (TextSpan.TEXT_UNDERLINED_DEFAULT) {
            CharacterStyle oUnderline = new UnderlineSpan();
            str.setSpan(oUnderline, 0, str.length(), flagsZeroth);
        }
        CharacterStyle oColor = new ForegroundColorSpan(TextSpan.FONT_COLOR_DEFAULT);
        str.setSpan(oColor, 0, str.length(), flagsZeroth);

        // (3/4). apply local converted spans
        // local spans must be SPAN_EXCLUSIVE_INCLUSIVE

        for (TextSpan textSpan : value.getTextSpans()) {
            final Selection sel = textSpan.getSelection();
            final int flagsLocal = Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
            if (textSpan.isBold() != TextSpan.TEXT_BOLD_DEFAULT || textSpan.isItalic() != TextSpan.TEXT_ITALIC_DEFAULT) {
                int style = 0;
                if (textSpan.isBold()) {
                    style |= Typeface.BOLD;
                }
                if (textSpan.isItalic()) {
                    style |= Typeface.ITALIC;
                }
                CharacterStyle o = new StyleSpan(style);
                str.setSpan(o, sel.mStart, sel.mEnd, flagsLocal);
            }
            if (textSpan.isUnderlined() != TextSpan.TEXT_UNDERLINED_DEFAULT) {
                CharacterStyle o = new UnderlineSpan();
                str.setSpan(o, sel.mStart, sel.mEnd, flagsLocal);
            }
            if (textSpan.getFontColor() != TextSpan.FONT_COLOR_DEFAULT) {
                CharacterStyle o = new ForegroundColorSpan(textSpan.getFontColor());
                str.setSpan(o, sel.mStart, sel.mEnd, flagsLocal);
            }
        }

        // (4/4). set text of SensitiveEditText

        mEditText.setText(str);
    }

    @Override // DocumentComponentView
    public DocumentTextValue getValue() {

        Editable editable = mEditText.getText();

        String text = editable.toString();
        List<TextSpan> textSpans = new ArrayList<>();
        int textFontSize = DocumentTextValue.FONT_SIZE_DEFAULT_DP;

        for (CharacterStyle cs : editable.getSpans(0, text.length(), CharacterStyle.class)) {
            if (cs instanceof AbsoluteSizeSpan) {
                textFontSize = ((AbsoluteSizeSpan) cs).getSize();
            } else if (cs instanceof StyleSpan) {
                TextSpan textSpan = new TextSpan();
                textSpan.setBold((((StyleSpan) cs).getStyle() & Typeface.BOLD) != 0);
                textSpan.setItalic((((StyleSpan) cs).getStyle() & Typeface.ITALIC) != 0);
                textSpans.add(textSpan);
            } else if (cs instanceof UnderlineSpan) {
                TextSpan textSpan = new TextSpan();
                textSpan.setUnderlined(true);
                textSpans.add(textSpan);
            } else if (cs instanceof ForegroundColorSpan) {
                TextSpan textSpan = new TextSpan();
                textSpan.setFontColor(((ForegroundColorSpan) cs).getForegroundColor());
                textSpans.add(textSpan);
            }
        }

        return new DocumentTextValue(text, textSpans, textFontSize);
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
        if (mOnContentSelectionChangeListener != null)
            mOnContentSelectionChangeListener.onContentSelectionChange(this, new Selection(selStart, selEnd));
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

    private void init() {
        inflate(getContext(), R.layout.view_document_text_component, this);

        mEditText = (SensitiveEditText) findViewById(R.id.view_document_text_edit_text);
        mComponentAdderView = (DocumentComponentAdderView) findViewById(R.id.view_document_text_document_component_adder);

        mEditText.setOnFocusChangeListener(DocumentTextComponentView.this);
        mComponentAdderView.setOnComponentAddListener(DocumentTextComponentView.this);

        mEditText.setOnSelectionChangeListener(this);
    }

    public interface OnContentSelectionChangeListener {
        void onContentSelectionChange(DocumentTextComponentView v, Selection selection);
    }
}
