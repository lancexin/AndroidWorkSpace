package com.hostxin.android.download;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.hostxin.android.util.Dbg;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class DownloadManagerTest {

    private static final String TAG = "DownloadManagerTest";

    @Test
    public void testDownloadMaianger() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hostxin.android.download.test", appContext.getPackageName());

        //this always init in Application
        NativeDownloadManager.getInstence().init(appContext,Contants.FILE_PATH);

        //this always set in Activity or Fragment
        NativeDownloadManager.getInstence().addDownloadCallback(mDownloadWatcher);

        String downloadId = "";
        int downloadtype = 1;
        String downloadUrl = "";
        NativeDownloadManager.getInstence().addDownload(downloadId,downloadtype,downloadUrl);

        //you mast remove it when the Activity or Fragment is destory
        //NativeDownloadManager.getInstence().removeDownloadWatcher(mDownloadWatcher);
    }

    //we always change the ui when the downloadinfo is change;
    NativeDownloadManager.DownloadWatcher mDownloadWatcher = new NativeDownloadManager.DownloadWatcher() {
        @Override
        public void onPending(DownloadInfo downloadInfo) {
            Dbg.d(TAG,"onPending "+downloadInfo);
        }

        @Override
        public void onConnecting(DownloadInfo downloadInfo) {
            Dbg.d(TAG,"onConnecting "+downloadInfo);
        }

        @Override
        public void onStart(DownloadInfo downloadInfo) {
            Dbg.d(TAG,"onStart "+downloadInfo);
        }

        @Override
        public void onProcess(DownloadInfo downloadInfo) {
            Dbg.d(TAG,"onProcess "+downloadInfo);
        }

        @Override
        public void onStop(DownloadInfo downloadInfo) {
            Dbg.d(TAG,"onStop "+downloadInfo);
        }

        @Override
        public void onSuccess(DownloadInfo downloadInfo) {
            Dbg.d(TAG,"onSuccess "+downloadInfo);
        }

        @Override
        public void onError(DownloadInfo downloadInfo) {
            Dbg.d(TAG,"onError "+downloadInfo);
        }

        @Override
        public void onRemove(DownloadInfo downloadInfo) {
            Dbg.d(TAG,"onRemove "+downloadInfo);
        }
    };
}
