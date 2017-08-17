package com.navercorp.android.lseapp.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * Created by NAVER on 2017-07-21.
 */

public class DocumentImageStripValue implements DocumentComponentValue {

    private String mImagePath0;
    private String mImagePath1;
    private String mImagePath2;

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
    public ObjectValue getDataObject() {
        byte[] imagePath0Bytes = mImagePath0.getBytes();
        byte[] imagePath1Bytes = mImagePath1.getBytes();
        byte[] imagePath2Bytes = mImagePath2.getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(imagePath0Bytes.length + imagePath1Bytes.length + imagePath2Bytes.length + 12);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putInt(imagePath0Bytes.length);
        byteBuffer.put(imagePath0Bytes);
        byteBuffer.putInt(imagePath1Bytes.length);
        byteBuffer.put(imagePath1Bytes);
        byteBuffer.putInt(imagePath2Bytes.length);
        byteBuffer.put(imagePath2Bytes);
        return new ObjectValue(componentType().getGlobalTypeSerial(), byteBuffer.array());
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

    public static DocumentImageStripValue createFromDataObject(ObjectValue dataObject) {
        if (dataObject.getContentType() != DocumentComponentType.IMAGE.getGlobalTypeSerial()) {
            throw new IllegalArgumentException();
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(dataObject.getContentValue());
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        String imagePath0;
        String imagePath1;
        String imagePath2;
        do {
            byte[] imagePath0Buffer = new byte[byteBuffer.getInt()];
            for (int i = 0; i < imagePath0Buffer.length; i++) {
                imagePath0Buffer[i] = byteBuffer.get();
            }
            imagePath0 = new String(imagePath0Buffer, Charset.forName("UTF-8"));
        } while (false);
        do {
            byte[] imagePath1Buffer = new byte[byteBuffer.getInt()];
            for (int i = 0; i < imagePath1Buffer.length; i++) {
                imagePath1Buffer[i] = byteBuffer.get();
            }
            imagePath1 = new String(imagePath1Buffer, Charset.forName("UTF-8"));
        } while (false);
        do {
            byte[] imagePath2Buffer = new byte[byteBuffer.getInt()];
            for (int i = 0; i < imagePath2Buffer.length; i++) {
                imagePath2Buffer[i] = byteBuffer.get();
            }
            imagePath2 = new String(imagePath2Buffer, Charset.forName("UTF-8"));
        } while (false);
        return new DocumentImageStripValue(imagePath0, imagePath1, imagePath2);
    }
}
