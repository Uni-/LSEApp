package com.navercorp.android.lseapp.app.writescreenarticle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentTextValue;
import com.navercorp.android.lseapp.model.ScreenArticle;
import com.navercorp.android.lseapp.model.TextProperty;
import com.navercorp.android.lseapp.util.Interval;
import com.navercorp.android.lseapp.util.ListChange;
import com.navercorp.android.lseapp.widget.DocumentComponentView;
import com.navercorp.android.lseapp.widget.DocumentTextComponentView;
import com.navercorp.android.lseapp.widget.WindowBottomBarView;

import java.util.List;

public final class WriteScreenArticleActivity
        extends AppCompatActivity
        implements
        WriteScreenArticleContract.View,
        DocumentComponentView.OnEnterKeyListener,
        DocumentComponentView.OnContentFocusChangeListener,
        DocumentComponentView.OnInsertComponentListener,
        DocumentTextComponentView.OnContentSelectionChangeListener,
        WindowBottomBarView.ActionListener {

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
        RvItemTouchHelperCallback callback = new RvItemTouchHelperCallback(mRvAdapter);
        new ItemTouchHelper(callback).attachToRecyclerView(mRecyclerView);

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

        mBottomBarView.updateButtons(v.getValue(), new Interval());

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
    public void onContentSelectionChange(DocumentTextComponentView v, Interval interval) {
        mBottomBarView.updateButtons(v.getValue(), interval);
    }

    @Override // WindowBottomBarView.ActionListener
    public void onTextPropertyChange(TextProperty textProperty, Object o) {
        DocumentTextComponentView textView = (DocumentTextComponentView) mRvLayoutManager.getFocusedChild();
        if (textView == null) {
            return;
        }

        textView.setSelectedTextProperty(textProperty, o);
    }

    @Override // WindowBottomBarView.ActionListener
    public void onRemoveComponent() {
        int index = mRvLayoutManager.indexOf(mRvLayoutManager.getFocusedChild());
        if (index == -1) {
            return;
        }

        mRvAdapter.removeItem(index, true);
    }

    private ScreenArticle getArticle() {
        return mPresenter.getArticle();
    }

    private void valuesChanged() {
        mRvAdapter.notifyDataSetChanged();
        mRvLayoutManager.onItemsChanged(mRecyclerView);
    }

    public void valuesChanged(boolean notifyStrictOrLazy) {
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

    private void valuesChanged(final ListChange change) {
        if (change instanceof ListChange.Insert) {
            int index = ((ListChange.Insert) change).index;
            mRvAdapter.notifyItemInserted(index);
            mRvLayoutManager.onItemsAdded(mRecyclerView, index, 1);
        } else if (change instanceof ListChange.Delete) {
            int index = ((ListChange.Delete) change).index;
            mRvAdapter.notifyItemRemoved(index);
            mRvLayoutManager.onItemsRemoved(mRecyclerView, index, 1);
        } else if (change instanceof ListChange.Replace) {
            int index = ((ListChange.Replace) change).index;
            mRvAdapter.notifyItemChanged(index);
            mRvLayoutManager.onItemsUpdated(mRecyclerView, index, 1);
        } else if (change instanceof ListChange.Move) {
            int fromIndex = ((ListChange.Move) change).fromIndex;
            int toIndex = ((ListChange.Move) change).toIndex;
            mRvAdapter.notifyItemMoved(fromIndex, toIndex);
            mRvLayoutManager.onItemsMoved(mRecyclerView, fromIndex, toIndex, 1);
        }
    }

    public void valuesChanged(final ListChange change, boolean notifyStrictOrLazy) {
        if (notifyStrictOrLazy) {
            valuesChanged(change);
        } else {
            mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    valuesChanged(change);
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

        // when lost focus,
        // if the view is text and empty: remove it.
        if (!focusGotOrLost) {
            final DocumentComponentValue value = mRvAdapter.getItem(index);
            if (value.componentType() == DocumentComponentType.TEXT) {
                final DocumentTextValue textValue = (DocumentTextValue) value;
                if (textValue.isEmpty()) {
                    mRvAdapter.removeItem(index, true);
                }
            }
        }

        setItemViewComponentAdderVisibilityAt(index, focusGotOrLost);
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

    public List<DocumentComponentValue> getComponentsList() {
        return getArticle().getComponentsList();
    }

}
