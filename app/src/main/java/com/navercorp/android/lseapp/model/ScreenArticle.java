package com.navercorp.android.lseapp.model;

import com.navercorp.android.lseapp.util.Hash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by NAVER on 2017-07-21.
 * <p>
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

    public static ObjectValue[] toObjectValues(ScreenArticle article) {
        StringBuilder content = new StringBuilder();
        ObjectValue[] objectValues = new ObjectValue[article.mComponentsList.size() + 1];
        for (int i = 0; i < article.mComponentsList.size(); i++) {
            objectValues[i + 1] = article.mComponentsList.get(i).getDataObject();
            content.append(objectValues[i + 1].getSha1sum());
        }
        objectValues[0] = new ObjectValue(article.type().getGlobalTypeSerial(), content.toString().getBytes());
        return objectValues;
    }

    public static ScreenArticle fromObjectValues(String sha1sumKey, Map<String, ObjectValue> objectValues) {
        ScreenArticle article = new ScreenArticle();
        ObjectValue articleValue = objectValues.get(sha1sumKey);
        if (articleValue.getContentType() != ArticleType.SCREEN.getGlobalTypeSerial()) {
            throw new IllegalArgumentException();
        }
        final String list = new String(articleValue.getContentValue());
        for (int i = 0; i < list.length() / (Hash.SHA1SUM_LENGTH_IN_BYTES * 2); i++) {
            String key = list.substring(i * (Hash.SHA1SUM_LENGTH_IN_BYTES * 2), (i + 1) * (Hash.SHA1SUM_LENGTH_IN_BYTES * 2));
            ObjectValue componentObjectValue = objectValues.get(key);
            DocumentComponentValue value = DocumentComponentValueFactory.get(componentObjectValue);
            article.mComponentsList.add(value);
        }
        return article;
    }
}
