package com.navercorp.android.lseapp.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.internal.util.Predicate;
import com.navercorp.android.lseapp.util.Interval;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by NAVER on 2017-08-02.
 */
/// see TreeSet<E>
/// not implementing 1.8+ methods
public class TextSpanSet extends AbstractSet<TextSpan> implements NavigableSet<TextSpan> {

    private final TreeSet<TextSpan> mInternalSet;

    public TextSpanSet() {
        mInternalSet = new TreeSet<>();
    }

    public TextSpanSet(Collection<? extends TextSpan> c) {
        this();
        addAll(c);
    }

    public TextSpanSet(TextSpanSet another) {
        mInternalSet = new TreeSet<>(another.mInternalSet);
    }

    private TextSpanSet(Set<TextSpan> internalSet) {
        mInternalSet = new TreeSet<>(internalSet);
    }

    /// constructors and add(), addAll() methods are designed to guarantee that:
    /// 1) no two TextSpan items have intersecting Intervals,
    /// 2) no TextSpan item has empty Interval.
    ///
    /// this implementation depends on four implementation truths:
    /// 1) Intervals are totally ordered left-to-right and so are TextSpans ordered through Interval,
    /// 2) Intervals are immutable,
    /// 3) mInterval field of TextSpan is final,
    /// 4) Iterator of TreeSet iterates over the collection in compareTo-defined left-to-right order.
    @Override
    public boolean add(TextSpan textSpan) {
        boolean modified = false;
        if (textSpan.getInterval().isEmpty()) {
            return false;
        }
        TreeSet<TextSpan> textSpansToAdd = new TreeSet<>();
        textSpansToAdd.add(new TextSpan(textSpan));
        Iterator<TextSpan> textSpanItemIterator = mInternalSet.iterator();
        while (textSpanItemIterator.hasNext()) {
            if (textSpansToAdd.isEmpty()) {
                break;
            }
            TextSpan textSpanItem = textSpanItemIterator.next();
            TextSpan textSpanToAddLast = textSpansToAdd.last();
            if (textSpanItem.getInterval().intersects(textSpanToAddLast.getInterval())) {
                Interval[] outerIntervalsPrior = Interval.minus(textSpanItem.getInterval(), textSpanToAddLast.getInterval());
                Interval innerInterval = Interval.intersect(textSpanItem.getInterval(), textSpanToAddLast.getInterval());
                Interval[] outerIntervalsPosterior = Interval.minus(textSpanToAddLast.getInterval(), textSpanItem.getInterval());
                textSpanItemIterator.remove();
                for (Interval outerInterval : outerIntervalsPrior) {
                    TextSpan outerSpan = new TextSpan(outerInterval, textSpanItem.properties());
                    mInternalSet.add(outerSpan);
                }
                TextSpan innerSpan = new TextSpan(innerInterval, textSpanItem.properties());
                innerSpan.applyDefined(textSpanToAddLast);
                mInternalSet.add(innerSpan);
                textSpansToAdd.remove(textSpanToAddLast);
                for (Interval interval : outerIntervalsPosterior) {
                    TextSpan outerSpan = new TextSpan(interval, textSpanToAddLast.properties());
                    textSpansToAdd.add(outerSpan);
                }
                modified = true;
                textSpanItemIterator = mInternalSet.iterator(); // reset iterator to the beginning to prevent concurrent modification at next()
            }
        }
        modified = modified || mInternalSet.addAll(textSpansToAdd);
        return modified;
    }

    /// Currently, same code of AbstractCollection.
    /// Being consistent by implementing addAll() via add().
    @Override
    public boolean addAll(Collection<? extends TextSpan> c) {
        boolean modified = false;
        for (TextSpan e : c)
            if (add(e))
                modified = true;
        return modified;
    }

    @Override
    public TextSpan lower(TextSpan textSpan) {
        return mInternalSet.lower(textSpan);
    }

    @Override
    public TextSpan floor(TextSpan textSpan) {
        return mInternalSet.floor(textSpan);
    }

    @Override
    public TextSpan ceiling(TextSpan textSpan) {
        return mInternalSet.ceiling(textSpan);
    }

    @Override
    public TextSpan higher(TextSpan textSpan) {
        return mInternalSet.higher(textSpan);
    }

    @Override
    public TextSpan pollFirst() {
        return mInternalSet.pollFirst();
    }

    @Override
    public TextSpan pollLast() {
        return mInternalSet.pollLast();
    }

    /// TODO: define custom iterator type to refine add method

    /**
     * Wrong implementation. Do not use.
     */
    @Deprecated
    @Override
    public Iterator<TextSpan> iterator() {
        return mInternalSet.iterator();
    }

    @NonNull
    @Override
    public TextSpanSet descendingSet() {
        return new TextSpanSet(mInternalSet.descendingSet());
    }

    /// TODO: define custom iterator type to refine add method

    /**
     * Wrong implementation. Do not use.
     */
    @Deprecated
    @NonNull
    @Override
    public Iterator<TextSpan> descendingIterator() {
        return mInternalSet.descendingIterator();
    }

    @NonNull
    @Override
    public TextSpanSet subSet(TextSpan fromElement, boolean fromInclusive, TextSpan toElement, boolean toInclusive) {
        return new TextSpanSet(mInternalSet.subSet(fromElement, fromInclusive, toElement, toInclusive));
    }

    @NonNull
    @Override
    public TextSpanSet headSet(TextSpan toElement, boolean inclusive) {
        return new TextSpanSet(mInternalSet.headSet(toElement, inclusive));
    }

    @NonNull
    @Override
    public TextSpanSet tailSet(TextSpan fromElement, boolean inclusive) {
        return new TextSpanSet(mInternalSet.tailSet(fromElement, inclusive));
    }

    @Override
    public int size() {
        return mInternalSet.size();
    }

    @Override
    public Comparator<? super TextSpan> comparator() {
        return mInternalSet.comparator();
    }

    @NonNull
    @Override
    public TextSpanSet subSet(TextSpan fromElement, TextSpan toElement) {
        return new TextSpanSet(mInternalSet.subSet(fromElement, toElement));
    }

    @NonNull
    @Override
    public TextSpanSet headSet(TextSpan toElement) {
        return new TextSpanSet(mInternalSet.headSet(toElement));
    }

    @NonNull
    @Override
    public TextSpanSet tailSet(TextSpan fromElement) {
        return new TextSpanSet(mInternalSet.tailSet(fromElement));
    }

    @Override
    public TextSpan first() {
        return mInternalSet.first();
    }

    @Override
    public TextSpan last() {
        return mInternalSet.last();
    }

    @Override
    public String toString() {
        return mInternalSet.toString();
    }

    public TextSpanSet filter(Predicate<TextSpan> p) {
        TextSpanSet result = new TextSpanSet();
        for (TextSpan textSpan : this) {
            if (p.apply(textSpan)) {
                result.add(textSpan);
            }
        }
        return result;
    }
}
