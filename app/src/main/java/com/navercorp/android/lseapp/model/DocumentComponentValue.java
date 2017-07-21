package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public interface DocumentComponentValue {

    DocumentComponentType componentType();

    byte[] getDataAsBytes();
    void setDataFromBytes(byte[] data);
}
