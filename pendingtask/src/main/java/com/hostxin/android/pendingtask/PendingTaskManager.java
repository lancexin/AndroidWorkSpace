package com.hostxin.android.pendingtask;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.HandlerThread;


public class PendingTaskManager {
	

	private  final String TAG = "AsyncResultManager";
	
	private  ExecutorService executors = Executors.newCachedThreadPool();
	
	private  Map<String, WeakReference<AbstractPendingRequest>> pendingResults = new ConcurrentHashMap<String, WeakReference<AbstractPendingRequest>>();
	
	private HandlerThread mHandlerThread;
	
	public void init(Context mContext){
		mHandlerThread = new HandlerThread(TAG);
		mHandlerThread.start();
	}
	
	public  void removeById(String id){
		pendingResults.remove(id);
	}

	public boolean hasId(String id){
		return pendingResults.containsKey(id);
	}
	
	public  <R extends Result> void setResult(String id,R result){
		WeakReference<AbstractPendingRequest> softReference = pendingResults.get(id);
		AbstractPendingRequest pendingResult = null;
		if (softReference != null) {
			pendingResult = softReference.get();
		}
		if(pendingResult == null){
			return;
		}
		if(pendingResult.isDone()){
			return;
		}
		pendingResult.setResult(result);
		removeById(id);
	}
	
	public  <R extends Result>  AbstractPendingRequest<R> execute(AbstractPendingRequest<R> pendingResult){
		WeakReference<AbstractPendingRequest> softReference = pendingResults
				.get(pendingResult.getId());
		AbstractPendingRequest result = null;
		if (softReference != null) {
			result = softReference.get();
			if(result != null){
				if(!result.isDone()){
					result.cancel();
				}
				removeById(pendingResult.getId());
			}	
		}

		pendingResult.setHandler(new ResultHandler<R>(mHandlerThread.getLooper()));
		pendingResults.put(pendingResult.getId(), new WeakReference<AbstractPendingRequest>(pendingResult));
		executors.execute(pendingResult);
		return pendingResult;
	}
}
