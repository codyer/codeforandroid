package com.pao123.contrl.startrun;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class RunningService extends Service implements Runnable {

	private static Lock mLock = new ReentrantLock();
	public static boolean isRun = false;
	public static long mRunningTime = 0;
	private iWorkOutStatus mInterface;
	public static final String RUNNING_STATUS_FILTER = "RunningService.Broadcast.status";

	@Override
	public void onCreate() {
		super.onCreate();
		new Thread(this).start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public void registerFilter(iWorkOutStatus inter) {
		mInterface = inter;
	}

	private IBinder binder = new RunningBinder();

	public class RunningBinder extends Binder {
		public RunningService getService() {
			return RunningService.this;
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(isRun)
			{
				mLock.lock();
				mRunningTime++;
				Intent intent = new Intent(RUNNING_STATUS_FILTER);
				intent.putExtra("RUNNING_TIME", mRunningTime);
				sendBroadcast(intent);
				mLock.unlock();
			}
		}
	}
	public void StartRun() {
		isRun = true;
		mRunningTime = 0;
	}
	public void pauseRun() {
		isRun = false;
	}
	public void continueRun() {
		isRun = true;
	}
	public void finishRun() {
		isRun = false;
	}
}
