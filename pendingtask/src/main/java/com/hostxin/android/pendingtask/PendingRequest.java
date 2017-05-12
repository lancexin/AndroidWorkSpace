package com.hostxin.android.pendingtask;


import java.util.concurrent.TimeUnit;

public interface PendingRequest<R extends Result> extends Runnable{
    public R await();

    public R await(long var1, TimeUnit var3);

    public void setResultCallback(ResultCallback<R> var1);
    
    public void setResultCallback(ResultCallback<R> var1, long var2, TimeUnit var4);
	
	public static interface ResultCallback<R extends Result> {
	    public void onResult(R var1);
	}

	public String getId();
}

