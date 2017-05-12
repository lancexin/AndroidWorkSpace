package com.hostxin.android.download;

import android.os.IBinder;
import android.os.RemoteException;

import com.hostxin.android.download.IDownloadCallback;

public class DownloadCallbackWapper extends IDownloadCallback.Stub implements
        NativeDownloadManager.DownloadWatcher {

    private IDownloadCallback downloadCallback;

    private Integer mPid;

    private IBinder.DeathRecipient mDeathRecipient;

    private DownloadService mDownloadService;

    public DownloadCallbackWapper(Integer pid, IDownloadCallback downloadCallback) {
        super();
        this.downloadCallback = downloadCallback;
        this.mPid = pid;
        mDeathRecipient = new MyDeathRecipient(this);

    }

    public IDownloadCallback getDownloadCallback() {
        return downloadCallback;
    }

    @Override
    public void onPending(DownloadInfo downloadInfo) {
        // TODO Auto-generated method stub
        try {
            downloadCallback.onPending(downloadInfo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onConnecting(DownloadInfo downloadInfo) {
        // TODO Auto-generated method stub
        try {
            downloadCallback.onConnecting(downloadInfo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(DownloadInfo downloadInfo) {
        // TODO Auto-generated method stub
        try {
            downloadCallback.onStart(downloadInfo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onProcess(DownloadInfo downloadInfo) {
        // TODO Auto-generated method stub
        try {
            downloadCallback.onProcess(downloadInfo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onStop(DownloadInfo downloadInfo) {
        // TODO Auto-generated method stub
        try {
            downloadCallback.onStop(downloadInfo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(DownloadInfo downloadInfo) {
        // TODO Auto-generated method stub
        try {
            downloadCallback.onSuccess(downloadInfo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onError(DownloadInfo downloadInfo) {
        // TODO Auto-generated method stub
        try {
            downloadCallback.onError(downloadInfo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onRemove(DownloadInfo downloadInfo) {
        // TODO Auto-generated method stub
        try {
            downloadCallback.onRemove(downloadInfo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((downloadCallback == null) ? 0 : downloadCallback.hashCode());
        return result;
    }

    public Integer getPid() {
        return mPid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DownloadCallbackWapper other = (DownloadCallbackWapper) obj;
        if (downloadCallback == null) {
            if (other.downloadCallback != null)
                return false;
        } else if (!downloadCallback.equals(other.downloadCallback))
            return false;
        return true;
    }

}
