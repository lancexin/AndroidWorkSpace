package com.hostxin.android.download;

public class DownloadException extends Exception {

    public static final int ERROR_FILE_EXCEPTION = 190;
    public static final int ERROR_BAD_REQUEST = 191;
    public static final int ERROR_NO_NET = 192;
    public static final int ERROR_DATA_ERROR = 193;
    public static final int ERROR_RESPONSE_CODE = 194;
    public static final int ERROR_CONTANT_TYPE = 195;
    public static final int ERROR_CONTANT_LENGTH = 196;
    public static final int ERROR_UNHANDLED_HTTP_CODE = 197;


    private final int mFinalStatus;

    public DownloadException(int finalStatus, String message) {
        super(message);
        mFinalStatus = finalStatus;
    }

    public DownloadException(int finalStatus, Throwable t) {
        this(finalStatus, t.getMessage());
        initCause(t);
    }

    public DownloadException(int finalStatus, String message, Throwable t) {
        this(finalStatus, message);
        initCause(t);
    }

    public int getFinalStatus() {
        return mFinalStatus;
    }
}
