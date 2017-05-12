package com.hostxin.android.download;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;


import com.hostxin.android.util.Dbg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;


/**
 * @author lixin
 * @since 2014-9-3
 */
public class NativeDownloadManager {

    private static final String TAG = "NativeDownloadManager";

    public static interface DownloadWatcher {
        public void onPending(DownloadInfo downloadInfo);

        public void onConnecting(DownloadInfo downloadInfo);

        public void onStart(DownloadInfo downloadInfo);

        public void onProcess(DownloadInfo downloadInfo);

        public void onStop(DownloadInfo downloadInfo);

        public void onSuccess(DownloadInfo downloadInfo);

        public void onError(DownloadInfo downloadInfo);

        public void onRemove(DownloadInfo downloadInfo);
    }

    private static NativeDownloadManager mInstence = new NativeDownloadManager();

    private Vector<DownloadWatcher> downloadCallbacks = new Vector<DownloadWatcher>();

    private HandlerThread mHandlerThread;

    private Thread pollThread;

    private Handler mHandler;

    private boolean isLoaded = false;

    private DownloadInfo downloadingModel;

    private DownloadTask downloadTask;

    private ArrayDeque<DownloadInfo> waitDownloads = new ArrayDeque<DownloadInfo>();
    private ArrayDeque<DownloadInfo> downloads = new ArrayDeque<DownloadInfo>();
    private String mFilePath;
    private Context mContext;

    private static final int MESSAGE_DOWNLOAD_ADD = 0;
    private static final int MESSAGE_DOWNLOAD_REMOVE = 1;
    private static final int MESSAGE_DOWNLOAD_COMPLETE = 2;
    private static final int MESSAGE_DOWNLOAD_START = 3;
    private static final int MESSAGE_DOWNLOAD_CONTINUE = 4;
    private static final int MESSAGE_DOWNLOAD_PENDING = 5;
    private static final int MESSAGE_DOWNLOAD_CONNECTING = 6;
    private static final int MESSAGE_DOWNLOAD_STOP = 7;
    private static final int MESSAGE_DOWNLOAD_SCHEDULE_CHANGE = 8;
    private static final int MESSAGE_DOWNLOAD_ERROR = 9;

    public static NativeDownloadManager getInstence() {
        return mInstence;
    }

    public synchronized void init(Context conetxt,String filePath) {
        mContext = conetxt;
        if (!isLoaded) {
            HandlerThread thread = new HandlerThread(TAG);
            thread.start();
            init(conetxt, thread,filePath);
        }
    }

    public synchronized void init(Context conetxt, HandlerThread handlerThread,String filePath) {
        if (!isLoaded) {
            isLoaded = true;
            mFilePath = filePath;
            File f = new File(filePath);
            if (!f.exists()) {
                f.mkdir();
            }
            try {
                String[] args2 = {"chmod", "777", filePath};
                Runtime.getRuntime().exec(args2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pollThread = new Thread(pollRunnable);
            mHandlerThread = handlerThread;

            mHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (isLoaded) {
                        onHandleMessage(msg);
                    }
                }
            };
            pollThread.start();
        }
    }

    public void quit() {
        synchronized (downloadCallbacks) {
            downloadCallbacks.clear();
        }

        synchronized (downloads) {
            downloads.clear();
        }

        synchronized (waitDownloads) {
            waitDownloads.clear();
        }

        if (mHandlerThread != null && mHandlerThread.isAlive()) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
        isLoaded = false;
        downloadingModel = null;
        if (downloadTask != null) {
            downloadTask.stopDownload();
            downloadTask = null;
        }
        mFilePath = null;
        mHandler = null;
    }

