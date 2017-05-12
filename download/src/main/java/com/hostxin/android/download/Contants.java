package com.hostxin.android.download;

import android.os.Environment;

import java.util.HashSet;
import java.util.Set;

public class Contants {

    public static final String TAG = "Contants";

    public static final Set<String> DOWNLOAD_CONTANT_TYPES = new HashSet<String>();

    static{
        DOWNLOAD_CONTANT_TYPES.add("application/vnd.android.package-archive");
    }

    public static final String FILE_PATH = Environment.getDownloadCacheDirectory().getPath();


}
