package com.hostxin.android.download;


import com.hostxin.android.util.MD5;

import java.io.File;

public class DownloadUtil {

    public static void getContentRange(String s, long[] values) {

        if (!s.startsWith("bytes")) {
            values[0] = 0;
            values[1] = 0;
            values[2] = 0;
            return;
        }
        int xin = s.indexOf("*");
        if (xin != -1) {
            int p3 = s.indexOf("/");
            String totalS = s.substring(p3 + 1);
            values[0] = Integer.parseInt(totalS) - 1;
            values[1] = Integer.parseInt(totalS) - 1;
            values[2] = Integer.parseInt(totalS);
            return;
        }
        int p = s.indexOf(" ");
        int p2 = s.indexOf("-");
        int p3 = s.indexOf("/");
        String startS = s.substring(p + 1, p2);
        String endS = s.substring(p2 + 1, p3);
        String totalS = s.substring(p3 + 1);
        values[0] = Integer.parseInt(startS);
        values[1] = Integer.parseInt(endS);
        values[2] = Integer.parseInt(totalS);
    }

    public static String getPostfix(String fname) {
        String postfix = null;
        if (fname == null)
            return "";
        if (fname.indexOf(".") != -1) {
            postfix = fname.substring(fname.indexOf("."));
        } else {
            return "";
        }
        return postfix;
    }

    public static String withOutPostfix(String fname) {
        if (fname == null)
            return null;
        if (fname.indexOf(".") != -1) {
            fname = fname.substring(0, fname.indexOf("."));
        } else {
            return "";
        }
        return fname;
    }

    public static void renameFileName(File file, File newFile) {
        if (!file.exists()) {
            return;
        }
        if (newFile.exists()) {
            newFile.delete();
        }
        file.renameTo(newFile);
    }

    public static boolean hasFile(String filePath, String downloadUrl) {
        File newFile = new File(filePath + MD5.getMD5(downloadUrl));
        return newFile.exists();
    }

    public static boolean hasTmpFile(String filePath, String downloadUrl) {
        File newFile = new File(filePath + MD5.getMD5(downloadUrl) + ".tmp");
        return newFile.exists();
    }

    public static File getTmpFile(String filePath, String downloadUrl) {
        File newFile = new File(filePath + MD5.getMD5(downloadUrl) + ".tmp");
        return newFile;
    }

    public static File getFile(String filePath, String downloadUrl) {
        File newFile = new File(filePath + MD5.getMD5(downloadUrl));
        return newFile;
    }

    public static String getFilePath(String filePath, String downloadUrl) {
        return filePath + MD5.getMD5(downloadUrl);
    }

    public static void checkAndCreateFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
