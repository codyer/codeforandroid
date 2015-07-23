package com.pao123.www.realtime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.pao123.bean.Lap;
import com.pao123.bean.Location;
import com.pao123.bean.WatchMember;
import com.pao123.bean.json.GetLatestData;
import com.pao123.common.Constants;
import com.pao123.common.PaoSdk;
import com.pao123.www.model.RealTimeRunManager;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class RealTimeMapService extends Service {

	public static boolean isWatching = false;// 是否正在观看
	private static long mWatchTime = 0;
	public static final String WATCHING_STATUS_FILTER = "RealTimeMapService.Broadcast.status";
	private IBinder binder = new RealTimeBinder();
	private volatile boolean running;
	private Thread loopThread;
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

	@Override
	public void onCreate() {
		super.onCreate();
		loopThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (running) {
					try {
						Thread.sleep(1000);
						mWatchTime++;
						// 每一秒发送一次实时位置请求
						getLastLocationList();
					} catch (InterruptedException e) {
						// do nothing
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}

		}, "RealTimeMapService");
	}

	@Override
	public void onDestroy() {
		running = false;
		if (loopThread != null) {
		loopThread.interrupt();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public class RealTimeBinder extends Binder {
		public RealTimeMapService getService() {
			return RealTimeMapService.this;
		}
	}

	private void getLastLocationList() throws ParseException {

		int size = RealTimeRunManager.getInstance().getmWatchMemberList()
				.size();
		for (int i = 0; i < size; i++) {
			WatchMember wMember = RealTimeRunManager.getInstance()
					.getmWatchMemberList().get(i);
			final String id = wMember.getId();
			PaoSdk sdk = new PaoSdk(Constants.URL_PULL_REALTIME_WORKOUT + id);
			sdk.setParameter("starttime", wMember.getLastTime());
			GetLatestData info = sdk.postObject(GetLatestData.class);
			if (info != null) {
				Lap lastLap = info.getLap().get(info.getLap().size()-1);
				Location lastLocation = lastLap.getLocationdata().get(lastLap.getLocationdata().size()-1);
				Date date = format.parse(lastLap.getStarttime());
				date = new Date(date.getTime()+(Long.parseLong(lastLocation.getTimeoffset())*1000));
				wMember.setLastTime(format.format(date));
//				wMember.set
				insertLocationChangedDataIntoList(id, info);
			}
		}
	}

	protected void insertLocationChangedDataIntoList(String id, GetLatestData info) {
		// TODO Auto-generated method stub
		// 1、把消息放到对应ID的消息列表，等待地图画面取走数据
		RealTimeRunManager.getInstance().getmMsgQueue().get(id).add(info);
		// 2、通知地图画面更新位置，发送广播
		Intent intent = new Intent(WATCHING_STATUS_FILTER);
		intent.putExtra("REFRESH_MEMBER", id);//发送需要刷新的用户id
		sendBroadcast(intent);
	}

	public void StartWatch() {
		isWatching = true;
		running = true;
		if (loopThread != null) {
			loopThread.start();
		}
	}

	public void finishWatch() {
		isWatching = false;
		running = false;
		if (loopThread != null) {
		loopThread.interrupt();}
	}

}
