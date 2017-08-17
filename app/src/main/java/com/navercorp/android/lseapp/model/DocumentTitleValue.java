package com.navercorp.android.lseapp.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

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
    public ObjectValue getDataObject() {
        byte[] textBytes = mText.getBytes();
        byte[] backgroundImagePathBytes = mBackgroundImagePath.getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(textBytes.length + backgroundImagePathBytes.length + 8);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.putInt(textBytes.length);
        byteBuffer.put(textBytes);
        byteBuffer.putInt(backgroundImagePathBytes.length);
        byteBuffer.put(backgroundImagePathBytes);
        return new ObjectValue(componentType().getGlobalTypeSerial(), byteBuffer.array());
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

    public static DocumentTitleValue createFromDataObject(ObjectValue dataObject) {
        if (dataObject.getContentType() != DocumentComponentType.TITLE.getGlobalTypeSerial()) {
            throw new IllegalArgumentException();
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(dataObject.getContentValue());
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        String text;
        String backgroundImagePath;
        do {
            byte[] textBuffer = new byte[byteBuffer.getInt()];
            for (int i = 0; i < textBuffer.length; i++) {
                textBuffer[i] = byteBuffer.get();
            }
            text = new String(textBuffer, Charset.forName("UTF-8"));
        } while (false);
        do {
            byte[] backgroundImagePathBuffer = new byte[byteBuffer.getInt()];
            for (int i = 0; i < backgroundImagePathBuffer.length; i++) {
                backgroundImagePathBuffer[i] = byteBuffer.get();
            }
            backgroundImagePath = new String(backgroundImagePathBuffer, Charset.forName("UTF-8"));
        } while (false);
        return new DocumentTitleValue(text, backgroundImagePath);
    }
}
