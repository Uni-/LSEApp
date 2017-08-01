package com.navercorp.android.lseapp.util;

/**
 * Created by NAVER on 2017-07-31.
 */

public class Selection {
    public final int mStart;
    public final int mEnd;

    /// empty parameter constructor
    public Selection() {
        this(0, 0);
    }

    public Selection(int start, int end) {
        mStart = start;
        mEnd = end;
    }

    /// copy constructor
    public Selection(Selection another) {
        this(another.mStart, another.mEnd);
    }
}
