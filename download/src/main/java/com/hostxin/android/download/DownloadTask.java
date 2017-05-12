package com.hostxin.android.download;

import android.os.Build;
import android.os.SystemClock;


import com.hostxin.android.util.Dbg;
import com.hostxin.android.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.atomic.AtomicBoolean;


public class DownloadTask implements Runnable {

    private static final String TAG = "DownloadTask";

    private DownloadInfo mDownloadInfo;

    private NativeDownloadManager mNativeDownloadManager;

    private HttpURLConnection httpUrlConnection;

    private RandomAccessFile mRandomAccessFile;

    private InputStream mInputStream;

    private AtomicBoolean isDownloading = new AtomicBoolean(false);

    private boolean isRunning = false;

    private boolean pauseByUser = false;

    private File mFile;

    private File mTmpFile;

    private URL mUrl;

    private long mSpeed;
    private long mSpeedSampleStart;
    private long mSpeedSampleBytes;

    public DownloadTask(NativeDownloadManager mNativeDownloadManager,
                        DownloadInfo mDownloadInfo) {
        this.mDownloadInfo = mDownloadInfo;
        this.mNativeDownloadManager = mNativeDownloadManager;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            isRunning = true;
            executeDownload();
        } catch (DownloadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (pauseByUser) {
                closeStream();
                mNativeDownloadManager.handleStop(mDownloadInfo);
            } else {
                isDownloading.set(false);
                closeStream();
                mNativeDownloadManager.handleError(mDownloadInfo, e);
            }
        } catch (Exception e){
            e.printStackTrace();
            isDownloading.set(false);
            closeStream();
        } finally {
            pauseByUser = false;
            isRunning = false;
        }
    }

    private void executeDownload() throws DownloadException {
        if (!isDownloading.get()) {
            isDownloading.set(true);
            mNativeDownloadManager.handleConnecting(mDownloadInfo);
        }

        mTmpFile = DownloadUtil.getTmpFile(
                mNativeDownloadManager.getFilePath(),
                mDownloadInfo.getDownloadUrl());
        mFile = DownloadUtil.getFile(
                mNativeDownloadManager.getFilePath(),
                mDownloadInfo.getDownloadUrl());

        long nPos = 0;
        if (mFile.exists()) {
            mFile.delete();
        }

        if (mTmpFile.exists()) {
            try {
                mRandomAccessFile = new RandomAccessFile(mTmpFile, "rw");
                nPos = mRandomAccessFile.length();
                mRandomAccessFile.seek(nPos);
            } catch (FileNotFoundException e) {
                if (mTmpFile != null && mTmpFile.exists())
                    mTmpFile.delete();
                throw new DownloadException(
                        DownloadException.ERROR_FILE_EXCEPTION, e);
            } catch (IOException e) {
                if (mTmpFile != null && mTmpFile.exists())
                    mTmpFile.delete();
                throw new DownloadException(
                        DownloadException.ERROR_FILE_EXCEPTION, e);
            }
        }

        try {
            if (StringUtils.isBlank(mDownloadInfo.getRealDownloadUrl())) {
                throw new DownloadException(DownloadException.ERROR_BAD_REQUEST, "readDownloadUrl is:" + mDownloadInfo.getRealDownloadUrl());
            }
            mUrl = new URL(mDownloadInfo.getRealDownloadUrl().trim());
        } catch (MalformedURLException e) {
            throw new DownloadException(DownloadException.ERROR_BAD_REQUEST, e);
        }

        int responseCode = -1;
        try {
            httpUrlConnection = (HttpURLConnection) mUrl.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setConnectTimeout(10 * 1000);
            httpUrlConnection.setReadTimeout(10 * 1000);
            httpUrlConnection
                    .setRequestProperty("RANGE", "bytes=" + nPos + "-");
            httpUrlConnection
                    .setRequestProperty("X_AAZENWATCH_SN", Build.SERIAL);

            responseCode = httpUrlConnection.getResponseCode();
            if (responseCode == 416){
                if (mTmpFile.exists()){
                    mTmpFile.delete();
                }
            }

        } catch (Exception e) {
            if (e instanceof ProtocolException
                    && e.getMessage().startsWith("Unexpected status line")) {
                throw new DownloadException(
                        DownloadException.ERROR_UNHANDLED_HTTP_CODE, e);
            } else {
                throw new DownloadException(DownloadException.ERROR_DATA_ERROR,
                        e);
            }
        }


        if (responseCode != 206 && responseCode != 200 && responseCode != 416) {
            throw new DownloadException(DownloadException.ERROR_RESPONSE_CODE,
                    "responseConde error responseConde is:" + responseCode);
        }

        String realUrl = httpUrlConnection.getHeaderField("Location");

        if (!StringUtils.isBlank(realUrl)) {
            mDownloadInfo.setRealDownloadUrl(realUrl);
            closeStream();
            executeDownload();
            return;
        }

        String contentType = httpUrlConnection.getContentType().trim();
        if (!Contants.DOWNLOAD_CONTANT_TYPES.contains(contentType)) {
            throw new DownloadException(DownloadException.ERROR_CONTANT_TYPE,
                    "download contant type error contentType:" + contentType
                            + " responseCode:" + responseCode
                            + " realDownloadUrl:"
                            + mDownloadInfo.getRealDownloadUrl());
        }

        long[] i = new long[3];
        String contentRange = httpUrlConnection.getHeaderField("Content-Range");
        String eTeg = httpUrlConnection.getHeaderField("ETag");
        if (!StringUtils.isBlank(contentRange) && (mDownloadInfo.getETag() == null || (mDownloadInfo.getETag() != null && eTeg
                .equals(mDownloadInfo.getETag())))) {
            DownloadUtil.getContentRange(contentRange, i);
        } else {
            if (mTmpFile.exists()) {
                mTmpFile.delete();
                closeStream();
                executeDownload();
                return;
            }
            i[2] = httpUrlConnection.getContentLength();
            Dbg.d(TAG,contentRange + "  " + i[2]);
            if (i[2] <= 1) {
                throw new DownloadException(
                        DownloadException.ERROR_CONTANT_LENGTH,
                        "contant length error contentLenght:" + i[2]);
            }
        }

        if (i[0] == i[1] && i[0] != 0) {
            DownloadUtil.renameFileName(mTmpFile, mFile);
            mFile.setReadable(true);
            mFile.setWritable(true);
            mDownloadInfo.setProgress(100);
            mDownloadInfo.setTotalBytes(i[2]);
            mDownloadInfo.setCurrentBytes(i[2]);
            closeStream();
            mNativeDownloadManager.handleSuccess(mDownloadInfo);
            return;
        }

        try {
            mInputStream = httpUrlConnection.getInputStream();
        } catch (IOException e) {
            throw new DownloadException(DownloadException.ERROR_DATA_ERROR, e);
        } catch (Exception e){
            throw new DownloadException(DownloadException.ERROR_DATA_ERROR, e);
        }
        if (mRandomAccessFile == null) {
            try {
                mRandomAccessFile = new RandomAccessFile(mTmpFile, "rw");
                mDownloadInfo.setETag(eTeg);
            } catch (FileNotFoundException e) {
                if (mTmpFile != null && mTmpFile.exists())
                    mTmpFile.delete();
                throw new DownloadException(
                        DownloadException.ERROR_FILE_EXCEPTION, e);
            }
        }

        if (i[2] <= 1) {
            throw new DownloadException(DownloadException.ERROR_CONTANT_LENGTH,
                    "mTotalBytes  error mTotalBytes:" + i[2]);
        }
        mDownloadInfo.setFileName(getFileName(httpUrlConnection));
        if (isDownloading.get() && nPos > 0) {
            mNativeDownloadManager.handleContinue(mDownloadInfo);
        } else {
            mNativeDownloadManager.handleStart(mDownloadInfo);
        }

        byte[] b = new byte[1024 * 4];
        int nRead;
        mDownloadInfo.setTotalBytes(i[2]);
        long total = nPos;
        while (true) {
            try {
                nRead = mInputStream.read(b, 0, b.length);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            }

            if (nRead <= 0) {
                break;
            }

            if (isDownloading.get()) {
                try {
                    mRandomAccessFile.write(b, 0, nRead);
                } catch (Exception e) {
                    throw new DownloadException(
                            DownloadException.ERROR_FILE_EXCEPTION, e);
                }
                total += nRead;
                mDownloadInfo.setCurrentBytes(total);
                updateProgress();
            } else {
                break;
            }
        }

        if (isDownloading.get()) {
            if (total != i[2]) {
                throw new DownloadException(DownloadException.ERROR_CONTANT_LENGTH,
                        "download length error downloaded:" + total + "  total:" + i[2]);
            }

            DownloadUtil.renameFileName(mTmpFile, mFile);
            mFile.setReadable(true);
            mFile.setWritable(true);
            isDownloading.set(false);
            mNativeDownloadManager.handleSuccess(mDownloadInfo);
        } else {
            mNativeDownloadManager.handleStop(mDownloadInfo);
        }
        closeStream();
    }

    private void updateProgress() {
        boolean changed = false;
        final long now = SystemClock.elapsedRealtime();
        final long currentBytes = mDownloadInfo.getCurrentBytes();
        final long totalBytes = mDownloadInfo.getTotalBytes();

        final long sampleDelta = now - mSpeedSampleStart;

        if (sampleDelta > 1000) {
            final long sampleSpeed = ((currentBytes - mSpeedSampleBytes) * 1000)
                    / sampleDelta;

            if (mSpeed == 0) {
                mSpeed = sampleSpeed;
            } else {
                mSpeed = ((mSpeed * 3) + sampleSpeed) / 4;
            }
            mDownloadInfo.setSpeed(mSpeed);
            // Only notify once we have a full sample window
            if (mSpeedSampleStart != 0) {
                changed = true;
            }

            mSpeedSampleStart = now;
            mSpeedSampleBytes = currentBytes;
        }

        int progress = (int) (currentBytes * 100f / totalBytes);
        if (progress != mDownloadInfo.getProgress()) {
            mDownloadInfo.setProgress(progress);
            changed = true;
        }

        if (changed) {
            mNativeDownloadManager.handleProgress(mDownloadInfo);
        }
    }

    public boolean isDownloading() {
        // TODO Auto-generated method stub
        return isDownloading.get();
    }

    public void stopDownload() {
        if (pauseByUser) {
            return;
        }
        isDownloading.set(false);
        pauseByUser = true;
        closeStream();
        mNativeDownloadManager.handleStop(mDownloadInfo);
    }

    private String getFileName(HttpURLConnection httpUrlConnection) {
        String fileName = null;
        try {
            String contentDisposition = httpUrlConnection
                    .getHeaderField("Content-Disposition");
            if (!StringUtils.isBlank(contentDisposition) && contentDisposition.contains("filename=")) {
                fileName = URLDecoder.decode(
                        contentDisposition.substring(contentDisposition
                                .indexOf("filename=") + 9), "UTF-8");
                return fileName;
            }

            String file = httpUrlConnection.getURL().getFile();
            fileName = file.substring(file.lastIndexOf('/') + 1);
            return fileName;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileName;
    }

    private void closeStream() {

        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RuntimeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (mRandomAccessFile != null) {
            try {
                mRandomAccessFile.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RuntimeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (httpUrlConnection != null) {
            try {
                httpUrlConnection.disconnect();
            } catch (RuntimeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
