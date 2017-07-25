package com.navercorp.android.lseapp.model;

import java.util.ArrayList;

/**
 * Created by NAVER on 2017-07-21.
 */

public class Document {

    private final ArrayList<DocumentComponentValue> mElementsList;

    public Document() {
        mElementsList = new ArrayList<>();
    }

    public ArrayList<DocumentComponentValue> getElementsList() {
        return mElementsList;
    }
}
