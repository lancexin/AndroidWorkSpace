package com.hostxin.android.retrofit;

import java.io.IOException;

/**
 * @author Lance.Xin
 * @since 2014-9-1
 */
public class ConnectException extends IOException {


    private static final long serialVersionUID = 1L;

    public static final int ERROR_TYPE_CONNECT = 0;
    public static final int ERROR_TYPE_SELF = 1;
    public static final int ERROR_TYPE_WATCH = 2;

    public static final int ERROR_CODE_SUCCESS = 200;
    public static final int ERROR_CODE_NO_NET = 1001;
    public static final int ERROR_CODE_DNS = 1002;
    public static final int ERROR_CODE_SOCKET = 1003;
    public static final int ERROR_CODE_SOCKETTOMEOUT = 1004;
    public static final int ERROR_CODE_IO = 1005;
    public static final int ERROR_CODE_UNKONW = 1006;
    public static final int ERROR_CODE_JSON_ERROR = 1007;

    public static final int ERROR_CODE_404 = 404;
    public static final int ERROR_CODE_500 = 500;

    private int errorCode;

    private String reason;

    public ConnectException(int errorCode, String reason) {
        super(reason);
        this.errorCode = errorCode;
        this.reason = reason;
        // TODO Auto-generated constructor stub
    }

    public ConnectException(int errorCode) {
        super();
        this.errorCode = errorCode;
        // TODO Auto-generated constructor stub
    }

    public int getErrorCode() {
        return errorCode;
    }
}
