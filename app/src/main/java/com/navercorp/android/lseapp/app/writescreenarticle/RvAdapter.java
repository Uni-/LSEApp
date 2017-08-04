package com.navercorp.android.lseapp.app.writescreenarticle;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentComponentValueFactory;
import com.navercorp.android.lseapp.util.ListChange;
import com.navercorp.android.lseapp.widget.DocumentComponentView;
import com.navercorp.android.lseapp.widget.DocumentComponentViewFactory;
import com.navercorp.android.lseapp.widget.DocumentTextComponentView;
import com.navercorp.android.lseapp.widget.DocumentTitleComponentView;

import java.util.List;

/**
 * Created by NAVER on 2017-08-04.
 */
public class RvAdapter extends RecyclerView.Adapter<RvHolder> {

    private final WriteScreenArticleActivity mActivity;
    private final DocumentComponentViewFactory mItemViewFactory;
    private final DocumentComponentValueFactory mItemValueFactory;

    public RvAdapter(WriteScreenArticleActivity activity) {
        mActivity = activity;
        mItemViewFactory = new DocumentComponentViewFactory(activity);
        mItemValueFactory = new DocumentComponentValueFactory();
    }

    @Override
    public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final DocumentComponentType type = DocumentComponentType.values()[viewType];
        final DocumentComponentView view = mItemViewFactory.get(type);

        switch (type) {
            case TITLE: {
                DocumentTitleComponentView titleView = (DocumentTitleComponentView) view;
                titleView.setOnEnterKeyListener(mActivity);
                titleView.setOnContentFocusChangeListener(mActivity);
                titleView.setOnInsertComponentListener(mActivity);
                break;
            }
            case TEXT: {
                DocumentTextComponentView textView = (DocumentTextComponentView) view;
                textView.setOnContentFocusChangeListener(mActivity);
                textView.setOnContentSelectionChangeListener(mActivity);
                textView.setOnInsertComponentListener(mActivity);
                break;
            }
            default:
        }

        return new RvHolder(view);
    }

    @Override
    public void onBindViewHolder(RvHolder holder, int position) {
        final List<DocumentComponentValue> list = mActivity.getComponentsList();
        final DocumentComponentValue value = list.get(position);
        final DocumentComponentView view = holder.getView();

        view.setValue(value);
    }

    @Override
    public int getItemCount() {
        final List<DocumentComponentValue> list = mActivity.getComponentsList();

        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        final List<DocumentComponentValue> list = mActivity.getComponentsList();
        final DocumentComponentValue value = list.get(position);

        return value.componentType().ordinal();
    }

    public void addItem(int index, DocumentComponentValue value, boolean notifyStrictOrLazy) {
        final List<DocumentComponentValue> list = mActivity.getComponentsList();

        list.add(index, value);

        mActivity.valuesChanged(new ListChange.Insert(index), notifyStrictOrLazy);
    }

    public void addItem(int index, DocumentComponentType type, boolean notifyStrictOrLazy) {
        addItem(index, mItemValueFactory.create(type), notifyStrictOrLazy);
    }

    public void removeItem(int index, boolean notifyStrictOrLazy) {
        final List<DocumentComponentValue> list = mActivity.getComponentsList();

        list.remove(index);

        mActivity.valuesChanged(new ListChange.Delete(index), notifyStrictOrLazy);
    }

    public void replaceItem(int index, DocumentComponentValue value, boolean notifyStrictOrLazy) {
        final List<DocumentComponentValue> list = mActivity.getComponentsList();

        list.set(index, value);

        mActivity.valuesChanged(new ListChange.Replace(index), notifyStrictOrLazy);
    }

    public void moveItem(int fromIndex, int toIndex, boolean notifyStrictOrLazy) {
        final List<DocumentComponentValue> list = mActivity.getComponentsList();

        DocumentComponentValue item = list.get(fromIndex);
        list.remove(fromIndex);
        list.add(toIndex, item);

        mActivity.valuesChanged(new ListChange.Move(fromIndex, toIndex), notifyStrictOrLazy);
    }

    public DocumentComponentValue getItem(int index) {
        final List<DocumentComponentValue> list = mActivity.getComponentsList();

        return list.get(index);
    }

    @Override
    public void onViewRecycled(RvHolder holder) {
        super.onViewRecycled(holder);

        final int index = holder.getAdapterPosition();
        final DocumentComponentView view = holder.getView();

        if (index != -1) {
            // usage of replaceItem(notifyStrictOrLazy=false),
            // because notify() function series must not be called in scroll of RecyclerView
            // and inner focus movement makes RecyclerView scroll
            replaceItem(index, view.getValue(), false);
            view.setComponentAdderVisibility(false);
        }
    }
}