    private Runnable pollRunnable = new Runnable() {
        @Override
        public void run() {
            while (isLoaded) {
                downloadTask = null;
                downloadingModel = null;
                synchronized (waitDownloads) {
                    if (waitDownloads.size() == 0) {
                        try {
                            waitDownloads.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    downloadingModel = waitDownloads.poll();
                }
                downloadTask = new DownloadTask(NativeDownloadManager.this,
                        downloadingModel);
                downloadTask.run();
            }
        }
    };

    protected void onHandleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case MESSAGE_DOWNLOAD_ADD:
                callDownloadAdd(msg);
                break;
            case MESSAGE_DOWNLOAD_REMOVE:
                callDownloadRemove(msg);
                break;
            case MESSAGE_DOWNLOAD_COMPLETE:
                callDownloadComplete(msg);
                break;
            case MESSAGE_DOWNLOAD_START:
                callDownloadStart(msg);
                break;
            case MESSAGE_DOWNLOAD_CONTINUE:
                callDownloadContinue(msg);
                break;
            case MESSAGE_DOWNLOAD_PENDING:
                callDownloadPending(msg);
                break;
            case MESSAGE_DOWNLOAD_CONNECTING:
                callDownloadConnecting(msg);
                break;
            case MESSAGE_DOWNLOAD_STOP:
                callDownloadStop(msg);
                break;
            case MESSAGE_DOWNLOAD_SCHEDULE_CHANGE:
                callDownloadScheduleChange(msg);
                break;
            case MESSAGE_DOWNLOAD_ERROR:
                callDownloadError(msg);
                break;

            default:
                break;
        }
    }

    public synchronized void addDownloadCallback(DownloadWatcher downloadWatcher) {
        synchronized (downloadCallbacks) {
            if (!downloadCallbacks.contains(downloadWatcher)) {
                downloadCallbacks.add(downloadWatcher);
            }
        }
    }

    public synchronized void removeDownloadWatcher(
            DownloadWatcher downloadWatcher) {
        synchronized (downloadCallbacks) {
            if (downloadCallbacks.contains(downloadWatcher)) {
                downloadCallbacks.remove(downloadWatcher);
            }
        }
    }

    public void addDownload(String downloadId, int downloadType, String downloadUrl) {

        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDownloadId(downloadId);
        downloadInfo.setDownloadUrl(downloadUrl);
        downloadInfo.setRealDownloadUrl(downloadUrl);
        downloadInfo.setDownloadtype(downloadType);
        Message message = mHandler.obtainMessage(MESSAGE_DOWNLOAD_ADD,
                downloadInfo);
        message.sendToTarget();
    }

