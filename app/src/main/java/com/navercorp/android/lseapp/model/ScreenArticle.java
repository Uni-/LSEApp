package com.navercorp.android.lseapp.model;

/**
 * Created by NAVER on 2017-07-21.
 *
 * Screen article have single Document instance member.
 */

public class ScreenArticle implements Article {

    private Document mDocument;

    @Override
    public ArticleType type() {
        return ArticleType.SCREEN;
    }
}
