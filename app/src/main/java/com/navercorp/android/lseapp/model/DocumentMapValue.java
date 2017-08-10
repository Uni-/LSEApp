package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentMapValue implements DocumentComponentValue {

    private int mLocationKatechX;
    private int mLocationKatechY;
    private String mLocationTitle;
    private String mLocationAddress;

    public DocumentMapValue() {
        this(0, 0, "", "");
    }

    public DocumentMapValue(DocumentMapValue another) {
        this(another.mLocationKatechX, another.mLocationKatechY, another.mLocationTitle, another.mLocationAddress);
    }

    public DocumentMapValue(int locationKatechX, int locationKatechY, String locationTitle, String locationAddress) {
        mLocationKatechX = locationKatechX;
        mLocationKatechY = locationKatechY;
        mLocationTitle = locationTitle;
        mLocationAddress = locationAddress;
    }

    @Override // DocumentComponentValue
    public DocumentComponentType componentType() {
        return DocumentComponentType.MAP;
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
        return (this == obj) || (obj instanceof DocumentMapValue) && (mLocationKatechX == ((DocumentMapValue) obj).mLocationKatechX) && (mLocationKatechY == ((DocumentMapValue) obj).mLocationKatechY);
    }

    @Override // Object
    public int hashCode() {
        return 403982 ^ mLocationKatechX ^ mLocationKatechY;
    }

    @Override // Object
    public String toString() {
        return String.format("DocumentMapValue{mLocationKatechX=%d,mLocationKatechY=%d}", mLocationKatechX, mLocationKatechY);
    }

    public int getLocationKatechX() {
        return mLocationKatechX;
    }

    public int getLocationKatechY() {
        return mLocationKatechY;
    }

    public String getLocationTitle() {
        return mLocationTitle;
    }

    public String getLocationAddress() {
        return mLocationAddress;
    }
}
