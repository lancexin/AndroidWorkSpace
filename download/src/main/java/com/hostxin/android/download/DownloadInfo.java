package com.hostxin.android.download;


import android.os.Parcel;
import android.os.Parcelable;

public class DownloadInfo implements Parcelable {

    public static class State {
        public final static int STATUS_PENDING = 1 << 0;
        public final static int STATUS_CONNECTING = 1 << 1;
        public final static int STATUS_DOWNLOADING = 1 << 2;
        public final static int STATUS_PAUSED = 1 << 3;
        public final static int STATUS_SUCCESSFUL = 1 << 4;
        public final static int STATUS_FAILED = 1 << 5;
    }

    private String mFileName;
    private String mMimeType;
    private int mState = State.STATUS_PENDING;
    private long mTotalBytes = -1;
    private long mCurrentBytes = -1;
    private String mETag;

    private String mDownloadUrl;
    private String mRealDownloadUrl;
    private String mDownloadId;
    private int mDownloadtype = -1;

    private String mErrorMsg;
    private int mErrorCode;

    private int mProgress = -1;
    private long mSpeed = -1;

    public DownloadInfo() {
        // TODO Auto-generated constructor stub
    }

    public DownloadInfo(Parcel paramAnonymousParcel) {
        mFileName = paramAnonymousParcel.readString();
        mMimeType = paramAnonymousParcel.readString();
        mState = paramAnonymousParcel.readInt();
        mTotalBytes = paramAnonymousParcel.readLong();
        mCurrentBytes = paramAnonymousParcel.readLong();
        mETag = paramAnonymousParcel.readString();
        mDownloadUrl = paramAnonymousParcel.readString();
        mRealDownloadUrl = paramAnonymousParcel.readString();
        mDownloadId = paramAnonymousParcel.readString();
        mErrorMsg = paramAnonymousParcel.readString();
        mErrorCode = paramAnonymousParcel.readInt();
        mProgress = paramAnonymousParcel.readInt();
        mSpeed = paramAnonymousParcel.readLong();
        mDownloadtype = paramAnonymousParcel.readInt();
    }

    public static final Creator<DownloadInfo> CREATOR = new Creator<DownloadInfo>() {
        public DownloadInfo createFromParcel(Parcel paramAnonymousParcel) {
            return new DownloadInfo(paramAnonymousParcel);
        }

        public DownloadInfo[] newArray(int paramAnonymousInt) {
            return new DownloadInfo[paramAnonymousInt];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(mFileName);
        dest.writeString(mMimeType);
        dest.writeInt(mState);
        dest.writeLong(mTotalBytes);
        dest.writeLong(mCurrentBytes);
        dest.writeString(mETag);
        dest.writeString(mDownloadUrl);
        dest.writeString(mRealDownloadUrl);
        dest.writeString(mDownloadId);
        dest.writeString(mErrorMsg);
        dest.writeInt(mErrorCode);
        dest.writeInt(mProgress);
        dest.writeLong(mSpeed);
        dest.writeInt(mDownloadtype);
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public void setMimeType(String mMimeType) {
        this.mMimeType = mMimeType;
    }

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        this.mState = mState;
    }

    public long getTotalBytes() {
        return mTotalBytes;
    }

    public void setTotalBytes(long mTotalBytes) {
        this.mTotalBytes = mTotalBytes;
    }

    public long getCurrentBytes() {
        return mCurrentBytes;
    }

    public void setCurrentBytes(long mCurrentBytes) {
        this.mCurrentBytes = mCurrentBytes;
    }

    public String getETag() {
        return mETag;
    }

    public void setETag(String mETag) {
        this.mETag = mETag;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String mDownloadUrl) {
        this.mDownloadUrl = mDownloadUrl;
    }

    public String getRealDownloadUrl() {
        return mRealDownloadUrl;
    }

    public void setRealDownloadUrl(String mRealDownloadUrl) {
        this.mRealDownloadUrl = mRealDownloadUrl;
    }


    public void setDownloadtype(int downloadtype) {
        this.mDownloadtype = downloadtype;
    }

    public int getDownloadtype() {
        return mDownloadtype;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public void setErrorMsg(String mErrorMsg) {
        this.mErrorMsg = mErrorMsg;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
    }

    public long getSpeed() {
        return mSpeed;
    }

    public void setSpeed(long mSpeed) {
        this.mSpeed = mSpeed;
    }

    public String getDownloadId() {
        return mDownloadId;
    }

    public void setDownloadId(String mDownloadId) {
        this.mDownloadId = mDownloadId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mDownloadUrl == null) ? 0 : mDownloadUrl.hashCode());
        result = prime * result
                + ((mDownloadId == null) ? 0 : mDownloadId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DownloadInfo other = (DownloadInfo) obj;
        if (mDownloadUrl == null) {
            if (other.mDownloadUrl != null)
                return false;
        } else if (!mDownloadUrl.equals(other.mDownloadUrl))
            return false;
        if (mDownloadId == null) {
            if (other.mDownloadId != null)
                return false;
        } else if (!mDownloadId.equals(other.mDownloadId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DownloadInfo [mFileName=" + mFileName + ", mMimeType="
                + mMimeType + ", mState=" + mState + ", mTotalBytes="
                + mTotalBytes + ", mCurrentBytes=" + mCurrentBytes + ", mETag="
                + mETag + ", mDownloadUrl=" + mDownloadUrl
                + ", mRealDownloadUrl=" + mRealDownloadUrl + ", mDownloadId="
                + mDownloadId + ", mErrorMsg=" + mErrorMsg + ", mErrorCode="
                + mErrorCode + ", mProgress=" + mProgress + ", mSpeed="
                + mSpeed + "]";
    }


}
