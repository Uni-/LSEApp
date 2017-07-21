package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentTitleValue implements DocumentComponentValue {

    private String text;

    @Override
    public DocumentComponentType componentType() {
        return DocumentComponentType.TITLE;
    }

    @Override
    public byte[] getDataAsBytes() {
        return text.getBytes();
    }

    @Override
    public void setDataFromBytes(byte[] data) {
        text = new String(data);
    }
}
