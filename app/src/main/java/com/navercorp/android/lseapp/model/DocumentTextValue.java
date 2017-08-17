package com.navercorp.android.lseapp.model;

import com.android.internal.util.Predicate;
import com.navercorp.android.lseapp.util.Interval;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Map;

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

    @Override // DocumentComponentValue
    public DocumentComponentType componentType() {
        return DocumentComponentType.TEXT;
    }

    @Override // DocumentComponentValue
    public ObjectValue getDataObject() {
        byte[] textBytes = mText.getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(textBytes.length + mTextSpanSet.definedPropertiesCount() * 16 + 8);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putInt(textBytes.length);
        byteBuffer.put(textBytes);
        byteBuffer.putInt(mTextSpanSet.definedPropertiesCount());
        for (TextSpan textSpan : mTextSpanSet) {
            Interval interval = textSpan.getInterval();
            Map<TextProperty, Object> properties = textSpan.properties();
            for (Map.Entry<TextProperty, Object> propEntry : properties.entrySet()) {
                byteBuffer.putInt(interval.mLeftBound);
                byteBuffer.putInt(interval.mRightBound);
                byteBuffer.putInt(propEntry.getKey().ordinal());
                if (propEntry.getKey().valueType == Integer.class) {
                    byteBuffer.putInt((int) propEntry.getValue());
                } else if (propEntry.getKey().valueType == Boolean.class) {
                    byteBuffer.putInt((boolean) propEntry.getValue() ? 1 : 0);
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }
        return new ObjectValue(componentType().getGlobalTypeSerial(), byteBuffer.array());
    }

    @Override // Object
    public boolean equals(Object obj) {
        return (this == obj) || (obj instanceof DocumentTextValue) && (mText.equals(((DocumentTextValue) obj).mText)) && mTextSpanSet.equals(((DocumentTextValue) obj).mTextSpanSet);
    }

    @Override // Object
    public int hashCode() {
        return 855621 ^ mText.hashCode() ^ mTextSpanSet.hashCode();
    }

    @Override // Object
    public String toString() {
        return String.format("DocumentTextValue{mText=%s,mTextSpanSet=%s}", mText, mTextSpanSet.toString());
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


    public static DocumentTextValue createFromDataObject(ObjectValue dataObject) {
        if (dataObject.getContentType() != DocumentComponentType.TEXT.getGlobalTypeSerial()) {
            throw new IllegalArgumentException();
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(dataObject.getContentValue());
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        String text;
        TextSpanSet textSpanSet;
        do {
            byte[] textBuffer = new byte[byteBuffer.getInt()];
            for (int i = 0; i < textBuffer.length; i++) {
                textBuffer[i] = byteBuffer.get();
            }
            text = new String(textBuffer, Charset.forName("UTF-8"));
        } while (false);
        do {
            textSpanSet = new TextSpanSet();
            for (int textSpanSetDefinedPropsCount = byteBuffer.getInt(); textSpanSetDefinedPropsCount-- != 0; ) {
                Interval interval = new Interval(byteBuffer.getInt(), byteBuffer.getInt());
                TextSpan textSpan = new TextSpan(interval);
                TextProperty propertyKey = TextProperty.values()[byteBuffer.getInt()];
                Object propertyValue;
                if (propertyKey.valueType == Integer.class) {
                    propertyValue = byteBuffer.getInt();
                } else if (propertyKey.valueType == Boolean.class) {
                    propertyValue = byteBuffer.getInt() != 0;
                } else {
                    throw new UnsupportedOperationException();
                }
                textSpan.set(propertyKey, propertyValue);
                textSpanSet.add(textSpan);
            }
        } while (false);
        return new DocumentTextValue(text, textSpanSet);
    }
}
