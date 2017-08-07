package com.navercorp.android.lseapp.app.writescreenarticle;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentComponentValueFactory;
import com.navercorp.android.lseapp.model.DocumentImageStripValue;
import com.navercorp.android.lseapp.util.ListChange;
import com.navercorp.android.lseapp.widget.DocumentComponentView;
import com.navercorp.android.lseapp.widget.DocumentComponentViewFactory;
import com.navercorp.android.lseapp.widget.DocumentImageStripComponentView;
import com.navercorp.android.lseapp.widget.DocumentTextComponentView;
import com.navercorp.android.lseapp.widget.DocumentTitleComponentView;

/**
 * Created by NAVER on 2017-08-04.
 */
public final class RvAdapter extends RecyclerView.Adapter<RvHolder> {

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
        final RvHolder holder = new RvHolder(view);

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
            case IMAGE: {
                final DocumentImageStripComponentView imageStripView = (DocumentImageStripComponentView) view;
                imageStripView.setOnContentFocusChangeListener(mActivity);
                imageStripView.setOnInsertComponentListener(mActivity);
                mActivity.startSelectImage(new WriteScreenArticleActivity.OnSelectImageHandler() {
                    @Override
                    public void onSelectImageOk(String path) {
                        imageStripView.setValue(new DocumentImageStripValue(path));
                    }

                    @Override
                    public void onSelectImageCancel() {
                        // TODO
                    }
                });
            }
            default:
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RvHolder holder, int position) {
        final DocumentComponentValue value = mActivity.getPresenter().getComponentValueItem(position);
        final DocumentComponentView view = holder.getView();

        view.setValue(value);
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

    @Override
    public int getItemCount() {
        return mActivity.getPresenter().getComponentValueItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).componentType().ordinal();
    }

    public DocumentComponentValue getItem(int index) {
        return mActivity.getPresenter().getComponentValueItem(index);
    }

    public void addItem(int index, DocumentComponentValue value, boolean notifyStrictOrLazy) {
        mActivity.getPresenter().addComponentValueItem(index, value);
        mActivity.valuesChanged(new ListChange.Insert(index), notifyStrictOrLazy);
    }

    public void addItem(int index, DocumentComponentType type, boolean notifyStrictOrLazy) {
        addItem(index, mItemValueFactory.create(type), notifyStrictOrLazy);
    }

    public void removeItem(int index, boolean notifyStrictOrLazy) {
        mActivity.getPresenter().removeComponentValueItem(index);
        mActivity.valuesChanged(new ListChange.Delete(index), notifyStrictOrLazy);
    }

    public void replaceItem(int index, DocumentComponentValue value, boolean notifyStrictOrLazy) {
        mActivity.getPresenter().replaceComponentValueItem(index, value);
        mActivity.valuesChanged(new ListChange.Replace(index), notifyStrictOrLazy);
    }

    public void moveItem(int fromIndex, int toIndex, boolean notifyStrictOrLazy) {
        mActivity.getPresenter().moveComponentValueItem(fromIndex, toIndex);
        mActivity.valuesChanged(new ListChange.Move(fromIndex, toIndex), notifyStrictOrLazy);
    }
}
