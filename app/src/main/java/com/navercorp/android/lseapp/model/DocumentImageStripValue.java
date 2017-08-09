package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentImageStripValue implements DocumentComponentValue {

    private final String mImagePath0;
    private final String mImagePath1;
    private final String mImagePath2;

    public DocumentImageStripValue() {
        this("", "", "");
    }

    public DocumentImageStripValue(DocumentImageStripValue another) {
        this(another.mImagePath0, another.mImagePath1, another.mImagePath2);
    }

    public DocumentImageStripValue(String imagePath0) {
        this(imagePath0, "", "");
    }

    public DocumentImageStripValue(String imagePath0, String imagePath1) {
        this(imagePath0, imagePath1, "");
    }

    public DocumentImageStripValue(String imagePath0, String imagePath1, String imagePath2) {
        mImagePath0 = imagePath0;
        mImagePath1 = imagePath1;
        mImagePath2 = imagePath2;
    }

    @Override // DocumentComponentValue
    public DocumentComponentType componentType() {
        return DocumentComponentType.IMAGE;
    }

    @Override // DocumentComponentValue
    public byte[] getDataAsBytes() {
        return new byte[0];
    }

    @Override // DocumentComponentValue
    public void setDataFromBytes(byte[] data) {
    }

    @Override // Object
    public boolean equals(Object obj) {
        return (this == obj) || (obj instanceof DocumentImageStripValue) && mImagePath0.equals(((DocumentImageStripValue) obj).mImagePath0) && mImagePath1.equals(((DocumentImageStripValue) obj).mImagePath1) && mImagePath2.equals(((DocumentImageStripValue) obj).mImagePath2);
    }

    @Override // Object
    public int hashCode() {
        return 380550 ^ mImagePath0.hashCode() ^ mImagePath1.hashCode() ^ mImagePath2.hashCode();
    }

    @Override // Object
    public String toString() {
        return String.format("DocumentImageStripValue{mImagePath0=%s,mImagePath1=%s,mImagePath2=%s}", mImagePath0, mImagePath1, mImagePath2);
    }

    public String getImagePath0() {
        return mImagePath0;
    }

    public String getImagePath1() {
        return mImagePath1;
    }

    public String getImagePath2() {
        return mImagePath2;
    }

    public int count() {
        return ((!mImagePath0.isEmpty()) ? 1 : 0) + ((!mImagePath1.isEmpty()) ? 1 : 0) + ((!mImagePath2.isEmpty()) ? 1 : 0);
    }
}
