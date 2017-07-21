package com.navercorp.android.lseapp.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;
import com.navercorp.android.lseapp.model.DocumentComponentValue;
import com.navercorp.android.lseapp.widget.DocumentComponentView;
import com.navercorp.android.lseapp.widget.DocumentComponentViewFactory;

import java.util.ArrayList;

public class WriteScreenArticleActivity extends AppCompatActivity implements WriteScreenArticleContract.View {

    private WriteScreenArticleContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private RvAdapter mRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new WriteScreenArticlePresenter(this);
        mPresenter.start();

        setContentView(R.layout.activity_write_screen_article);
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);

        mRvAdapter = new RvAdapter(mPresenter.getComponentValuesList());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRvAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.end();
        mPresenter = null;
    }

    private class RvViewHolder<ViewType extends DocumentComponentView>
            extends RecyclerView.ViewHolder {

        ViewType mV;

        protected RvViewHolder(ViewType itemView) {
            super(itemView.getView());
            mV = itemView;
        }
    }

    private class RvAdapter extends RecyclerView.Adapter<RvViewHolder> {

        private final ArrayList<DocumentComponentValue> mItems;
        private final DocumentComponentViewFactory mItemViewFactory;

        public RvAdapter(ArrayList<DocumentComponentValue> items) {
            super();
            mItems = items;
            mItemViewFactory = new DocumentComponentViewFactory(WriteScreenArticleActivity.this);
        }

        @Override
        public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            DocumentComponentView v = mItemViewFactory.get(DocumentComponentType.TEXT);
            return new RvViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RvViewHolder holder, int position) {
            DocumentComponentValue value = mItems.get(position);
            holder.mV.setData(value.getDataAsBytes());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    @Override
    public void applyDataSetChange() {
        mRvAdapter.notifyDataSetChanged();
    }
}
