package com.ikar.grossumtesttask.db;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by iKar on 11/5/15.
 */
public class UriMatcherHelper {
    public static final UriMatcher uriMatcher;

    static final String AUTHORITY = "com.ikar.grossumtesttask";
    static final String PATH = "grossumtesttask";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);

    static final int URI_CASHDESK = 1;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, URI_CASHDESK);
    }
}
