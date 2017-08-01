package com.navercorp.android.lseapp.app.writescreenarticle;

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
import android.view.ViewTreeObserver;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentComponentValueFactory;
import com.navercorp.android.lseapp.model.DocumentTextValue;
import com.navercorp.android.lseapp.model.ScreenArticle;
import com.navercorp.android.lseapp.util.Selection;
import com.navercorp.android.lseapp.util.ListChangeType;
import com.navercorp.android.lseapp.widget.DocumentComponentView;
import com.navercorp.android.lseapp.widget.DocumentComponentViewFactory;
import com.navercorp.android.lseapp.widget.DocumentTextComponentView;
import com.navercorp.android.lseapp.widget.DocumentTitleComponentView;
import com.navercorp.android.lseapp.widget.WindowBottomBarView;

import java.util.List;

public class WriteScreenArticleActivity
        extends AppCompatActivity
        implements
        WriteScreenArticleContract.View,
        DocumentComponentView.OnEnterKeyListener,
        DocumentComponentView.OnContentFocusChangeListener,
        DocumentComponentView.OnInsertComponentListener,
        DocumentTextComponentView.OnContentSelectionChangeListener,
        WindowBottomBarView.ActionListener {

    private ScreenArticle mArticle;

    private WriteScreenArticleContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private RvAdapter mRvAdapter;
    private RvLayoutManager mRvLayoutManager;

    private WindowBottomBarView mBottomBarView;

    @Override // AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_screen_article);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_write_screen_article_recyclerview);
        mBottomBarView = (WindowBottomBarView) findViewById(R.id.activity_write_screen_article_bottombarview);

        mRvAdapter = new RvAdapter(WriteScreenArticleActivity.this);
        mRvLayoutManager = new RvLayoutManager(WriteScreenArticleActivity.this);
        mRecyclerView.setAdapter(mRvAdapter);
        mRecyclerView.setLayoutManager(mRvLayoutManager);

        mBottomBarView.setActionListener(this);

        mPresenter = new WriteScreenArticlePresenter(WriteScreenArticleActivity.this);
        mPresenter.start();

        mRvAdapter.addItem(0, DocumentComponentType.TITLE, true);
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
    public void setArticle(ScreenArticle article) {
        mArticle = article;
        valuesChanged(true);
    }

    @Override // WriteScreenArticleContract.View
    public ScreenArticle getArticle() {
        return mArticle;
    }

    @Override // DocumentComponentView.OnEnterKeyListener
    public void onEnterKey(DocumentComponentView v) {
        // Enter key pressed in title view.
        // create (if not exists) the 1st text view and focus to it.
        final boolean create = createIfNotExistsFirstText();

        requestItemViewContentFocusAt(1, !create);
    }

    @Override // DocumentComponentView.OnContentFocusChangeListener
    public void onContentFocusChange(DocumentComponentView v, boolean hasFocus) {
        final int index = mRvLayoutManager.indexOf(v.getView());
        if (index == -1) {
            return;
        }

        if (!hasFocus) {
            mRvAdapter.replaceItem(index, v.getValue(), false);
        }

        mBottomBarView.updateButtons(v.getValue(), new Selection());

        afterSetFocus(index, hasFocus);
    }

    @Override // DocumentComponentView.OnInsertComponentListener
    public void onInsertComponent(DocumentComponentView v, DocumentComponentType componentType) {
        final int index = mRvLayoutManager.indexOf(v.getView());
        if (index == -1) {
            return;
        }

        mRvAdapter.addItem(index + 1, componentType, true);

        requestItemViewContentFocusAt(index + 1, false);
    }

    @Override // DocumentTextComponentView.OnContentSelectionChangeListener
    public void onContentSelectionChange(DocumentTextComponentView v, Selection selection) {
        mBottomBarView.updateButtons(v.getValue(), selection);
    }

    @Override // WindowBottomBarView.ActionListener
    public void onRemoveComponent() {
        int index = mRvLayoutManager.indexOf(mRvLayoutManager.getFocusedChild());
        if (index == -1) {
            return;
        }

        mRvAdapter.removeItem(index, true);
    }

    private void valuesChanged() {
        mRvAdapter.notifyDataSetChanged();
        mRvLayoutManager.onItemsChanged(mRecyclerView);
    }

    private void valuesChanged(boolean notifyStrictOrLazy) {
        if (notifyStrictOrLazy) {
            valuesChanged();
        } else {
            mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    valuesChanged();
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    private void valuesChanged(final int index, final ListChangeType change) {
        switch (change) {
            case INSERT: {
                mRvAdapter.notifyItemInserted(index);
                mRvLayoutManager.onItemsAdded(mRecyclerView, index, 1);
                break;
            }
            case REMOVE: {
                mRvAdapter.notifyItemRemoved(index);
                mRvLayoutManager.onItemsRemoved(mRecyclerView, index, 1);
                break;
            }
            case REPLACE: {
                mRvAdapter.notifyItemChanged(index);
                mRvLayoutManager.onItemsUpdated(mRecyclerView, index, 1);
            }
            default:
        }
    }

    private void valuesChanged(final int index, final ListChangeType change, boolean notifyStrictOrLazy) {
        if (notifyStrictOrLazy) {
            valuesChanged(index, change);
        } else {
            mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    valuesChanged(index, change);
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    public boolean createIfNotExistsFirstText() {
        if (mRvAdapter.getItemCount() > 1) {
            if (mRvAdapter.getItem(1).componentType() != DocumentComponentType.TEXT) {
                mRvAdapter.addItem(1, DocumentComponentType.TEXT, true);
                return true;
            } else {
                return false;
            }
        } else {
            mRvAdapter.addItem(1, DocumentComponentType.TEXT, true); // append
            return true;
        }
    }

    private void afterSetFocus(int index, boolean focusGotOrLost) {
        // got focus: true, lost focus: false

        boolean remove = false;

        // when lost focus,
        // if the view is text and empty: remove it.
        if (!focusGotOrLost) {
            final DocumentComponentValue value = mRvAdapter.getItem(index);
            if (value.componentType() == DocumentComponentType.TEXT) {
                final DocumentTextValue textValue = (DocumentTextValue) value;
                if (textValue.isEmpty()) {
                    mRvAdapter.removeItem(index, true);
                    remove = true;
                }
            }
        }

        if (remove) {
            setItemViewComponentAdderVisibilityAt(index + 1, focusGotOrLost);
        } else {
            setItemViewComponentAdderVisibilityAt(index, focusGotOrLost);
            if (index > 0) {
                setItemViewComponentAdderVisibilityAt(index - 1, focusGotOrLost);
            }
        }
    }

    private void requestItemViewContentFocusAt(final int position, final boolean requestStrictOrLazy) {
        if (requestStrictOrLazy) {
            requestItemViewContentFocusAt(position);
        } else {
            mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    requestItemViewContentFocusAt(position);
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    private void requestItemViewContentFocusAt(final int position) {
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

    public List<DocumentComponentValue> getDocumentElementsList() {
        return mArticle.getDocument().getElementsList();
    }

    private static class RvHolder extends RecyclerView.ViewHolder {

        private DocumentComponentView mView;

        private RvHolder(DocumentComponentView view) {
            super(view.getView());
            mView = view; // also referenced via super.itemView
        }

        private void setView(DocumentComponentView view) {
            mView = view;
        }

        private DocumentComponentView getView() {
            return mView;
        }
    }

    private static class RvAdapter extends RecyclerView.Adapter<RvHolder> {

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
            final List<DocumentComponentValue> list = mActivity.getDocumentElementsList();
            final DocumentComponentValue value = list.get(position);

            holder.getView().setValue(value);
        }

        @Override
        public int getItemCount() {
            final List<DocumentComponentValue> list = mActivity.getDocumentElementsList();

            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            final List<DocumentComponentValue> list = mActivity.getDocumentElementsList();
            final DocumentComponentValue value = list.get(position);

            return value.componentType().ordinal();
        }

        public void addItem(int index, DocumentComponentValue value, boolean notifyStrictOrLazy) {
            final List<DocumentComponentValue> list = mActivity.getDocumentElementsList();

            list.add(index, value);

            mActivity.valuesChanged(index, ListChangeType.INSERT, notifyStrictOrLazy);
        }

        public void addItem(int index, DocumentComponentType type, boolean notifyStrictOrLazy) {
            addItem(index, mItemValueFactory.create(type), notifyStrictOrLazy);
        }

        public void removeItem(int index, boolean notifyStrictOrLazy) {
            final List<DocumentComponentValue> list = mActivity.getDocumentElementsList();

            list.remove(index);

            mActivity.valuesChanged(index, ListChangeType.REMOVE, notifyStrictOrLazy);
        }

        public void replaceItem(int index, DocumentComponentValue value, boolean notifyStrictOrLazy) {
            final List<DocumentComponentValue> list = mActivity.getDocumentElementsList();

            list.set(index, value);

            mActivity.valuesChanged(index, ListChangeType.REPLACE, notifyStrictOrLazy);
        }

        public DocumentComponentValue getItem(int index) {
            final List<DocumentComponentValue> list = mActivity.getDocumentElementsList();

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

    private static class RvLayoutManager extends LinearLayoutManager {

        public RvLayoutManager(Context context) {
            super(context);
        }

        public int indexOf(View view) {
            for (int i = 0; i < getItemCount(); i++) {
                if (view.equals(findViewByPosition(i))) {
                    return i;
                }
            }
            return -1;
        }
    }
}
