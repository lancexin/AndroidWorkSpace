package com.hostxin.android.jsmethod;

import android.content.Intent;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

public class JsMethodManager {

	private BaseActivityBrowser activity;

	private MeWebView webView;

	private Map<String, JsMethod> methodMap = new HashMap<String, JsMethod>();

	private SparseArray<JsCallback> callbackMap = new SparseArray<JsCallback>();

	public JsMethodManager(BaseActivityBrowser activity, MeWebView webView) {
		this.activity = activity;
		this.webView = webView;


		CloseBrowser closeBrowser = new CloseBrowser(this);
		addMethod(closeBrowser.getMethodName(), closeBrowser);
	}

	public JsMethod findMethodByName(String name) {
		return methodMap.get(name);
	}

	public BaseActivityBrowser getActivity() {
		return activity;
	}

	public void execMethod(String methodName, String params, String callbackId) {
		JsMethod method = findMethodByName(methodName);
		if (method != null) {
			try {
				method.exec(callbackId, params);
			} catch (JsExctption e) {
				methodError(callbackId, e.getMessage());
				e.printStackTrace();
			}
		}
	}

	protected void methodCallback(String callbackId, Object o) {
		getWebView().nativeCallback(callbackId, o);
	}

	private void methodError(String callbackId, String error) {
		getWebView().nativeCallError(callbackId, error);
	}

	public void addMethod(String name, JsMethod method) {
		methodMap.put(name, method);
	}

	public MeWebView getWebView() {
		return webView;
	}

	public void addJsCallback(int requestCode, JsCallback callback) {
		callbackMap.put(requestCode, callback);
	}

	public JsCallback getJsCallback(int requestCode) {
		JsCallback callback = callbackMap.get(requestCode);
		callbackMap.remove(requestCode);
		return callback;
	}

	public void handleActivityResult(int requestCode, int resultCode,
			Intent data) {
		JsCallback callback = getJsCallback(requestCode);
		JsMethod method = findMethodByName(callback.getMethodName());
		if (method != null) {
			try {
				Object v = method.handleCallback(resultCode, data);
				if (v != null) {
					methodCallback(callback.getCallbackId(), v);
				}
			} catch (JsExctption e) {
				methodError(callback.getCallbackId(), e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void cleanCallback() {
		callbackMap.clear();
	}
}
