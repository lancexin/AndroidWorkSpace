package com.hostxin.android.download;

import android.os.Environment;

import java.util.HashSet;
import java.util.Set;

public class Contants {

    public static final Set<String> DOWNLOAD_CONTANT_TYPES = new HashSet<String>();

    static{
        DOWNLOAD_CONTANT_TYPES.add("application/vnd.android.package-archive");
    }

    //download info will cache here
    public static final String FILE_PATH = Environment.getDownloadCacheDirectory().getPath();


    public static final String DOWNLOAD_ACTION = "com.hostxin.android.download.DOWNLOAD_SERVICE";

    public static final String DOWNLOAD_PACKAGE = "com.hostxin.android";

}
