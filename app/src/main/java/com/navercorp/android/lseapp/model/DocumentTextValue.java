package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentTextValue implements DocumentComponentValue {

    private String mText;

    public DocumentTextValue() {
        mText = "";
    }

    public DocumentTextValue(DocumentTextValue another) {
        mText = new String(another.mText);
    }

    public DocumentTextValue(String text) {
        this.mText = text;
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

    public boolean isEmpty() {
        return mText.isEmpty();
    }
}
