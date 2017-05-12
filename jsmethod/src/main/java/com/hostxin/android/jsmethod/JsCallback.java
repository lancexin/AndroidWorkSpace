package com.hostxin.android.jsmethod;

public class JsCallback {
	private int requestCode = 0;
	
	private String methodName;

	private String callbackId;
	

	public JsCallback(int requestCode, String methodName, String callbackId) {
		super();
		this.requestCode = requestCode;
		this.methodName = methodName;
		this.callbackId = callbackId;
	}

	public String getMethodName() {
		return methodName;
	}
	
	public int getRequestCode() {
		return requestCode;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}
	
	public String getCallbackId() {
		return callbackId;
	}
	
	public void setCallbackId(String callbackId) {
		this.callbackId = callbackId;
	}
}
