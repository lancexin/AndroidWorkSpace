package com.hostxin.android.jsmethod;


import android.webkit.JavascriptInterface;

import com.hostxin.android.util.MD5;


public class MeJsApi {
	JsMethodManager manager;

	public MeJsApi(JsMethodManager manager) {
		this.manager = manager;
	}

	@JavascriptInterface
	public void exec(String methodName, String params, String callbackId) {

		
		manager.execMethod(methodName, params, callbackId);
	}
		
	@JavascriptInterface
	public String newCallbackId(){
		return MD5.getMD5(System.currentTimeMillis()+"");
	}
}
