package com.navercorp.android.lseapp.app.selectlocationsearch;

import java.util.List;

/**
 * Created by NAVER on 2017-08-10.
 */
/// fields are open for GSON interoperability
public class SearchResult {
    public String lastBuildDate;
    public int total;
    public int start;
    public int display;
    public List<SearchResultItem> items;
}
