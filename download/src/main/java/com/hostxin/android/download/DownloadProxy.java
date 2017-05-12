package com.hostxin.android.download;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.hostxin.android.download.Download.ConnectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloadProxy {

    private IDownloadManager mDownloadManager = null;

    private static final List<Download.DownloadCallback> mDownloadCallbacks = new ArrayList<Download.DownloadCallback>();

    private IDownloadCallback.Stub mDownloadCallback = new IDownloadCallback.Stub() {

        @Override
        public void onSuccess(DownloadInfo downloadInfo) throws RemoteException {
            // TODO Auto-generated method stub
            notifySuccess(downloadInfo);
        }

        @Override
        public void onStop(DownloadInfo downloadInfo) throws RemoteException {
            // TODO Auto-generated method stub
            notifyStop(downloadInfo);
        }

        @Override
        public void onStart(DownloadInfo downloadInfo) throws RemoteException {
            // TODO Auto-generated method stub
            notifyStart(downloadInfo);
        }

        @Override
        public void onRemove(DownloadInfo downloadInfo) throws RemoteException {
            // TODO Auto-generated method stub
            notifyRemove(downloadInfo);
        }

        @Override
        public void onProcess(DownloadInfo downloadInfo) throws RemoteException {
            // TODO Auto-generated method stub
            notifyProgress(downloadInfo);
        }

        @Override
        public void onPending(DownloadInfo downloadInfo) throws RemoteException {
            // TODO Auto-generated method stub
            notifyPending(downloadInfo);
        }

        @Override
        public void onError(DownloadInfo downloadInfo) throws RemoteException {
            // TODO Auto-generated method stub
            notifyError(downloadInfo);
        }

        @Override
        public void onConnecting(DownloadInfo downloadInfo)
                throws RemoteException {
            // TODO Auto-generated method stub
            notifyConnecting(downloadInfo);
        }
    };

    private void notifySuccess(DownloadInfo downloadInfo) {
        synchronized (mDownloadCallbacks) {
            for (Download.DownloadCallback callback : mDownloadCallbacks) {
                try {
                    callback.onSuccess(downloadInfo);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyStart(DownloadInfo downloadInfo) {
        synchronized (mDownloadCallbacks) {
            for (Download.DownloadCallback callback : mDownloadCallbacks) {
                try {
                    callback.onStart(downloadInfo);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyStop(DownloadInfo downloadInfo) {
        synchronized (mDownloadCallbacks) {
            for (Download.DownloadCallback callback : mDownloadCallbacks) {
                try {
                    callback.onStop(downloadInfo);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyRemove(DownloadInfo downloadInfo) {
        synchronized (mDownloadCallbacks) {
            for (Download.DownloadCallback callback : mDownloadCallbacks) {
                try {
                    callback.onRemove(downloadInfo);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyProgress(DownloadInfo downloadInfo) {
        synchronized (mDownloadCallbacks) {
            for (Download.DownloadCallback callback : mDownloadCallbacks) {
                try {
                    callback.onProgress(downloadInfo);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyPending(DownloadInfo downloadInfo) {
        synchronized (mDownloadCallbacks) {
            for (Download.DownloadCallback callback : mDownloadCallbacks) {
                try {
                    callback.onPending(downloadInfo);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyError(DownloadInfo downloadInfo) {
        synchronized (mDownloadCallbacks) {
            for (Download.DownloadCallback callback : mDownloadCallbacks) {
                try {
                    callback.onError(downloadInfo);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyConnecting(DownloadInfo downloadInfo) {
        synchronized (mDownloadCallbacks) {
            for (Download.DownloadCallback callback : mDownloadCallbacks) {
                try {
                    callback.onConnecting(downloadInfo);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    private ConnectListener mConnectListener;

    private AtomicBoolean isInited = new AtomicBoolean(false);

    private AtomicBoolean isConnected = new AtomicBoolean(false);

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected.set(false);
            mDownloadManager = null;
            final ConnectListener listener = mConnectListener;
            if (listener != null) {
                listener.onServiceDisconnected();
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnected.set(true);
            mDownloadManager = IDownloadManager.Stub.asInterface(service);
            remoteSetDownloadCallback();
            final ConnectListener listener = mConnectListener;
            if (listener != null) {
                listener.onServiceConnected();
            }
        }
    };

    public void init(Context context) {
        if (isInited.get()) {
            return;
        }
        isInited.set(true);
        bindService(context);
    }

    public void bindService(Context context) {
        if (isConnected.get()) {
            return;
        }
        Intent intent = new Intent(Contants.DOWNLOAD_ACTION);
        intent.setPackage(Contants.DOWNLOAD_PACKAGE);
        context.bindService(intent, mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        context.unbindService(mServiceConnection);
    }

    public void setConnectListener(ConnectListener mConnectListener) {
        this.mConnectListener = mConnectListener;
    }

    public void addDownloadCallback(Download.DownloadCallback downloadCallback) {
        synchronized (mDownloadCallbacks) {
            if (!mDownloadCallbacks.contains(downloadCallback)) {
                mDownloadCallbacks.add(downloadCallback);
            }
        }
    }

    public void removeDownloadCallback(Download.DownloadCallback downloadCallback) {
        synchronized (mDownloadCallbacks) {
            mDownloadCallbacks.remove(downloadCallback);
        }
    }


    public void addDownload(String packageName, String downloadUrl)
            throws RemoteException {
        mDownloadManager.addDownload(packageName, downloadUrl);
    }

    public void removeDownload(String packageName, String downloadUrl)
            throws RemoteException {
        mDownloadManager.removeDownload(packageName, downloadUrl);
    }

    public DownloadInfo getDownloadInfoByDowlnadId(String packageName)
            throws RemoteException {
        return mDownloadManager.getDownloadInfoByDowlnadId(packageName);
    }

    private void remoteSetDownloadCallback() {
        try {
            mDownloadManager.setDownloadCallback(mDownloadCallback);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected.get();
    }

}
