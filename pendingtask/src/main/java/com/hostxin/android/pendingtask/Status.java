package com.hostxin.android.pendingtask;

import java.lang.String;

public class Status implements Result {

	public static final Status STATUS_SUCCESS = new Status(0);
	public static final Status STATUS_ERROR = new Status(8);
	public static final Status STATUS_INTERRUPTED = new Status(12);
	public static final Status STATUS_TIMEOUT = new Status(13);
	public static final Status STATUS_CANCELED = new Status(16);
	
	private String mStatusMessage;
	private int mStatusCode;

	public Status(int n) {
		this(1, null);
	}

	public Status(int statusCode, String statusMessage) {
		this.mStatusCode = statusCode;
		this.mStatusMessage = statusMessage;
	}

	public int getStatusCode() {
		return mStatusCode;
	}

	public String getStatusMessage() {
		return mStatusMessage;
	}

	public Status getStatus() {
		return this;
	}

	public boolean isSuccess() {
		if (this.mStatusCode > 0)
			return false;
		return true;
	}

	public boolean isCanceled() {
		return this.mStatusCode == 16;
	}

	public boolean isInterrupted() {
		return this.mStatusCode == 14;
	}

}
