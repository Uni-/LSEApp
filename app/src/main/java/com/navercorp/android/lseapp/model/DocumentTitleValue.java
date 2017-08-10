package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentTitleValue implements DocumentComponentValue {

    private String mText;
    private String mBackgroundImagePath;

    public DocumentTitleValue() {
        this("", "");
    }

    public DocumentTitleValue(DocumentTitleValue another) {
        this(another.mText, another.mBackgroundImagePath);
    }

    public DocumentTitleValue(String text) {
        this(text, "");
    }

    public DocumentTitleValue(String text, String backgroundImagePath) {
        mText = text;
        mBackgroundImagePath = backgroundImagePath;
    }

    @Override // DocumentComponentValue
    public DocumentComponentType componentType() {
        return DocumentComponentType.TITLE;
    }

    @Override // DocumentComponentValue
    public byte[] getDataAsBytes() {
        return new byte[0]; // TODO
    }

    @Override // DocumentComponentValue
    public void setDataFromBytes(byte[] data) {
        // TODO
    }

    @Override // Object
    public boolean equals(Object obj) {
        return (this == obj) || (obj instanceof DocumentTitleValue) && (mText.equals(((DocumentTitleValue) obj).mText)) && (mBackgroundImagePath.equals(((DocumentTitleValue) obj).mBackgroundImagePath));
    }

    @Override // Object
    public int hashCode() {
        return 927490 ^ mText.hashCode() ^ mBackgroundImagePath.hashCode();
    }

    @Override // Object
    public String toString() {
        return String.format("DocumentTitleValue{mText=%s,mBackgroundImagePath=%s}", mText, mBackgroundImagePath);
    }

    public String getText() {
        return mText;
    }

    public String getBackgroundImagePath() {
        return mBackgroundImagePath;
    }

    public boolean hasBackgroundImage() {
        return !mBackgroundImagePath.isEmpty();
    }
}
