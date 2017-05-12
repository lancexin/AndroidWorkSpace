package com.hostxin.android.jsmethod;

public class JsExctption extends Exception{

	private static final long serialVersionUID = -9085528165720949948L;
	public JsExctption(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}
	
	public static final JsExctption CANCEL = new JsExctption("cancel");
	public static final JsExctption EMPTY = new JsExctption("empty");
}
