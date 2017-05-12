
package com.hostxin.android.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.hostxin.android.util.Dbg;
import com.hostxin.android.util.NetworkUtils;

import java.util.ListIterator;
import java.util.Vector;

/**
 * 
 * @author lixin
 * @since 2014-8-1
 * 用于监控网络改变的代理
 */
public class NetworkReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkReceiver";

    private static final Vector<NetworkWatcher> watchers = new Vector<NetworkWatcher>();
    
    public static interface NetworkWatcher {
        public void onNetChange(boolean isOn);
    }

    public static void addWatcher(NetworkWatcher watcher) {
        synchronized (watchers) {
            watchers.add(watcher);
        }
    }

    public static void removeWatcher(NetworkWatcher watcher) {
        synchronized (watchers) {
            watchers.remove(watcher);
        }
    }
   
    @Override
    public void onReceive(Context context, Intent intent) {

        boolean success = NetworkUtils.isNetworkConnected(context);
        Dbg.d(TAG,"onReceive success:"+success);
        // TODO Auto-generated method stub
        synchronized (watchers) {
            ListIterator<NetworkWatcher> lte = watchers.listIterator();
            while (lte.hasNext()) {
                lte.next().onNetChange(success);
            }
        }
    }
}
