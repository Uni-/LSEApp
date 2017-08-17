package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public enum DocumentComponentValueFactory {

    ;

    public static DocumentComponentValue get(ObjectValue objectValue) {
        if (objectValue.getContentType() == DocumentComponentType.TITLE.getGlobalTypeSerial()) {
            return DocumentTitleValue.createFromDataObject(objectValue);
        } else if (objectValue.getContentType() == DocumentComponentType.TEXT.getGlobalTypeSerial()) {
            return DocumentTextValue.createFromDataObject(objectValue);
        } else if (objectValue.getContentType() == DocumentComponentType.IMAGE.getGlobalTypeSerial()) {
            return DocumentImageStripValue.createFromDataObject(objectValue);
        } else if (objectValue.getContentType() == DocumentComponentType.MAP.getGlobalTypeSerial()) {
            return DocumentMapValue.createFromDataObject(objectValue);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
