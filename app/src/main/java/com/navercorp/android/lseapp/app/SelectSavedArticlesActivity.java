package com.navercorp.android.lseapp.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.navercorp.android.lseapp.R;

public class SelectSavedArticlesActivity extends AppCompatActivity implements SelectSavedArticlesContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_saved_articles);
    }
}
