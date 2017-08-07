package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentImageStripValue implements DocumentComponentValue {

    private String mPath;

    public DocumentImageStripValue() {
        mPath = "";
    }

    public DocumentImageStripValue(DocumentImageStripValue another) {
        mPath = another.mPath;
    }

    public DocumentImageStripValue(String path) {
        mPath = path;
    }

    @Override
    public DocumentComponentType componentType() {
        return DocumentComponentType.IMAGE;
    }

    @Override
    public byte[] getDataAsBytes() {
        return new byte[0];
    }

    @Override
    public void setDataFromBytes(byte[] data) {
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj) || (obj instanceof DocumentImageStripValue) && true;
    }

    @Override
    public int hashCode() {
        return 380550;
    }

    public String getPath() {
        return mPath;
    }
}
