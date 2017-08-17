package com.navercorp.android.lseapp.app.selectsavedarticles;

/**
 * Created by NAVER on 2017-08-14.
 */

public class SavedArticleEntry {

    public final String timeSaved;
    public final String sha1sum;
    public final String title;

    public SavedArticleEntry(String timeSaved, String sha1sum, String title) {
        this.timeSaved = timeSaved;
        this.sha1sum = sha1sum;
        this.title = title;
    }
}
