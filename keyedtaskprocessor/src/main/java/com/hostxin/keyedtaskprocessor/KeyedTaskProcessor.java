
package com.hostxin.keyedtaskprocessor;


import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class KeyedTaskProcessor {
    private final Object mLock = new Object();
    private ThreadPoolExecutor mPool;
    private Map<Object, WorkQueueRunnable> mTaskMap;
    private boolean started = true;

    public KeyedTaskProcessor(int corePoolSize, int maximumPoolSize,long keepAliveTime, TimeUnit timeUnit) {
        this.mTaskMap = new ConcurrentHashMap<Object, WorkQueueRunnable>();
        this.mPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, new LinkedBlockingDeque<Runnable>(), new WorkFactory("KeyedTaskProcessor"));
        this.started = true;
    }

    public void notify(Object key, WorkTask workTask) {
        if(!started){
            return;
        }
        WorkQueueRunnable workQueueRunnable = (WorkQueueRunnable)this.mTaskMap.get(key);
        if (workQueueRunnable == null) {
            workQueueRunnable = new WorkQueueRunnable(key);
            synchronized (mLock) {
                this.mTaskMap.put(key, workQueueRunnable);
            }
            workQueueRunnable.addTask(workTask);
            this.mPool.submit(workQueueRunnable);
        }else{
            workQueueRunnable.addTask(workTask);
        }
    }

    private class WorkQueueRunnable
        implements Runnable {
        private final Object key;
        private final ArrayDeque<WorkTask> taskQueue;

        public WorkQueueRunnable(Object object) {
            this.taskQueue = new ArrayDeque<WorkTask> ();
            this.key = object;
        }

        public void addTask(WorkTask workTask) {
            this.taskQueue.offer(workTask);
        }

        public void run() throws RuntimeException {
            while(started) {
                synchronized (mLock) {
                    if (taskQueue.isEmpty()) {
                        mTaskMap.remove(key);
                        return;
                    }
                }
                WorkTask workTask = (WorkTask)this.taskQueue.poll();
                try {
                    workTask.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public String toString() {
            return "WorkQueueRunnable[" + this.hashCode() + ", " + this.key + ", " + this.taskQueue.size() + "]";
        }
    }

    public void exit(){
        started = false;
        mPool.shutdown();

        mTaskMap.clear();
        mTaskMap = null;
        mPool = null;
    }

}

