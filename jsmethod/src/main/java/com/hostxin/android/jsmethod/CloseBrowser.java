package com.hostxin.android.jsmethod;


public class CloseBrowser extends JsMethod {

	public CloseBrowser(JsMethodManager manager) {
		super(manager);
		setMethodName("closeBrowser");
		setHasCallback(false);
	}

	@Override
	public void exec(String callbackId, String data) throws JsExctption {
		getActivity().finish();
	}
}
