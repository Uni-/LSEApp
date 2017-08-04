package com.navercorp.android.lseapp.app.writescreenarticle;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by NAVER on 2017-08-04.
 */

public class RvItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private RvAdapter mRvAdapter;

    public RvItemTouchHelperCallback(RvAdapter rvAdapter) {
        mRvAdapter = rvAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getAdapterPosition() == 0 || target.getAdapterPosition() == 0) {
            return false;
        }

        mRvAdapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition(), true);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // do nothing
    }
}
