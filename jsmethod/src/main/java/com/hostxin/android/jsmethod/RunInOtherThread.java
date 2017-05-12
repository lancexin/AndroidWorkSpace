package com.hostxin.android.jsmethod;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 基于handler消息发送机制，的非主线程，单线程队列执行工具类
 * @author cindy
 *
 */
public class RunInOtherThread {
    private static final String LOG_TAG = "RunInOtherThread";
    
    private LooperThread localThread = new LooperThread();
    
    private boolean isRunning = false;

    public Handler getHandler(){
    	return localThread.getHandler();
    }
    
    private class LooperThread extends Thread {
        private Handler mHandler;

        public void run() {
            Looper.prepare();
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                	onReceiveMessage(msg.what);
                }
            };
           
            isRunning = true;
            Looper.loop();
        }
        
        Handler getHandler(){
        	return mHandler;
        }
        
        public Looper getLooper(){
        	return Looper.myLooper();
        }
   
    }
    
    public void start(){
    	localThread.start();
    	
    	while(!isRunning()){
    		
    	}
    }
    
    public void quit(){
    	Looper.myLooper().quit();
    	isRunning = false;
    }
    
    public boolean isRunning(){
    	return isRunning;
    }
    
    public void sendMessage(int what){
    	getHandler().sendEmptyMessage(what);
    }
    
    public void onReceiveMessage(int what){};
     
}