    public void removeDownload(String downloadId, String downloadUrl) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDownloadId(downloadId);
        downloadInfo.setDownloadUrl(downloadUrl);
        Message message = mHandler.obtainMessage(MESSAGE_DOWNLOAD_REMOVE,
                downloadInfo);
        message.sendToTarget();
    }

    private synchronized void callDownloadAdd(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        Dbg.d(TAG, "callDownloadAdd " + downloadInfo.toString());
        android.util.Log.d("testSpeed", "callDownloadAdd ");


        synchronized (waitDownloads) {
            if (waitDownloads.contains(downloadInfo)) {
                return;
            }
            downloadInfo.setState(DownloadInfo.State.STATUS_PENDING);
            waitDownloads.add(downloadInfo);
            waitDownloads.notifyAll();
        }

        synchronized (downloads) {
            downloads.remove(downloadInfo);
            downloads.add(downloadInfo);
        }

        handlePending(downloadInfo);
    }
	
	public synchronized ArrayDeque<DownloadInfo> getDownloadQueue() {
		return downloads;
	}

    public synchronized ArrayDeque<DownloadInfo> getDownloadingQueue() {
        ArrayDeque<DownloadInfo> queue = new ArrayDeque<>();
        Iterator it = downloads.iterator();
        while (it.hasNext()){
            DownloadInfo downloadInfo = (DownloadInfo) it.next();
            if (downloadInfo.getState() != DownloadInfo.State.STATUS_FAILED
                    || downloadInfo.getState() != DownloadInfo.State.STATUS_SUCCESSFUL){
                queue.add(downloadInfo);
            }
        }
        return queue;
    }
	
	public synchronized ArrayDeque<DownloadInfo> getWaitingQueue() {
		return waitDownloads;
	}

    private synchronized void callDownloadRemove(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        Dbg.d(TAG, "callDownloadRemove " + downloadInfo.toString());
        synchronized (waitDownloads) {
            if (waitDownloads.contains(downloadInfo)) {
                waitDownloads.remove(downloadInfo);
            }
        }

        if (downloadingModel != null && downloadTask != null
                && downloadTask.isDownloading()) {
            if (downloadInfo.equals(downloadingModel)) {
                downloadTask.stopDownload();
                downloadTask = null;
            }
        }
        synchronized (downloads) {
            if (downloads.contains(downloadInfo)) {
                downloads.remove(downloadInfo);
            }
        }
        File mFile = DownloadUtil.getFile(getFilePath(), downloadInfo.getDownloadUrl());
        if (mFile.exists()) {
            mFile.delete();
        }
        notifyRemove(downloadInfo);
    }

    private void callDownloadComplete(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        Dbg.d(TAG, "callDownloadComplete " + downloadInfo.toString());
        android.util.Log.d("testSpeed", "callDownloadComplete ");
        notifySuccess(downloadInfo);
    }

    private void callDownloadStart(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        Dbg.d(TAG, "callDownloadStart " + downloadInfo.toString());
        android.util.Log.d("testSpeed", "callDownloadStart ");
        notifyStart(downloadInfo);
    }

    private void callDownloadContinue(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        Dbg.d(TAG, "callDownloadContinue " + downloadInfo.toString());
        notifyContinue(downloadInfo);
    }

    private void callDownloadPending(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        Dbg.d(TAG, "callDownloadPending " + downloadInfo.toString());
        notifyPending(downloadInfo);
    }

    private void callDownloadConnecting(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        Dbg.d(TAG, "callDownloadConnecting " + downloadInfo.toString());
        notifyConnecting(downloadInfo);
    }

    private void callDownloadStop(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        Dbg.d(TAG, "callDownloadStop " + downloadInfo.toString());
        notifyStop(downloadInfo);
    }

    private void callDownloadScheduleChange(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        notifyProcess(downloadInfo);
    }

    private void callDownloadError(Message msg) {
        DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
        notifyError(downloadInfo);
    }

    private void notifySuccess(DownloadInfo model) {
        synchronized (downloadCallbacks) {
            ListIterator<DownloadWatcher> ite = downloadCallbacks
                    .listIterator();
            while (ite.hasNext()) {
                ite.next().onSuccess(model);
            }
        }
    }

    private void notifyStart(DownloadInfo model) {
        synchronized (downloadCallbacks) {
            ListIterator<DownloadWatcher> ite = downloadCallbacks
                    .listIterator();
            while (ite.hasNext()) {
                ite.next().onStart(model);
            }
        }
    }

    private void notifyContinue(DownloadInfo model) {
        synchronized (downloadCallbacks) {
            ListIterator<DownloadWatcher> ite = downloadCallbacks
                    .listIterator();
            while (ite.hasNext()) {
                ite.next().onStart(model);
            }
        }
    }

    private void notifyPending(DownloadInfo model) {
        synchronized (downloadCallbacks) {
            ListIterator<DownloadWatcher> ite = downloadCallbacks
                    .listIterator();
            while (ite.hasNext()) {
                ite.next().onPending(model);
            }
        }
    }

    private void notifyConnecting(DownloadInfo model) {
        synchronized (downloadCallbacks) {
            ListIterator<DownloadWatcher> ite = downloadCallbacks
                    .listIterator();
            while (ite.hasNext()) {
                ite.next().onConnecting(model);
            }
        }
    }

    private void notifyStop(DownloadInfo model) {
        synchronized (downloadCallbacks) {
            ListIterator<DownloadWatcher> ite = downloadCallbacks
                    .listIterator();
            while (ite.hasNext()) {
                ite.next().onStop(model);
            }
        }
    }

    private void notifyProcess(DownloadInfo model) {
        synchronized (downloadCallbacks) {
            ListIterator<DownloadWatcher> ite = downloadCallbacks
                    .listIterator();
            while (ite.hasNext()) {
                ite.next().onProcess(model);
            }
        }
    }

    private void notifyError(DownloadInfo model) {
        synchronized (downloadCallbacks) {
            ListIterator<DownloadWatcher> ite = downloadCallbacks
                    .listIterator();
            while (ite.hasNext()) {
                ite.next().onError(model);
            }
        }
    }

    private void notifyRemove(DownloadInfo model) {
        synchronized (downloadCallbacks) {
            ListIterator<DownloadWatcher> ite = downloadCallbacks
                    .listIterator();
            while (ite.hasNext()) {
                ite.next().onRemove(model);
            }
        }
    }

    public DownloadInfo getDownloadingModel() {
        return downloadingModel;
    }

    public DownloadTask getDownloadTask() {
        return downloadTask;
    }

    public DownloadInfo getDownloadInfoByDownloadId(String downloadId) {
        synchronized (downloads) {
            Iterator<DownloadInfo> ite = downloads.iterator();
            while (ite.hasNext()) {
                DownloadInfo info = ite.next();
                if (info.getDownloadId().equals(downloadId)) {
                    return info;
                }
            }
        }
        return null;
    }

    protected void handleSuccess(DownloadInfo model) {
        model.setState(DownloadInfo.State.STATUS_SUCCESSFUL);
        Message message = mHandler.obtainMessage(MESSAGE_DOWNLOAD_COMPLETE,
                model);
        message.sendToTarget();
    }

    protected void handleStart(DownloadInfo model) {
        model.setState(DownloadInfo.State.STATUS_DOWNLOADING);
        Message message = mHandler.obtainMessage(MESSAGE_DOWNLOAD_START, model);
        message.sendToTarget();
    }

    protected void handleContinue(DownloadInfo model) {
        model.setState(DownloadInfo.State.STATUS_DOWNLOADING);
        Message message = mHandler.obtainMessage(MESSAGE_DOWNLOAD_CONTINUE,
                model);
        message.sendToTarget();
    }

    protected void handlePending(DownloadInfo model) {
        model.setState(DownloadInfo.State.STATUS_PENDING);
        Message message = mHandler.obtainMessage(MESSAGE_DOWNLOAD_PENDING,
                model);
        message.sendToTarget();
    }

    protected void handleConnecting(DownloadInfo model) {
        model.setState(DownloadInfo.State.STATUS_CONNECTING);
        Message message = mHandler.obtainMessage(MESSAGE_DOWNLOAD_CONNECTING,
                model);
        message.sendToTarget();
    }

    protected void handleStop(DownloadInfo model) {
        model.setState(DownloadInfo.State.STATUS_PAUSED);
        Message message = mHandler.obtainMessage(MESSAGE_DOWNLOAD_STOP, model);
        message.sendToTarget();
    }

    protected void handleProgress(DownloadInfo model) {
        Message message = mHandler.obtainMessage(
                MESSAGE_DOWNLOAD_SCHEDULE_CHANGE, model);
        message.sendToTarget();
    }

    protected void handleError(DownloadInfo model, DownloadException exception) {
        model.setState(DownloadInfo.State.STATUS_FAILED);
        model.setErrorCode(exception.getFinalStatus());
        Message message = mHandler.obtainMessage(MESSAGE_DOWNLOAD_ERROR, model);
        message.sendToTarget();
    }

    public String getFilePath() {
        return mFilePath;
    }

    public int getDownloadCount(){
        return downloads.size();
    }

}
