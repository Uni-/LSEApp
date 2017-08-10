package com.navercorp.android.lseapp.app.selectlocationsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.navercorp.android.lseapp.R;

import java.util.List;

/**
 * Created by NAVER on 2017-08-10.
 */
/// Every moment the list gets refreshed, the list is cleared and added all new items.
/// No holder pattern used here for simple code.
class SearchResultItemsListAdapter extends ArrayAdapter<SearchResultItem> {

    private static final int layout = R.layout.item_activity_select_location_search_list;

    public SearchResultItemsListAdapter(Context context) {
        super(context, layout);
    }

    public SearchResultItemsListAdapter(Context context, List<SearchResultItem> items) {
        super(context, layout, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (convertView != null) ? convertView : layoutInflater.inflate(layout, null, false);
        AppCompatTextView titleTextView = (AppCompatTextView) view.findViewById(R.id.item_activity_select_location_search_list_title);
        AppCompatTextView addressTextView = (AppCompatTextView) view.findViewById(R.id.item_activity_select_location_search_list_address);
        SearchResultItem item = getItem(position);

        titleTextView.setText(Html.fromHtml((item != null) ? item.title : ""));
        addressTextView.setText((item != null) ? item.address : "");

        return view;
    }
}
