package com.hostxin.android.mvp;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

public abstract class AbsBasePresenter<V> implements IBasePresenter<V>{

    private List<V> mViewList = new ArrayList<>();

    private CompositeSubscription mSubscriptions;

    public AbsBasePresenter(){
        mSubscriptions = new CompositeSubscription();
    }

    public synchronized void addView(V v){
        synchronized (mViewList){
            if(!mViewList.contains(v)){
                mViewList.add(v);
            }
        }
    }
    public synchronized void removeView(V v){
        synchronized (mViewList){
            if(mViewList.contains(v)){
                mViewList.remove(v);
            }
        }
    }

    public List<V> getViewList() {
        return mViewList;
    }

    public List<V> newViewList() {
        return new ArrayList(mViewList);
    }

    public CompositeSubscription getSubscriptions() {
        return mSubscriptions;
    }
}
