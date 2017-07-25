package com.navercorp.android.lseapp.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.widget.DocumentComponentView;
import com.navercorp.android.lseapp.widget.DocumentComponentViewFactory;
import com.navercorp.android.lseapp.widget.DocumentTextComponentView;
import com.navercorp.android.lseapp.widget.DocumentTitleComponentView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class WriteScreenArticleActivity
        extends AppCompatActivity
        implements
        WriteScreenArticleContract.View,
        DocumentTitleComponentView.OnEnterKeyListener,
        DocumentComponentView.OnContentFocusChangeListener,
        DocumentComponentView.OnInsertComponentListener {

    private WriteScreenArticleContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private RvAdapter mRvAdapter;
    private RvLayoutManager mRvLayoutManager;

    @Override // AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new WriteScreenArticlePresenter(WriteScreenArticleActivity.this);
        mPresenter.start();

        setContentView(R.layout.activity_write_screen_article);

        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);

        mRvAdapter = new RvAdapter(WriteScreenArticleActivity.this);
        mRvLayoutManager = new RvLayoutManager(WriteScreenArticleActivity.this);
        mRecyclerView.setAdapter(mRvAdapter);
        mRecyclerView.setLayoutManager(mRvLayoutManager);
    }

    @Override // AppCompatActivity
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.end();
        mPresenter = null;
    }

    @Override // AppCompatActivity
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override // AppCompatActivity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_load: {
                return true;
            }
            case R.id.menu_main_save: {
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override // WriteScreenArticleContract.View
    public void notifyValuesChanged(int index) {
        if (mRvAdapter != null) {
//            mRvAdapter.notifyDataSetChanged();
            if (index > 0) {
                mRvAdapter.notifyItemInserted(index);
            } else {
                mRvAdapter.notifyItemRemoved(-index);
            }
        }
        if (mRvLayoutManager != null) {
            mRvLayoutManager.onItemsChanged(mRecyclerView);
        }
    }

    @Override // DocumentTitleComponentView.OnEnterKeyListener
    public void onEnterKey() {
        focusCreateFirstTextFromTitleEnter();
    }

    @Override // DocumentComponentView.OnContentFocusChangeListener
    public void onContentFocusChange(DocumentComponentView v, boolean hasFocus) {
        int index = mRvLayoutManager.indexOf(v.getView());

        afterSetFocus(index, hasFocus);
    }

    @Override
    public void onInsertComponent(DocumentComponentView v, DocumentComponentType componentType) {
        final int index = mRvLayoutManager.indexOf(v.getView());

        mPresenter.insertElementValue(index + 1, componentType);

        mRvLayoutManager.registerLayoutCompleteObserver(new RvLayoutManager.OnLayoutObserver() {
            @Override
            public void notifyOnLayout(RvLayoutManager rvLayoutManager) {
                for (int i = 0; i < mRvLayoutManager.getChildCount(); i++) {
                    setItemViewComponentAdderVisibilityAt(i, false);
                }
                requestItemViewContentFocusAt(index + 1);
                afterSetFocus(index + 1, true);
                rvLayoutManager.unregisterLayoutCompleteObserver(this);
            }
        });
    }

    private void focusCreateFirstTextFromTitleEnter() {
        final boolean create = mPresenter.createIfNotExistsFirstText();
        if (create) {
            mRvLayoutManager.registerLayoutCompleteObserver(new RvLayoutManager.OnLayoutObserver() {
                @Override
                public void notifyOnLayout(RvLayoutManager rvLayoutManager) {
                    focusToFirstFromZeroth();
                    rvLayoutManager.unregisterLayoutCompleteObserver(this);
                }
            });
        } else {
            focusToFirstFromZeroth();
        }
    }

    private void focusToFirstFromZeroth() {
        requestItemViewContentFocusAt(1);
        afterSetFocus(0, false);
        afterSetFocus(1, true);
    }

    private void afterSetFocus(int index, boolean gotOrLost) {
        // got focus: true, lost focus: false
        if (gotOrLost) {
            mPresenter.notifyGotFocus(index);
        }

        setItemViewComponentAdderVisibilityAt(index, gotOrLost);
        if (index > 0) {
            setItemViewComponentAdderVisibilityAt(index - 1, gotOrLost);
        }
    }

    public void requestItemViewContentFocusAt(int position) {
        DocumentComponentView v = (DocumentComponentView) mRvLayoutManager.findViewByPosition(position);
        if (v != null) {
            v.requestContentFocus();
        }
    }

    private void setItemViewComponentAdderVisibilityAt(int position, boolean visibility) {
        DocumentComponentView v = (DocumentComponentView) mRvLayoutManager.findViewByPosition(position);
        if (v != null) {
            v.setComponentAdderVisibility(visibility);
        }
    }

    private static class RvViewHolder extends RecyclerView.ViewHolder {

        DocumentComponentView mView;

        protected RvViewHolder(DocumentComponentView itemView) {
            super(itemView.getView());
            mView = itemView;
        }
    }

    private static class RvAdapter extends RecyclerView.Adapter<RvViewHolder> {

        private final WriteScreenArticleActivity mActivity;
        private final DocumentComponentViewFactory mItemViewFactory;

        public RvAdapter(WriteScreenArticleActivity activity) {
            super();
            mActivity = activity;
            mItemViewFactory = new DocumentComponentViewFactory(activity);
        }

        @Override
        public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            DocumentComponentType t = DocumentComponentType.values()[viewType];
            DocumentComponentView v = mItemViewFactory.get(t);

            switch (t) {
                case TITLE: {
                    DocumentTitleComponentView titleComponentView = (DocumentTitleComponentView) v;
                    titleComponentView.setOnEnterKeyListener(mActivity);
                    titleComponentView.setOnContentFocusChangeListener(mActivity);
                    titleComponentView.setOnInsertComponentListener(mActivity);
                    break;
                }
                case TEXT: {
                    DocumentTextComponentView textComponentView = (DocumentTextComponentView) v;
                    textComponentView.setOnContentFocusChangeListener(mActivity);
                    textComponentView.setOnInsertComponentListener(mActivity);
                    break;
                }
                default:
            }

            return new RvViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RvViewHolder holder, int position) {
            DocumentComponentValue value = getList().get(position);
            holder.mView.setValue(value);
        }

        public List<DocumentComponentValue> getList() {
            return mActivity.mPresenter.getComponentValuesList();
        }

        @Override
        public int getItemCount() {
            return getList().size();
        }

        @Override
        public int getItemViewType(int position) {
            return getList().get(position).componentType().ordinal();
        }

        @Override
        public void onViewRecycled(RvViewHolder holder) {
            super.onViewRecycled(holder);
        }
    }

    private static class RvLayoutManager extends LinearLayoutManager {

        private final HashSet<OnLayoutObserver> mOnLayoutObserverCollection;

        public RvLayoutManager(Context context) {
            super(context);
            mOnLayoutObserverCollection = new HashSet<>();
        }

        @Override
        public void onLayoutCompleted(RecyclerView.State state) {
            super.onLayoutCompleted(state);

            if (!state.isPreLayout()) {
                Iterator<OnLayoutObserver> onLayoutObserverIterator = mOnLayoutObserverCollection.iterator();
                while (onLayoutObserverIterator.hasNext()) {
                    // using `while` instead of `for`(foreach), to avoid concurrent modification error
                    OnLayoutObserver onLayoutObserver = onLayoutObserverIterator.next();
                    onLayoutObserver.notifyOnLayout(this); // some unregister(aka removals) occur here
                }
            }
        }

        public int indexOf(View v) {
            for (int i = 0; i < getItemCount(); i++) {
                if (v.equals(findViewByPosition(i))) {
                    return i;
                }
            }
            return -1;
        }

        public boolean registerLayoutCompleteObserver(OnLayoutObserver onLayoutObserver) {
            return mOnLayoutObserverCollection.add(onLayoutObserver);
        }

        public boolean unregisterLayoutCompleteObserver(OnLayoutObserver onLayoutObserver) {
            return mOnLayoutObserverCollection.remove(onLayoutObserver);
        }

        public void unregisterAllLayoutCompleteObservers() {
            mOnLayoutObserverCollection.clear();
        }

        private static abstract class OnLayoutObserver {
            public abstract void notifyOnLayout(RvLayoutManager rvLayoutManager);
        }
    }
}
