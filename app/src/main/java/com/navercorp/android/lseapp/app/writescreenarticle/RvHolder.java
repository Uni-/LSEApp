package com.navercorp.android.lseapp.app.writescreenarticle;

import android.support.v7.widget.RecyclerView;

import com.navercorp.android.lseapp.widget.DocumentComponentView;

/**
 * Created by NAVER on 2017-08-04.
 */
public final class RvHolder extends RecyclerView.ViewHolder {

    private DocumentComponentView mView;

    RvHolder(DocumentComponentView view) {
        super(view.getView());
        mView = view; // also referenced via super.itemView
    }

    private void setView(DocumentComponentView view) {
        mView = view;
    }

    public DocumentComponentView getView() {
        return mView;
    }
}
