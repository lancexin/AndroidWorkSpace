package com.hostxin.android.brcode.decode;

import android.content.Context;
import android.os.Handler;

public interface DecodeFrame {

	public Context getContext();
	
	public Handler getHandler();
	
	public void handleDecode(String s);

	public int getX();

	public int getY();

	public int getCropWidth();

	public int getCropHeight();

}
