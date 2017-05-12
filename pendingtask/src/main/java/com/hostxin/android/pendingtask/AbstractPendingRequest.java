package com.hostxin.android.pendingtask;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import android.os.Looper;

import com.hostxin.android.util.Assert;

public abstract class AbstractPendingRequest<R extends Result> implements
		PendingRequest<R> {
	private PendingTaskManager mPendingTaskManager;
	private ResultCallback<R> mCallback;
	private boolean mConsumed = false;
	private ResultHandler<R> mHandler;
	private boolean mIsDone;
	private CountDownLatch mLatch = new CountDownLatch(1);
	private final Object mLock = new Object();
	private R mResult;
	private String id;
	
	public static final int MAX_WAIT_TIME = 20;
	public static final TimeUnit WAIT_TIME_UNIT = TimeUnit.SECONDS;
	
	public AbstractPendingRequest(PendingTaskManager pendingTaskManager,String id) {
		this.mPendingTaskManager = pendingTaskManager;
		this.id = id;
	}
	
	public AbstractPendingRequest(PendingTaskManager pendingTaskManager) {
		this.mPendingTaskManager = pendingTaskManager;
		this.id = getClass().getName();
	}

	private R consume() {
		synchronized (this.mLock) {
			boolean bl = !(this.mConsumed);
			Assert.isTrue(bl, "Result has already been consumed.");
			Assert.isTrue(this.isReady(), "Result is not ready.");
			R var4_4 = this.mResult;
			this.clear();
			return var4_4;
		}
	}

	private void create(R var1_1) {
		this.mResult = var1_1;
		this.mLatch.countDown();
		if (this.mCallback == null)
			return;
		this.mHandler.release();
		this.mHandler.call(this.mCallback, this.consume());
	}

	static <R extends Result> void execute(
			AbstractPendingRequest<R> abstractPendingResult) {
		abstractPendingResult.timeout();
	}

	private void interrupt() {
		synchronized (this.mLock) {
			if (this.isReady())
				return;
			this.setResult(this.create(Status.STATUS_INTERRUPTED));
			this.mIsDone = true;
			return;
		}
	}
	
	public boolean isDone() {
		return mIsDone;
	}

	private void timeout() {
		synchronized (this.mLock) {
			if (this.isReady())
				return;
			this.setResult(this.create(Status.STATUS_TIMEOUT));
			this.mIsDone = true;
			return;
		}
	}
	
	public void cancel() {
		synchronized (this.mLock) {
			if (this.isReady())
				return;
			this.setResult(this.create(Status.STATUS_CANCELED));
			this.mIsDone = true;
			return;
		}
	}
	public void error(Exception e) {
		synchronized (this.mLock) {
			if (this.isReady())
				return;
			this.setResult(this.create(new Status(8, e.toString())));
			this.mIsDone = true;
			return;
		}
	}
	
	private void down() {
		synchronized (this.mLock) {
			if (this.isReady())
				return;
			this.setResult(mResult);
			this.mIsDone = true;
			return;
		}
	}

	@Override
	public final R await() {
		return this.await(5, TimeUnit.MINUTES);
	}

	@Override
	public final R await(long l, TimeUnit timeUnit) {
		boolean bl = true;
		boolean bl2 = (Looper.myLooper() != Looper.getMainLooper()) ? (bl)
				: (false);
		Assert.isTrue(bl2, "await must not be called on the UI thread");
		if (this.mConsumed) {
			bl = false;
		}
		Assert.isTrue(bl, "Result has already been consumed");
		try {
			if (!(this.mLatch.await(l, (TimeUnit) (timeUnit)))) {
				this.timeout();
			}
		} catch (InterruptedException var6_5) {
			this.interrupt();
		}
		Assert.isTrue(this.isReady(), "Result is not ready.");
		return this.consume();
	}

	protected void clear() {
		this.mConsumed = true;
		this.mResult = null;
		this.mCallback = null;
	}

	public abstract R create(Status var1);

	public final boolean isReady() {
		if (this.mLatch.getCount() != 0)
			return false;
		return true;
	}

	protected void setHandler(ResultHandler<R> paramResultHandler) {
		this.mHandler = paramResultHandler;
	}
	
	public final void setResult(R var1_1) {
		boolean bl = true;
		synchronized (this.mLock) {
			if ((this.mIsDone) ) {
				return;
			}
			boolean bl2 = (!(this.isReady())) ? (bl) : (false);
			Assert.isTrue(bl2, "Results have already been set");
			if (this.mConsumed) {
				bl = false;
			}
			Assert.isTrue(bl, "Result has already been consumed");
			this.create(var1_1);
			mPendingTaskManager.removeById(getId());
			return;
		}
	}

	@Override
	public final void setResultCallback(ResultCallback<R> resultCallback) {

		boolean bl = !(this.mConsumed);
		Assert.isTrue(bl, "Result has already been consumed.");
		synchronized (this.mLock) {
			if (this.isReady()) {
				this.mHandler.call(resultCallback, this.consume());
			}
		}
		this.mCallback = resultCallback;
	}

	@Override
	public void setResultCallback(ResultCallback<R> resultCallback,
			long paramLong, TimeUnit paramTimeUnit) {
		synchronized (this.mLock) {
			if (isReady()) {
				this.mHandler.call(resultCallback, this.consume());
			} else {
				this.mCallback = resultCallback;
				this.mHandler.call(this, paramTimeUnit.toMillis(paramLong));
			}
		}
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	public abstract void doRequest();
	
	@Override
	public void run() {
		synchronized (mLock) {
			if(mConsumed){
				return;
			}
		}
		try {
			doRequest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 error(e);
			 return;
		}
		try {
			mLatch.await(MAX_WAIT_TIME,WAIT_TIME_UNIT);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			interrupt();
		}
		if(mResult != null){
			down();
		}else{
			timeout();
		}
	}
}