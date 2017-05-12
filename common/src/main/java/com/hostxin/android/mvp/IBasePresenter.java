package com.hostxin.android.mvp;

/**
 * Created by lixin on 17-3-27.
 */

public interface IBasePresenter<V> {
    void addView(V v);
    void removeView(V v);
}
