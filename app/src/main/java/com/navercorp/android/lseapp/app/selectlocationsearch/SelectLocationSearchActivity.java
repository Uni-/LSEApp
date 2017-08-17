package com.navercorp.android.lseapp.app.selectlocationsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;

import com.navercorp.android.lseapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public final class SelectLocationSearchActivity extends AppCompatActivity implements View.OnClickListener, ListViewCompat.OnItemClickListener {

    private AppCompatEditText mEditText;
    private AppCompatButton mButton;
    private ListViewCompat mListView;
    private AppCompatTextView mListEmptyTextView;

    private SearchResultItemsListAdapter mAdapter;

    public static final String EXTRA_INT_KEY_LOCATION_KATECH_X = "LocationKatechX";
    public static final String EXTRA_INT_KEY_LOCATION_KATECH_Y = "LocationKatechY";
    public static final String EXTRA_STRING_KEY_LOCATION_TITLE = "LocationTitle";
    public static final String EXTRA_STRING_KEY_LOCATION_ADDRESS = "LocationAddress";

    @Override // AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_search);

        mEditText = (AppCompatEditText) findViewById(R.id.activity_select_location_search_edittext);
        mButton = (AppCompatButton) findViewById(R.id.activity_select_location_search_button);
        mListView = (ListViewCompat) findViewById(R.id.activity_select_location_search_list);
        mListEmptyTextView = (AppCompatTextView) findViewById(R.id.activity_select_location_search_text_list_empty);

        setResult(RESULT_CANCELED);

        mAdapter = new SearchResultItemsListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mListEmptyTextView);
        mListView.setOnItemClickListener(this);

        mButton.setOnClickListener(this);
    }

    @Override // View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_select_location_search_button: {
                runSearch();
                break;
            }
        }
    }

    @Override // ListViewCompat.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchResultItem item = mAdapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_INT_KEY_LOCATION_KATECH_X, Integer.parseInt(item.mapx));
        intent.putExtra(EXTRA_INT_KEY_LOCATION_KATECH_Y, Integer.parseInt(item.mapy));
        intent.putExtra(EXTRA_STRING_KEY_LOCATION_TITLE, item.title);
        intent.putExtra(EXTRA_STRING_KEY_LOCATION_ADDRESS, item.address);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void runSearch() {
        String keyword = mEditText.getText().toString();

        if (keyword.isEmpty()) {
            mAdapter.clear();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://openapi.naver.com").addConverterFactory(GsonConverterFactory.create()).build();
        NaverLocalSearch localSearch = retrofit.create(NaverLocalSearch.class);
        Call<SearchResult> searchCall = localSearch.listLocation(keyword);
        searchCall.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                SearchResult searchResult = response.body();
                mAdapter.clear();
                mAdapter.addAll(searchResult.items);
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                mAdapter.clear();
            }
        });
    }

    private interface NaverLocalSearch {
        @Headers({
                "X-Naver-Client-Id: kaA9LCofEDxiOmtLTsFT",
                "X-Naver-Client-Secret: JWsjRAnwRE",
        })
        @GET("/v1/search/local.json")
        Call<SearchResult> listLocation(@Query("query") String query);
    }
}
