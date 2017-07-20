package com.navercorp.android.lseapp.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.navercorp.android.lseapp.R;

public class WriteScreenArticleActivity extends AppCompatActivity implements WriteScreenArticleContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_screen_article);
    }
}
