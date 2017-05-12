package com.hostxin.android.pendingtask;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;

public class ResultHandler<R extends Result> extends Handler {
	public ResultHandler() {
		this(Looper.getMainLooper());
	}

	public ResultHandler(Looper looper) {
		super(looper);
	}

	public boolean call(PendingRequest.ResultCallback<R> resultCallback, R var2_2) {
		return this.sendMessage(this.obtainMessage(1,
				new Pair<PendingRequest.ResultCallback<R>, R>(resultCallback, var2_2)));
	}

	public void call(AbstractPendingRequest<R> parama, long paramLong) {
		sendMessageDelayed(obtainMessage(4, parama), paramLong);
	}

	public void handleMessage(Message message) {
		switch (message.what) {
		default: {
			return;
		}
		case 1: {
			Pair<PendingRequest.ResultCallback<R>, R> pair = (Pair<PendingRequest.ResultCallback<R>, R>) message.obj;
			this.onResult(pair.first, pair.second);
			return;
		}
		case 4: {
			AbstractPendingRequest.execute((AbstractPendingRequest) message.obj);
			return;
		}
		}
	}

	protected void onResult(PendingRequest.ResultCallback<R> resultCallback, R var2_1) {
		try {
			resultCallback.onResult(var2_1);
			return;
		} catch (RuntimeException var3_2) {
			throw var3_2;
		}
	}

	public void release() {
		this.removeMessages(4);
	}
}