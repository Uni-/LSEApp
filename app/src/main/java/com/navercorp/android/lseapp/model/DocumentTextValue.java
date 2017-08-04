package com.navercorp.android.lseapp.model;

import com.android.internal.util.Predicate;
import com.navercorp.android.lseapp.util.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentTextValue implements DocumentComponentValue {

    private String mText;
    private TextSpanSet mTextSpanSet;

    public DocumentTextValue() {
        this("");
    }

    public DocumentTextValue(DocumentTextValue another) {
        this(another.mText, another.mTextSpanSet);
    }

    public DocumentTextValue(String text) {
        mText = text;
        mTextSpanSet = new TextSpanSet();
    }

    public DocumentTextValue(String text, TextSpanSet TextSpanSet) {
        mText = text;
        mTextSpanSet = new TextSpanSet(TextSpanSet);
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
        return (this == obj) || (obj instanceof DocumentTextValue) && (mText.equals(((DocumentTextValue) obj).mText)) && mTextSpanSet.equals(((DocumentTextValue) obj).mTextSpanSet);
    }

    @Override
    public int hashCode() {
        return 855621 ^ mText.hashCode() ^ mTextSpanSet.hashCode();
    }

    public String getText() {
        return mText;
    }

    public TextSpan[] getTextSpansArray() {
        return mTextSpanSet.toArray(new TextSpan[0]);
    }

    public boolean isEmpty() {
        return mText.isEmpty();
    }

    public TextSpanSet getTextSpans(final Interval interval) {
        final Predicate<TextSpan> p = new Predicate<TextSpan>() {
            @Override
            public boolean apply(TextSpan textSpan) {
                return textSpan.getInterval().intersects(interval) || (textSpan.getInterval().mRightBound == interval.mRightBound) && interval.isEmpty();
            }
        };
        return mTextSpanSet.filter(p);
    }

}
