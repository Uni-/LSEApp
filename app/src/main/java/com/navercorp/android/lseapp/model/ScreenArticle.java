package com.navercorp.android.lseapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NAVER on 2017-07-21.
 *
 * Screen article have single Document instance member.
 */

public class ScreenArticle implements Article {


    private final List<DocumentComponentValue> mComponentsList;

    public ScreenArticle() {
        mComponentsList = new ArrayList<>();
    }

    @Override
    public ArticleType type() {
        return ArticleType.SCREEN;
    }

    public List<DocumentComponentValue> getComponentsList() {
        return mComponentsList;
    }
}
