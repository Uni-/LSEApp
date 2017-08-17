package com.navercorp.android.lseapp.model;

import android.support.annotation.NonNull;

import com.navercorp.android.lseapp.util.Interval;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by NAVER on 2017-07-31.
 */

public class TextSpan implements Comparable<TextSpan> {

    public static final boolean TEXT_BOLD_DEFAULT = false;
    public static final boolean TEXT_ITALIC_DEFAULT = false;
    public static final boolean TEXT_UNDERLINED_DEFAULT = false;
    public static final int FONT_SIZE_DEFAULT_SP = 16;
    public static final int FONT_COLOR_DEFAULT_ARGB = 0xff000000;

    private final Interval mInterval;
    private final Map<TextProperty, Object> mPropertyValues; // for implementation, use EnumMap

    /// empty parameter constructor
    public TextSpan() {
        mInterval = new Interval();
        mPropertyValues = new EnumMap<>(TextProperty.class);
    }

    /// copy constructor
    public TextSpan(TextSpan another) {
        mInterval = another.mInterval;
        mPropertyValues = new EnumMap<>(another.mPropertyValues);
    }

    public TextSpan(Interval interval) {
        mInterval = interval;
        mPropertyValues = new EnumMap<>(TextProperty.class);
    }

    public TextSpan(Interval interval, Map<TextProperty, Object> propertyValues) {
        mInterval = interval;
        mPropertyValues = new EnumMap<>(TextProperty.class);
        mPropertyValues.putAll(propertyValues);
    }

    public Object get(TextProperty entry, Object defaultValue) {
        if (!entry.valueType.equals(defaultValue.getClass())) {
            throw new AssertionError();
        }
        Object o = mPropertyValues.get(entry);
        return (o != null) ? o : defaultValue;
    }

    public void set(TextProperty entry, Object value) {
        if (!entry.valueType.equals(value.getClass())) {
            throw new AssertionError();
        }
        mPropertyValues.put(entry, value);
    }

    public void unset(TextProperty entry) {
        mPropertyValues.remove(entry);
    }

    public boolean isBold() {
        final boolean defaultValue = TEXT_BOLD_DEFAULT;
        Object o = mPropertyValues.get(TextProperty.BOLD);
        return (o != null) ? (boolean) o : defaultValue;
    }

    public void setBold(boolean bold) {
        mPropertyValues.put(TextProperty.BOLD, bold);
    }

    public boolean isItalic() {
        final boolean defaultValue = TEXT_ITALIC_DEFAULT;
        Object o = mPropertyValues.get(TextProperty.ITALIC);
        return (o != null) ? (boolean) o : defaultValue;
    }

    public void setItalic(boolean italic) {
        mPropertyValues.put(TextProperty.ITALIC, italic);
    }

    public boolean isUnderlined() {
        final boolean defaultValue = TEXT_UNDERLINED_DEFAULT;
        Object o = mPropertyValues.get(TextProperty.UNDERLINE);
        return (o != null) ? (boolean) o : defaultValue;
    }

    public void setUnderlined(boolean underlined) {
        mPropertyValues.put(TextProperty.UNDERLINE, underlined);
    }

    public int getFontSize() {
        final int defaultValue = FONT_SIZE_DEFAULT_SP;
        Object o = mPropertyValues.get(TextProperty.SIZE);
        return (o != null) ? (int) o : defaultValue;
    }

    public void setFontSize(int fontColor) {
        mPropertyValues.put(TextProperty.SIZE, fontColor);
    }

    public int getFontColor() {
        final int defaultValue = FONT_COLOR_DEFAULT_ARGB;
        Object o = mPropertyValues.get(TextProperty.COLOR);
        return (o != null) ? (int) o : defaultValue;
    }

    public void setFontColor(int fontColor) {
        mPropertyValues.put(TextProperty.COLOR, fontColor);
    }

    public Interval getInterval() {
        return mInterval;
    }

    public Map<TextProperty, Object> properties() {
        return Collections.unmodifiableMap(mPropertyValues);
    }

    public void applyDefined(TextSpan another) {
        for (Map.Entry<TextProperty, Object> entry : another.mPropertyValues.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override // Object
    public boolean equals(Object obj) {
        return (this == obj) || (obj instanceof TextSpan) && (mInterval.equals(((TextSpan) obj).mInterval)) && (mPropertyValues.equals(((TextSpan) obj).mPropertyValues));
    }

    @Override // Object
    public int hashCode() {
        return 182039 ^ mInterval.hashCode() ^ mPropertyValues.hashCode();
    }

    @Override // Object
    public String toString() {
        return String.format("%s{mInterval=%s, mPropertyValues=%s}", getClass().getName(), mInterval.toString(), mPropertyValues.toString());
    }

    /**
     * This class does not follow a.equals(b) implying a.compareTo(b) == 0.
     */
    @Override // Comparable
    public int compareTo(@NonNull TextSpan o) {
        return mInterval.compareTo(o.mInterval);
    }
}
