package com.hostxin.keyedtaskprocessor;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



public class KeyedTaskProcessorTest {

    private final int WQ_MAX = Math.max(this.WQ_SIZE, 10);
    private final int WQ_SIZE = Runtime.getRuntime().availableProcessors();
    private final int WQ_ALIVE_TIME = 30;//seconds

    private  KeyedTaskProcessor mKeyedTaskProcessor;

    public static void main(String args[]){
        KeyedTaskProcessorTest test = new KeyedTaskProcessorTest();
        test.init();
        test.test();
    }

    public void init(){
        System.out.println("init");
        mKeyedTaskProcessor = new KeyedTaskProcessor(this.WQ_SIZE, this.WQ_MAX, this.WQ_ALIVE_TIME,TimeUnit.SECONDS);
    }

    public void test(){
        int size = 10;
        int keyCount = 0;
        final CountDownLatch mLatch = new CountDownLatch(size);
        for(int i=0;i<10;i+=2){
            keyCount++;
            mKeyedTaskProcessor.notify("key"+keyCount, new WorkTask() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("task1 "+Thread.currentThread().getName());
                    mLatch.countDown();
                }
            });

            mKeyedTaskProcessor.notify("key"+keyCount, new WorkTask() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("task2 "+Thread.currentThread().getName());
                    mLatch.countDown();
                }
            });
        }
        try {
            mLatch.await();
            System.out.println("exit");
            mKeyedTaskProcessor.exit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}