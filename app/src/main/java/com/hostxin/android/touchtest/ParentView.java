package com.hostxin.android.touchtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by lixin on 17-6-7.
 */

public class ParentView extends ViewGroup{
    public ParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
