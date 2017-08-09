package com.navercorp.android.lseapp.util;

import android.support.annotation.NonNull;

/**
 * Created by NAVER on 2017-07-31.
 */

/// This class is written assuming left open bounded right closed or left closed right open bounded.
public class Interval implements Comparable<Interval> {
    public final int mLeftBound;
    public final int mRightBound;

    /// empty parameter constructor
    public Interval() {
        this(0, 0);
    }

    /// copy constructor
    public Interval(Interval another) {
        this(another.mLeftBound, another.mRightBound);
    }

    public Interval(int leftBound, int rightBound) {
        if (rightBound < leftBound) {
            mRightBound = leftBound;
            mLeftBound = rightBound;
            return;
        }
        mLeftBound = leftBound;
        mRightBound = rightBound;
    }

    @Override // Object
    public boolean equals(Object obj) {
        return (this == obj) || (obj instanceof Interval) && (mLeftBound == ((Interval) obj).mLeftBound) && (mRightBound == ((Interval) obj).mRightBound);
    }

    @Override // Object
    public int hashCode() {
        return 66247 ^ mLeftBound ^ mRightBound;
    }

    @Override // Object
    public String toString() {
        return String.format("[%d, %d]", mLeftBound, mRightBound);
    }

    @Override // Comparable
    public int compareTo(@NonNull Interval o) {
        int result;
        result = Integer.compare(mLeftBound, o.mLeftBound);
        if (result != 0) {
            return result;
        } else {
            result = Integer.compare(mRightBound, o.mRightBound);
            return result;
        }
    }

    public boolean isEmpty() {
        return mLeftBound == mRightBound;
    }

    public boolean contains(Interval another) {
        return another.isEmpty() || (mLeftBound <= another.mLeftBound) && (another.mRightBound <= mRightBound);
    }

    public boolean intersects(Interval another) {
        return (mLeftBound < another.mLeftBound) && (another.mLeftBound < mRightBound) || (mLeftBound < another.mRightBound) && (another.mRightBound < mRightBound) ||
                (another.mLeftBound < mLeftBound) && (mLeftBound < another.mRightBound) || (another.mLeftBound < mRightBound) && (mRightBound < another.mRightBound) ||
                equals(another);
    }

    public boolean isDisjointWith(Interval another) {
        return !intersects(another);
    }

    public static Interval intersect(Interval a, Interval b) {
        if (a.intersects(b)) {
            return new Interval(Math.max(a.mLeftBound, b.mLeftBound), Math.min(a.mRightBound, b.mRightBound));
        } else {
            return null;
        }
    }

    public static Interval[] minus(Interval a, Interval b) {
        Interval i = intersect(a, b);

        if (i == null) {
            return new Interval[]{a};
        } else { // Interval a has a nonempty sub-Interval i
            boolean leftRest = a.mLeftBound != i.mLeftBound;
            boolean rightRest = a.mRightBound != i.mRightBound;
            int index = 0;
            Interval[] result = new Interval[(leftRest ? 1 : 0) + (rightRest ? 1 : 0)];
            if (leftRest) {
                result[index++] = new Interval(a.mLeftBound, i.mLeftBound);
            }
            if (rightRest) {
                result[index++] = new Interval(i.mRightBound, a.mRightBound);
            }
            return result;
        }
    }
}
