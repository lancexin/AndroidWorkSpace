package com.hostxin.android.mvp;


import rx.Scheduler;
import rx.schedulers.Schedulers;

public class ImmediateSchedulerProvider implements BaseSchedulerProvider {

    @Override
    public Scheduler computation() {
        return Schedulers.immediate();
    }

    @Override
    public Scheduler io() {
        return Schedulers.immediate();
    }

    @Override
    public Scheduler ui() {
        return Schedulers.immediate();
    }
}
