package com.navercorp.android.lseapp.app.writescreenarticle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.app.selectsavedarticles.SelectSavedArticleActivity;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.model.DocumentImageStripValue;
import com.navercorp.android.lseapp.model.DocumentTextValue;
import com.navercorp.android.lseapp.model.DocumentTitleValue;
import com.navercorp.android.lseapp.model.TextProperty;
import com.navercorp.android.lseapp.util.Interval;
import com.navercorp.android.lseapp.util.ListChange;
import com.navercorp.android.lseapp.util.NotifyPolicy;
import com.navercorp.android.lseapp.widget.DocumentComponentView;
import com.navercorp.android.lseapp.widget.DocumentImageStripComponentView;
import com.navercorp.android.lseapp.widget.DocumentTextComponentView;
import com.navercorp.android.lseapp.widget.DocumentTitleComponentView;
import com.navercorp.android.lseapp.widget.FooterControlPanelView;

public final class WriteScreenArticleActivity
        extends AppCompatActivity
        implements
        WriteScreenArticleContract.View,
        DocumentComponentView.OnEnterKeyListener,
        DocumentComponentView.OnContentFocusChangeListener,
        DocumentComponentView.OnInsertComponentListener,
        DocumentTextComponentView.OnContentSelectionChangeListener,
        FooterControlPanelView.ActionHandler {

    private WriteScreenArticleContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private RvAdapter mRvAdapter;
    private RvLayoutManager mRvLayoutManager;

    private FooterControlPanelView mFooterControlPanelView;

    private OnSelectImageHandler mOnSelectImageHandler;

    private static final int REQUEST_SELECT_IMAGE = 5557;
    private static final int REQUEST_SELECT_SAVED_ARTICLE = 5558;

    @Override // AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_screen_article);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_write_screen_article_recyclerview);
        mFooterControlPanelView = (FooterControlPanelView) findViewById(R.id.activity_write_screen_article_bottombarview);

        mRvAdapter = new RvAdapter(WriteScreenArticleActivity.this);
        mRvLayoutManager = new RvLayoutManager(WriteScreenArticleActivity.this);
        mRecyclerView.setAdapter(mRvAdapter);
        mRecyclerView.setLayoutManager(mRvLayoutManager);
        RvItemTouchHelperCallback callback = new RvItemTouchHelperCallback(mRvAdapter);
        new ItemTouchHelper(callback).attachToRecyclerView(mRecyclerView);

        mFooterControlPanelView.setActionListener(this);

        mPresenter = new WriteScreenArticlePresenter(WriteScreenArticleActivity.this);
        mPresenter.start();

        mRvAdapter.addItem(0, new DocumentTitleValue(), NotifyPolicy.STRICT);
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
                Intent intent = new Intent(WriteScreenArticleActivity.this, SelectSavedArticleActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_SAVED_ARTICLE);
                return true;
            }
            case R.id.menu_main_save: {
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override // AppCompatActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_IMAGE: {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    mOnSelectImageHandler.onSelectImageOk(data.getDataString());
                } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                    mOnSelectImageHandler.onSelectImageCancel();
                }
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
    }

    @Override // WriteScreenArticleContract.View
    public WriteScreenArticleContract.Presenter getPresenter() {
        return mPresenter;
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
            // usage of NotifyPolicy.LAZY
            mRvAdapter.replaceItem(index, v.getValue(), NotifyPolicy.LAZY);

//            mFooterControlPanelView.updateButtons(null, null);
        } else {
            DocumentComponentValue value = v.getValue();
            DocumentComponentView nextView = ((DocumentComponentView) mRvLayoutManager.findViewByPosition(index + 1));
            DocumentComponentValue nextValue = nextView != null ? nextView.getValue() : null;
            mFooterControlPanelView.updateButtonsVisibility(value, nextValue);
        }

        afterSetFocus(index, hasFocus);
    }

    @Override // DocumentComponentView.OnInsertComponentListener
    public void onInsertComponent(DocumentComponentView v, DocumentComponentType componentType) {
        final int index = mRvLayoutManager.indexOf(v.getView());
        if (index == -1) {
            return;
        }

        switch (componentType) {
            case TEXT: {
                DocumentTextValue textValue = new DocumentTextValue();
                mRvAdapter.addItem(index + 1, textValue, NotifyPolicy.STRICT);
                break;
            }
            case IMAGE: {
                startSelectImage(new OnSelectImageHandler() {
                    @Override
                    public void onSelectImageOk(String path) {
                        DocumentImageStripValue imageValue = new DocumentImageStripValue(path);
                        mRvAdapter.addItem(index + 1, imageValue, NotifyPolicy.STRICT);
                    }

                    @Override
                    public void onSelectImageCancel() {
                        // do nothing
                    }
                });
                break;
            }
            case MAP: {
                // TODO
            }
            default:
        }

        requestItemViewContentFocusAt(index + 1, false);
    }

    @Override // DocumentTextComponentView.OnContentSelectionChangeListener
    public void onContentSelectionChange(DocumentTextComponentView v, Interval interval) {
        DocumentComponentValue value = v.getValue();
        mFooterControlPanelView.updateButtonsValue(value, interval);
    }

    @Override // WindowBottomBarView.ActionHandler
    public void onTitleBackgroundSelect() {
        startSelectImage(new OnSelectImageHandler() {
            @Override
            public void onSelectImageOk(String path) {
                DocumentTitleValue titleValueBefore = (DocumentTitleValue) mRvAdapter.getItem(0);
                DocumentTitleValue titleValue = new DocumentTitleValue(titleValueBefore.getText(), path);
                DocumentTitleComponentView titleView = (DocumentTitleComponentView) mRvLayoutManager.findViewByPosition(0);
                titleView.setValue(titleValue);
                mRvAdapter.replaceItem(0, titleValue, NotifyPolicy.STRICT);
            }

            @Override
            public void onSelectImageCancel() {
                // do nothing
            }
        });
    }

    @Override
    public void onTitleBackgroundRemove() {
        DocumentTitleValue titleValueBefore = (DocumentTitleValue) mRvAdapter.getItem(0);
        DocumentTitleValue titleValue = new DocumentTitleValue(titleValueBefore.getText());
        DocumentTitleComponentView titleView = (DocumentTitleComponentView) mRvLayoutManager.findViewByPosition(0);
        titleView.setValue(titleValue);
        mRvAdapter.replaceItem(0, titleValue, NotifyPolicy.STRICT);
    }

    @Override // WindowBottomBarView.ActionHandler
    public void onTextPropertyChange(TextProperty textProperty, Object o) {
        DocumentTextComponentView textView = (DocumentTextComponentView) mRvLayoutManager.getFocusedChild();
        if (textView == null) {
            return;
        }

        textView.setSelectedTextProperty(textProperty, o);
    }

    @Override // WindowBottomBarView.ActionHandler
    public void onRemoveComponent() {
        View view = mRvLayoutManager.getFocusedChild();
        if (view == null) {
            return;
        }
        int index = mRvLayoutManager.indexOf(view);
        if (index == -1) {
            return;
        }

        mRvAdapter.removeItem(index, NotifyPolicy.STRICT);
    }

    @Override // WindowBottomBarView.ActionHandler
    public void onImageStripMerge() {
        DocumentImageStripComponentView view = (DocumentImageStripComponentView) mRvLayoutManager.getFocusedChild();
        int index = mRvLayoutManager.indexOf(view.getView());
        DocumentImageStripComponentView nextView = (DocumentImageStripComponentView) mRvLayoutManager.findViewByPosition(index + 1);
        if (nextView == null) {
            return; // TODO: FIXIT: should be unreachable, but is reachable
        }
        DocumentImageStripValue value = view.getValue();
        DocumentImageStripValue nextValue = nextView.getValue();
        if (value.count() == 1) {
            view.setValue(new DocumentImageStripValue(value.getImagePath0(), nextValue.getImagePath0()));
            mRvAdapter.removeItem(index + 1, NotifyPolicy.STRICT);
        } else if (value.count() == 2) {
            view.setValue(new DocumentImageStripValue(value.getImagePath0(), value.getImagePath1(), nextValue.getImagePath0()));
            mRvAdapter.removeItem(index + 1, NotifyPolicy.STRICT);
        }
    }

    @Override // WindowBottomBarView.ActionHandler
    public void onImageStripSplit() {
        DocumentImageStripComponentView view = (DocumentImageStripComponentView) mRvLayoutManager.getFocusedChild();
        int index = mRvLayoutManager.indexOf(view.getView());
        DocumentImageStripValue value = view.getValue();
        if (value.count() == 3) {
            view.setValue(new DocumentImageStripValue(value.getImagePath0(), value.getImagePath1()));
            mRvAdapter.addItem(index + 1, new DocumentImageStripValue(value.getImagePath2()), NotifyPolicy.STRICT);
        } else if (value.count() == 2) {
            view.setValue(new DocumentImageStripValue(value.getImagePath0()));
            mRvAdapter.addItem(index + 1, new DocumentImageStripValue(value.getImagePath1()), NotifyPolicy.STRICT);
        }
    }

    private void valuesChanged() {
        mRvAdapter.notifyDataSetChanged();
    }

    public void valuesChanged(boolean notifyStrictOrLazy) {
        if (notifyStrictOrLazy) {
            valuesChanged();
        } else {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    valuesChanged();
                }
            });
        }
    }

    private void valuesChanged(final ListChange change) {
        if (change instanceof ListChange.Insert) {
            int index = ((ListChange.Insert) change).index;
            mRvAdapter.notifyItemInserted(index);
        } else if (change instanceof ListChange.Delete) {
            int index = ((ListChange.Delete) change).index;
            mRvAdapter.notifyItemRemoved(index);
        } else if (change instanceof ListChange.Replace) {
            int index = ((ListChange.Replace) change).index;
            mRvAdapter.notifyItemChanged(index);
        } else if (change instanceof ListChange.Move) {
            int fromIndex = ((ListChange.Move) change).fromIndex;
            int toIndex = ((ListChange.Move) change).toIndex;
            mRvAdapter.notifyItemMoved(fromIndex, toIndex);
        }
    }

    public void valuesChanged(final ListChange change, boolean notifyStrictOrLazy) {
        if (notifyStrictOrLazy) {
            valuesChanged(change);
        } else {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    valuesChanged(change);
                }
            });
        }
    }

    public void startSelectImage(OnSelectImageHandler onSelectImageHandler) {
        mOnSelectImageHandler = onSelectImageHandler;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

    private boolean createIfNotExistsFirstText() {
        if (mRvAdapter.getItemCount() > 1) {
            if (mRvAdapter.getItem(1).componentType() != DocumentComponentType.TEXT) {
                mRvAdapter.addItem(1, new DocumentTextValue(), NotifyPolicy.STRICT);
                return true;
            } else {
                return false;
            }
        } else {
            mRvAdapter.addItem(1, new DocumentTextValue(), NotifyPolicy.STRICT); // append
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
                    // usage of NotifyPolicy.LAZY
                    mRvAdapter.removeItem(index, NotifyPolicy.LAZY);
                }
            }
        }

        setItemViewComponentAdderVisibilityAt(index, focusGotOrLost);
    }

    private void requestItemViewContentFocusAt(final int position, final boolean requestStrictOrLazy) {
        if (requestStrictOrLazy) {
            requestItemViewContentFocusAt(position);
        } else {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    requestItemViewContentFocusAt(position);
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

    private interface OnSelectImageHandler {
        void onSelectImageOk(String path);

        void onSelectImageCancel();
    }

}
