package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentTextValue implements DocumentComponentValue {

    private String text;

    @Override
    public DocumentComponentType componentType() {
        return DocumentComponentType.TEXT;
    }

    @Override
    public byte[] getDataAsBytes() {
        return new byte[0];
    }

    @Override
    public void setDataFromBytes(byte[] data) {
        text = new String(data);
    }
}
