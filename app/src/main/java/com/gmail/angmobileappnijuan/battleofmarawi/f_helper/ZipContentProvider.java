package com.gmail.angmobileappnijuan.battleofmarawi.f_helper;

import android.net.Uri;

import com.android.vending.expansion.zipfile.APEZProvider;

import java.io.File;

public class ZipContentProvider extends APEZProvider {

    private static final String AUTHORITY = "com.gmail.angmobileappnijuan.provider";

    public static Uri buildUri(String pathIntoApk) {
        StringBuilder contentPath = new StringBuilder("content://");

        contentPath.append(AUTHORITY);
        contentPath.append(File.separator);
        contentPath.append(pathIntoApk);

        return Uri.parse(contentPath.toString());
    }

    @Override
    public String getAuthority() {
        return AUTHORITY;
    }
}
