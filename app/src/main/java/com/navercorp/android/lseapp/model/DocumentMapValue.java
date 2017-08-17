package com.navercorp.android.lseapp.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

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
    public ObjectValue getDataObject() {
        byte[] locationTitleBytes = mLocationTitle.getBytes();
        byte[] locationAddressBytes = mLocationAddress.getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(locationTitleBytes.length + locationAddressBytes.length + 16);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putInt(mLocationKatechX);
        byteBuffer.putInt(mLocationKatechY);
        byteBuffer.putInt(locationTitleBytes.length);
        byteBuffer.put(locationTitleBytes);
        byteBuffer.putInt(locationAddressBytes.length);
        byteBuffer.put(locationAddressBytes);
        return new ObjectValue(componentType().getGlobalTypeSerial(), byteBuffer.array());
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

    public static DocumentMapValue createFromDataObject(ObjectValue dataObject) {
        if (dataObject.getContentType() != DocumentComponentType.MAP.getGlobalTypeSerial()) {
            throw new IllegalArgumentException();
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(dataObject.getContentValue());
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        int locationKatechX = byteBuffer.getInt();
        int locationKatechY = byteBuffer.getInt();
        String locationTitle;
        String locationAddress;
        do {
            byte[] locationTitleBuffer = new byte[byteBuffer.getInt()];
            for (int i = 0; i < locationTitleBuffer.length; i++) {
                locationTitleBuffer[i] = byteBuffer.get();
            }
            locationTitle = new String(locationTitleBuffer, Charset.forName("UTF-8"));
        } while (false);
        do {
            byte[] locationAddressBuffer = new byte[byteBuffer.getInt()];
            for (int i = 0; i < locationAddressBuffer.length; i++) {
                locationAddressBuffer[i] = byteBuffer.get();
            }
            locationAddress = new String(locationAddressBuffer, Charset.forName("UTF-8"));
        } while (false);
        return new DocumentMapValue(locationKatechX, locationKatechY, locationTitle, locationAddress);
    }
}
