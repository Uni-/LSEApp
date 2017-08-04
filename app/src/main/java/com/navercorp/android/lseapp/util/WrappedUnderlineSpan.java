package com.navercorp.android.lseapp.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.UnderlineSpan;

/**
 * Created by NAVER on 2017-08-03.
 */

public class WrappedUnderlineSpan extends UnderlineSpan {

    Parcelable.Creator<WrappedUnderlineSpan> CREATOR = new Creator<WrappedUnderlineSpan>() {
        @Override
        public WrappedUnderlineSpan createFromParcel(Parcel source) {
            return new WrappedUnderlineSpan(source);
        }

        @Override
        public WrappedUnderlineSpan[] newArray(int size) {
            return new WrappedUnderlineSpan[0];
        }
    };

    public WrappedUnderlineSpan() {
        super();
    }

    public WrappedUnderlineSpan(Parcel src) {
        super(src);
    }
}
