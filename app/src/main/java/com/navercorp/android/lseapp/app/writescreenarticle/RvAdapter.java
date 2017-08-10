package com.navercorp.android.lseapp.app.writescreenarticle;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.util.ListChange;
import com.navercorp.android.lseapp.util.NotifyPolicy;
import com.navercorp.android.lseapp.widget.DocumentComponentView;
import com.navercorp.android.lseapp.widget.DocumentComponentViewFactory;
import com.navercorp.android.lseapp.widget.DocumentImageStripComponentView;
import com.navercorp.android.lseapp.widget.DocumentMapComponentView;
import com.navercorp.android.lseapp.widget.DocumentTextComponentView;
import com.navercorp.android.lseapp.widget.DocumentTitleComponentView;

/**
 * Created by NAVER on 2017-08-04.
 */
public final class RvAdapter extends RecyclerView.Adapter<RvHolder> {

    private final WriteScreenArticleActivity mActivity;
    private final DocumentComponentViewFactory mItemViewFactory;

    public RvAdapter(WriteScreenArticleActivity activity) {
        mActivity = activity;
        mItemViewFactory = new DocumentComponentViewFactory(activity);
    }

    @Override
    public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final DocumentComponentType type = DocumentComponentType.values()[viewType];
        final DocumentComponentView view = mItemViewFactory.get(type);
        final RvHolder holder = new RvHolder(view);

        switch (type) {
            case TITLE: {
                final DocumentTitleComponentView titleView = (DocumentTitleComponentView) view;
                titleView.setOnEnterKeyListener(mActivity);
                titleView.setOnContentFocusChangeListener(mActivity);
                titleView.setOnInsertComponentListener(mActivity);
                break;
            }
            case TEXT: {
                final DocumentTextComponentView textView = (DocumentTextComponentView) view;
                textView.setOnContentFocusChangeListener(mActivity);
                textView.setOnContentSelectionChangeListener(mActivity);
                textView.setOnInsertComponentListener(mActivity);
                break;
            }
            case IMAGE: {
                final DocumentImageStripComponentView imageStripView = (DocumentImageStripComponentView) view;
                imageStripView.setOnContentFocusChangeListener(mActivity);
                imageStripView.setOnInsertComponentListener(mActivity);
                break;
            }
            case MAP: {
                final DocumentMapComponentView mapView = (DocumentMapComponentView) view;
                mapView.setOnContentFocusChangeListener(mActivity);
                mapView.setOnInsertComponentListener(mActivity);
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
            // usage of NotifyPolicy.NEVER
            replaceItem(index, view.getValue(), NotifyPolicy.NEVER);
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

    protected DocumentComponentValue getItem(int index) {
        return mActivity.getPresenter().getComponentValueItem(index);
    }

    protected void addItem(int index, DocumentComponentValue value, NotifyPolicy notifyPolicy) {
        mActivity.getPresenter().addComponentValueItem(index, value);
        switch (notifyPolicy) {
            case STRICT:
                mActivity.valuesChanged(new ListChange.Insert(index), true);
                break;
            case LAZY:
                mActivity.valuesChanged(new ListChange.Insert(index), false);
                break;
            default:
        }
    }

    protected void removeItem(int index, NotifyPolicy notifyPolicy) {
        mActivity.getPresenter().removeComponentValueItem(index);
        switch (notifyPolicy) {
            case STRICT:
                mActivity.valuesChanged(new ListChange.Delete(index), true);
                break;
            case LAZY:
                mActivity.valuesChanged(new ListChange.Delete(index), false);
                break;
            default:
        }
    }

    protected void replaceItem(int index, DocumentComponentValue value, NotifyPolicy notifyPolicy) {
        mActivity.getPresenter().replaceComponentValueItem(index, value);
        switch (notifyPolicy) {
            case STRICT:
                mActivity.valuesChanged(new ListChange.Replace(index), true);
                break;
            case LAZY:
                mActivity.valuesChanged(new ListChange.Replace(index), false);
                break;
            default:
        }
    }

    protected void moveItem(int fromIndex, int toIndex, NotifyPolicy notifyPolicy) {
        mActivity.getPresenter().moveComponentValueItem(fromIndex, toIndex);
        switch (notifyPolicy) {
            case STRICT:
                mActivity.valuesChanged(new ListChange.Move(fromIndex, toIndex), true);
                break;
            case LAZY:
                mActivity.valuesChanged(new ListChange.Move(fromIndex, toIndex), false);
                break;
            default:
        }
    }
}
