package com.hostxin.android.download;

import android.content.Context;
import android.os.RemoteException;


public class Download {

    public static interface ConnectListener {
        public void onServiceDisconnected();

        public void onServiceConnected();
    }

    public static interface DownloadCallback {

        public void onSuccess(DownloadInfo downloadInfo) throws RemoteException;

        public void onStop(DownloadInfo downloadInfo) throws RemoteException;

        public void onStart(DownloadInfo downloadInfo) throws RemoteException;

        public void onRemove(DownloadInfo downloadInfo) throws RemoteException;

        public void onProgress(DownloadInfo downloadInfo) throws RemoteException;

        public void onPending(DownloadInfo downloadInfo) throws RemoteException;

        public void onError(DownloadInfo downloadInfo) throws RemoteException;

        public void onConnecting(DownloadInfo downloadInfo)
                throws RemoteException;
    }

    ;


    private static DownloadProxy mProxy = new DownloadProxy();

    public static void init(Context context) {
        mProxy.init(context);
    }

    public static void bindService() {
        mProxy.bindService();
    }

    public static void unbindService() {
        mProxy.unbindService();
    }

    public static void setConnectListener(ConnectListener mConnectListener) {
        mProxy.setConnectListener(mConnectListener);
    }

    public static void addDownloadCallback(Download.DownloadCallback downloadCallback) {
        mProxy.addDownloadCallback(downloadCallback);
    }

    public static void removeDownloadCallback(Download.DownloadCallback downloadCallback) {
        mProxy.removeDownloadCallback(downloadCallback);
    }

    public static void addDownload(String packageName, String downloadUrl)
            throws RemoteException {
        mProxy.addDownload(packageName, downloadUrl);
    }

    public static void removeDownload(String packageName, String downloadUrl)
            throws RemoteException {
        mProxy.removeDownload(packageName, downloadUrl);
    }

    public static DownloadInfo getDownloadInfoByDowlnadId(String packageName)
            throws RemoteException {
        return mProxy.getDownloadInfoByDowlnadId(packageName);
    }

    public static boolean isConnected() {
        return mProxy.isConnected();
    }

}
