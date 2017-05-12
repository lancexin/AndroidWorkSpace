package com.hostxin.android.jsmethod;


import android.app.Activity;
import android.content.Intent;

public abstract class JsMethod {
	
	private JsMethodManager manager;
	private String methodName;
	
	private boolean hasCallback;
	
	public JsMethod(JsMethodManager manager) {
		this.manager = manager;
	}
	
	public abstract void exec(String callbackId,String params)throws JsExctption ;
	
	public Object handleCallback(int resultCode,Intent intent)throws JsExctption{
		throw new JsExctption("not support callback");
	}
	
	public JsCallback newCallback(String callbackId){
		return new JsCallback(getRequestCode(), getMethodName(), callbackId);
	}
	
	public int addNewCallback(String callbackId){
		JsCallback callback = newCallback(callbackId);
		manager.addJsCallback(callback.getRequestCode(), callback);
		return callback.getRequestCode();
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public JsMethodManager getManager() {
		return manager;
	}
	
	public int getRequestCode() {
		return Math.abs((int)System.currentTimeMillis());
	}

	public boolean isHasCallback() {
		return hasCallback;
	}
	
	public void setHasCallback(boolean hasCallback) {
		this.hasCallback = hasCallback;
	}
	
	public Activity getActivity(){
		return getManager().getActivity();
	}
}
