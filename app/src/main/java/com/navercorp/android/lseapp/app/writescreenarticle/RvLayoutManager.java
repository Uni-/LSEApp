package com.navercorp.android.lseapp.app.writescreenarticle;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

/**
 * Created by NAVER on 2017-08-04.
 */
public final class RvLayoutManager extends LinearLayoutManager {

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
