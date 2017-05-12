package com.hostxin.android.download;
import com.hostxin.android.download.DownloadInfo;

interface IDownloadCallback {     
    void onPending(in DownloadInfo downloadInfo);

	void onStart(in DownloadInfo downloadInfo);
	
	void onProcess(in DownloadInfo downloadInfo);

	 void onStop(in DownloadInfo downloadInfo);

	void onSuccess(in DownloadInfo downloadInfo);

	void onError(in DownloadInfo downloadInfo);
	
	void onConnecting(in DownloadInfo downloadInfo);
	
	void onRemove(in DownloadInfo downloadInfo);
    
} 