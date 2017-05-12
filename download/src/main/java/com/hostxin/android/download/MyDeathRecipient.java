package com.hostxin.android.download;

import android.os.IBinder;
import android.os.RemoteException;

import com.hostxin.android.util.Dbg;

public class MyDeathRecipient implements IBinder.DeathRecipient {

    private static final String TAG = "MyDeathRecipient";
    private DownloadCallbackWapper mDownloadCallbackWapper;

    public MyDeathRecipient(DownloadCallbackWapper downloadCallbackWapper) {
        this.mDownloadCallbackWapper = downloadCallbackWapper;
        try {
            mDownloadCallbackWapper.asBinder().linkToDeath(this, 0);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void binderDied() {
        Dbg.d(TAG, "binderDied  " + mDownloadCallbackWapper.toString());
        mDownloadCallbackWapper.asBinder().unlinkToDeath(this, 0);
        DownloadService.removeDownloadCallbackByPid(mDownloadCallbackWapper
                .getPid());
    }
}