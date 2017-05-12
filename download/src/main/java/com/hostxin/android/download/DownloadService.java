package com.hostxin.android.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SparseArray;

import com.hostxin.android.util.Dbg;

import java.util.HashMap;
import java.util.Map;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    private static SparseArray<DownloadCallbackWapper> watchers = new SparseArray<DownloadCallbackWapper>();

    @Override
    public void onCreate() {
        Dbg.d(TAG, "DownloadService  onCreate");
        super.onCreate();
        NativeDownloadManager.getInstence().init(Contants.FILE_PATH);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Dbg.d(TAG, "DownloadService  onBind");
        return stub.asBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Dbg.d(TAG, "DownloadService  onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Dbg.d(TAG, "DownloadService  onDestroy");
        super.onDestroy();
        watchers.clear();
        NativeDownloadManager.getInstence().quit();
    }

    private IDownloadManager.Stub stub = new IDownloadManager.Stub() {

        @Override
        public void addDownload(String packageName, String downloadUrl)
                throws RemoteException {
            Dbg.d(TAG, "DownloadService  addDownload  packageName:" + packageName + "   downloadUrl:" + downloadUrl);
            long l = Binder.clearCallingIdentity();
            try {
                NativeDownloadManager.getInstence().addDownload(packageName, 0,
                        downloadUrl);
            } finally {
                Binder.restoreCallingIdentity(l);
            }
        }

        @Override
        public void removeDownload(String packageName, String downloadUrl)
                throws RemoteException {
            Dbg.d(TAG, "DownloadService  removeDownload  packageName:" + packageName + "   downloadUrl:" + downloadUrl);
            long l = Binder.clearCallingIdentity();
            try {
                NativeDownloadManager.getInstence().removeDownload(packageName,
                        downloadUrl);
            } finally {
                Binder.restoreCallingIdentity(l);
            }
        }

        @Override
        public DownloadInfo getDownloadInfoByDowlnadId(String downloadId)
                throws RemoteException {
            //LogUtil.d(TAG, "DownloadService  getDownloadInfoByPackageName  packageName:"+packageName);
            long l = Binder.clearCallingIdentity();
            try {
                return NativeDownloadManager.getInstence()
                        .getDownloadInfoByDownloadId(downloadId);
            } finally {
                Binder.restoreCallingIdentity(l);
            }
        }

        @Override
        public void setDownloadCallback(IDownloadCallback downloadCallback)
                throws RemoteException {

            Integer pid = Binder.getCallingPid();
            Dbg.d(TAG, "DownloadService  setDownloadCallback pid:" + pid);
            long l = Binder.clearCallingIdentity();
            try {
                synchronized (watchers) {
                    if (watchers.indexOfKey(pid) >= 0) {
                        DownloadCallbackWapper dcw = watchers.get(pid);
                        IDownloadCallback oldDownloadCallback = dcw
                                .getDownloadCallback();
                        if (oldDownloadCallback.equals(downloadCallback)) {
                            Dbg.d(TAG, "DownloadService  setDownloadCallback oldDownloadCallback.equals(downloadCallback) return");
                            return;
                        }
                        Dbg.d(TAG, "DownloadService  setDownloadCallback removeDownloadWatcher");
                        NativeDownloadManager.getInstence()
                                .removeDownloadWatcher(dcw);
                    }

                    if (downloadCallback != null) {
                        DownloadCallbackWapper dcw = new DownloadCallbackWapper(
                                pid, downloadCallback);
                        watchers.put(pid, dcw);
                        NativeDownloadManager.getInstence()
                                .addDownloadCallback(dcw);
                        Dbg.d(TAG, "DownloadService  setDownloadCallback add DownloadCallbackWapper:" + dcw);
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(l);
            }
        }
    };

    public static void removeDownloadCallbackByPid(Integer pid) {
        synchronized (watchers) {
            Dbg.d(TAG, "DownloadService  removeDownloadCallbackByPid :" + pid);
            watchers.remove(pid);
        }
    }
}
