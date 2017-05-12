
package com.hostxin.android.brcode.decode;

import android.os.Handler;
import android.os.Message;
import com.hostxin.android.brcode.R;

import com.hostxin.android.brcode.camera.CameraManager;


/**
 * Message of scan result sender
 */
public final class QrScanHandler extends Handler {

    DecodeThread decodeThread = null;
    DecodeFrame activity = null;
    private State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public QrScanHandler(DecodeFrame activity) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity);
        decodeThread.start();
        state = State.SUCCESS;
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if(message.what == R.id.auto_focus){
            if (state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            }
        }else if(message.what == R.id.restart_preview){
            restartPreviewAndDecode();
        }else if(message.what == R.id.decode_succeeded){
            state = State.SUCCESS;
            activity.handleDecode((String) message.obj);
        }else if(message.what == R.id.decode_failed){
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        }

    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
        removeMessages(R.id.decode);
        removeMessages(R.id.auto_focus);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
    }

}
