package com.navercorp.android.lseapp.model;

import com.navercorp.android.lseapp.util.Selection;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by NAVER on 2017-07-31.
 */

public class TextSpan {

    public static final boolean TEXT_BOLD_DEFAULT = false;
    public static final boolean TEXT_ITALIC_DEFAULT = false;
    public static final boolean TEXT_UNDERLINED_DEFAULT = false;
    public static final int FONT_COLOR_DEFAULT = 0xff000000;

    private Selection mSelection;
    private Map<TextSpanEntry, Object> mValues;

    /// empty parameter constructor
    public TextSpan() {
        mSelection = new Selection();
        mValues = new TreeMap<>();
    }

    /// copy constructor
    public TextSpan(TextSpan another) {
        mSelection = new Selection(another.mSelection);
        mValues = new TreeMap<>(another.mValues);
    }

    public Object get(TextSpanEntry entry, Object defaultValue) {
        if (!entry.var.equals(defaultValue.getClass())) {
            throw new AssertionError();
        }
        Object o = mValues.get(entry);
        return (o != null) ? o : defaultValue;
    }

    public void set(TextSpanEntry entry, Object value) {
        if (!entry.var.equals(value.getClass())) {
            throw new AssertionError();
        }
        mValues.put(entry, value);
    }

    public boolean isBold() {
        final boolean defaultValue = TEXT_BOLD_DEFAULT;
        Object o = mValues.get(TextSpanEntry.BOLD);
        return (o != null) ? (boolean) o : defaultValue;
    }

    public void setBold(boolean bold) {
        mValues.put(TextSpanEntry.BOLD, bold);
    }

    public boolean isItalic() {
        final boolean defaultValue = TEXT_ITALIC_DEFAULT;
        Object o = mValues.get(TextSpanEntry.ITALIC);
        return (o != null) ? (boolean) o : defaultValue;
    }

    public void setItalic(boolean italic) {
        mValues.put(TextSpanEntry.ITALIC, italic);
    }

    public boolean isUnderlined() {
        final boolean defaultValue = TEXT_UNDERLINED_DEFAULT;
        Object o = mValues.get(TextSpanEntry.UNDERLINE);
        return (o != null) ? (boolean) o : defaultValue;
    }

    public void setUnderlined(boolean underlined) {
        mValues.put(TextSpanEntry.UNDERLINE, underlined);
    }

    public int getFontColor() {
        final int defaultValue = FONT_COLOR_DEFAULT;
        Object o = mValues.get(TextSpanEntry.COLOR);
        return (o != null) ? (int) o : defaultValue;
    }

    public void setFontColor(int fontColor) {
        mValues.put(TextSpanEntry.COLOR, fontColor);
    }

    public Selection getSelection() {
        return mSelection;
    }

    public void setSelection(Selection selection) {
        mSelection = selection;
    }
}
