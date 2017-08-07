package com.navercorp.android.lseapp.data;

import com.navercorp.android.lseapp.model.Article;
import com.navercorp.android.lseapp.model.DocumentComponentValue;

/**
 * Created by NAVER on 2017-08-04.
 */

public interface Repository {

    void setNewScreenArticle();

    DocumentComponentValue getComponentValueItemOfScreenArticle(int index);

    int getComponentValueItemCountOfScreenArticle();

    void addComponentValueItemToScreenArticle(int index, DocumentComponentValue value);

    void removeComponentValueItemFromScreenArticle(int index);

    void replaceComponentValueItemOfScreenArticle(int index, DocumentComponentValue value);

    void moveComponentValueItemOfScreenArticle(int fromIndex, int toIndex);
}
