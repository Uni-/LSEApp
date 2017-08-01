package com.navercorp.android.lseapp.model;

import com.navercorp.android.lseapp.util.Selection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentTextValue implements DocumentComponentValue {

    public static final int FONT_SIZE_DEFAULT_DP = 16;

    private String mText;
    private List<TextSpan> mTextSpansList;
    private int mTextFontSize;

    public DocumentTextValue() {
        this("");
    }

    public DocumentTextValue(DocumentTextValue another) {
        this(another.mText, another.mTextSpansList, another.mTextFontSize);
    }

    public DocumentTextValue(String text) {
        mText = text;
        mTextSpansList = new ArrayList<>();
        mTextFontSize = FONT_SIZE_DEFAULT_DP;
    }

    public DocumentTextValue(String text, List<TextSpan> textSpansList, int textFontSize) {
        mText = text;
        mTextSpansList = new ArrayList<>(textSpansList);
        mTextFontSize = textFontSize;
    }

    @Override
    public DocumentComponentType componentType() {
        return DocumentComponentType.TEXT;
    }

    @Override
    public byte[] getDataAsBytes() {
        return mText.getBytes();
    }

    @Override
    public void setDataFromBytes(byte[] data) {
        mText = new String(data);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DocumentTextValue && mText.equals(((DocumentTextValue) obj).mText);
    }

    @Override
    public int hashCode() {
        return mText.hashCode();
    }

    public String getText() {
        return mText;
    }

    public TextSpan[] getTextSpans() {
        return mTextSpansList.toArray(new TextSpan[0]);
    }

    public int getTextFontSize() {
        return mTextFontSize;
    }

    public boolean isEmpty() {
        return mText.isEmpty();
    }

    public TextSpan getSpanOverFocus(Selection selection) {
        return new TextSpan();
    }
}
