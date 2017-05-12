
package com.hostxin.android.brcode.decode;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

/**
 * Decode thread
 */
final class DecodeThread extends Thread {

	private DecodeFrame frame;
    private Handler handler;
    private final CountDownLatch handlerInitLatch;

    DecodeThread(DecodeFrame frame) {
        this.frame = frame;
        handlerInitLatch = new CountDownLatch(1);
    }

    Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            // continue?
        }
        return handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new DecodeHandler(frame);
        handlerInitLatch.countDown();
        Looper.loop();
    }

}
