package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentTitleValue implements DocumentComponentValue {

    private String mText;

    public DocumentTitleValue() {
        mText = "";
    }

    public DocumentTitleValue(DocumentTitleValue another) {
        mText = new String(another.mText);
    }

    public DocumentTitleValue(String text) {
        this.mText = text;
    }

    @Override
    public DocumentComponentType componentType() {
        return DocumentComponentType.TITLE;
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
        return (this == obj) || (obj instanceof DocumentTitleValue) && (mText.equals(((DocumentTitleValue) obj).mText));
    }

    @Override
    public int hashCode() {
        return 927490 ^ mText.hashCode();
    }

    public String getText() {
        return mText;
    }
}
