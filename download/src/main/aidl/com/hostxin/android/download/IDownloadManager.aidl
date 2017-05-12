package com.hostxin.android.download;
import com.hostxin.android.download.DownloadInfo;
import com.hostxin.android.download.IDownloadCallback;
interface IDownloadManager {     
    void addDownload(String packageName,String downloadUrl);

	void removeDownload(String packageName,String downloadUrl);
	
	DownloadInfo getDownloadInfoByDowlnadId(String packageName);

	void setDownloadCallback(in IDownloadCallback downloadCallback);
    
} 