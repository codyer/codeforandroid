package com.pao123.www.realtime;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.AMap.CancelableCallback;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.pao123.bean.Lap;
import com.pao123.bean.Location;
import com.pao123.bean.WatchMember;
import com.pao123.bean.json.GetLatestData;
import com.pao123.common.Constants;
import com.pao123.www.R;
import com.pao123.www.model.RealTimeRunManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class RealTimeMapViewActivity extends Activity implements
		OnClickListener,CancelableCallback {
	private MapView mapView;
	private AMap aMap;
	private ListView mListView;
	private RealTimeMapFriendListAdapter mAdapter;
	private ImageView mBackBtn;
	private Button mPraiseBtn;
	private LinearLayout mRightBar;
	private LinearLayout mTopBar;
	private ImageView mTopBarCloseBtn;
	private Button mRightCtrlBtn;
	private RealTimeMapService mRealTimeMapService;
	private RealTimeMapBroadcastReceiver mRealTimeMapReceiver;
	private boolean isOpened = true;
	private static boolean isZoomfinished = true;
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	private final static float weight = (float)(4.0/(7.0+4.0));
	private void closeRightBar() {
		AnimatorSet trans = new AnimatorSet();
		trans.playTogether(ObjectAnimator.ofFloat(mRightBar, "translationX",
				getScreenWidth()*weight-mRightCtrlBtn.getWidth()));
		trans.setInterpolator(AnimationUtils.loadInterpolator(RealTimeMapViewActivity.this,
				android.R.anim.decelerate_interpolator));
		trans.setDuration(250);
		trans.start();
		mTopBar.setVisibility(View.VISIBLE);
	}
	private void openRightBar() {
		AnimatorSet trans = new AnimatorSet();
		trans.playTogether(ObjectAnimator.ofFloat(mRightBar, "translationX",
				0));
		trans.setInterpolator(AnimationUtils.loadInterpolator(RealTimeMapViewActivity.this,
				android.R.anim.decelerate_interpolator));
		trans.setDuration(250);
		trans.start();
		mTopBar.setVisibility(View.GONE);
	}
	public int getScreenWidth() {
		RealTimeMapViewActivity.this.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}
	private class RealTimeMapBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String id = intent.getStringExtra("REFRESH_MEMBER");

			for (int i = 0; i < RealTimeRunManager.getInstance()
					.getmWatchMemberList().size(); i++) {
				WatchMember watchMember = RealTimeRunManager.getInstance()
						.getmWatchMemberList().get(i);
				if (watchMember.getId().equals(id)) {
					if (RealTimeRunManager.getInstance().getmMsgQueue()
							.containsKey(watchMember.getId())) {
						refreshMap(i, watchMember, id);
					}
				}
			}
			Log.e("RealTimeMapBroadcastReceiver", "REFRESH_MEMBER=" + id);
		}
	}

	/**
	 * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
		boolean animated = true;
			if (animated) {
				aMap.animateCamera(update, 1000, callback);
				isZoomfinished = false;
			} else {
				aMap.moveCamera(update);
				isZoomfinished = false;
			}			
	}

	/**
	 * 刷新地图数据，画路径
	 * 
	 * @param index
	 * @param watchMember
	 * @param id
	 *            指定的用户
	 */
	private void refreshMap(int position, WatchMember watchMember, String id) {
		List<Object> msgList = RealTimeRunManager.getInstance().getmMsgQueue()
				.get(id);
		// RealTimeRunManager.getInstance().getmMsgQueue().get(id).clear();//消费掉
		GetLatestData infoData = null;
		List<Lap> lapList = null;
		List<Location> locList = null;
		Location location = null;
		List<LatLng> points = new ArrayList<LatLng>();
		String latString = watchMember.getLastLat();
		String lngString = watchMember.getLastLng();
		if (latString != null && lngString != null) {
			points.add(new LatLng(Double.parseDouble(latString), Double
					.parseDouble(lngString)));// last location
		}
		for (int i = 0; i < msgList.size(); i++) {
			infoData = (GetLatestData) msgList.get(i);
			lapList = infoData.getLap();
			for (int j = 0; j < lapList.size(); j++) {
				locList = lapList.get(j).getLocationdata();
				for (int k = 0; k < locList.size(); k++) {
					location = locList.get(k);
					points.add(new LatLng(Double.parseDouble(location
							.getLatitude()), Double.parseDouble(location
							.getLongitude())));
				}
			}
		}
		if (location != null) {
			watchMember.setLastLat(location.getLatitude());
			watchMember.setLastLng(location.getLongitude());
		}

		if (isZoomfinished) {
		if (RealTimeRunManager.getInstance().getmCurrentPosition() == position) {
			changeCamera(
					CameraUpdateFactory.newCameraPosition(new CameraPosition(
							points.get(points.size() - 1), 18, 30, 0)), this);
		}}
		aMap.addPolyline((new PolylineOptions()).addAll(points).width(20)
				.geodesic(true).color(Constants.LineColors[position]));
		msgList.clear();
		points.clear();
	}

	private ServiceConnection mConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			mRealTimeMapService = ((RealTimeMapService.RealTimeBinder) binder)
					.getService();
			if (mRealTimeMapService != null) {
				mRealTimeMapService.StartWatch();
				// mRealTimeMapService.registerHandler(realtimeMapHandler);
				Log.e("TAG", "RealTimeMapViewActivity:ServiceConnection ...");
			} else {
				Log.e("TAG",
						"RealTimeMapViewActivity: mRealTimeMapService==null ...");
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.d("TAG", "onServiceDisconnected ...");
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉界面任务条
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.real_time_map_view);
		/*
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
		// Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mListView = (ListView) findViewById(R.id.realtime_map_running_list);
		mBackBtn = (ImageView) findViewById(R.id.realtime_right_bar_back_btn);
		mPraiseBtn = (Button) findViewById(R.id.realtime_praise_btn);
		mapView = (MapView) findViewById(R.id.realtime_map_view);
		mRightBar = (LinearLayout) findViewById(R.id.realtime_right_bar);
		mTopBar = (LinearLayout) findViewById(R.id.realtime_top_bar_layout);
		mTopBar.setVisibility(View.GONE);
		
		mTopBarCloseBtn = (ImageView) findViewById(R.id.realtime_top_bar_close_btn);
		mRightCtrlBtn = (Button) findViewById(R.id.realtime_right_ctrl_btn);
		mRightCtrlBtn.setAlpha(0.6f);
		
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		mRealTimeMapReceiver = new RealTimeMapBroadcastReceiver();

		init();
		setListeners();

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e("RealTimeMapFriendListAdapter",
				"RealTimeMapFriendListAdapter.onStart()");
		mAdapter = new RealTimeMapFriendListAdapter(
				RealTimeMapViewActivity.this);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private void setListeners() {
		mBackBtn.setOnClickListener(this);
		mPraiseBtn.setOnClickListener(this);
		mTopBarCloseBtn.setOnClickListener(this);
		mRightCtrlBtn.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mAdapter.setClickPos(position);
				mAdapter.notifyDataSetChanged();
				WatchMember watchMember = RealTimeRunManager.getInstance()
						.getmWatchMemberList().get(position);
				if (watchMember.getLastLat() != null && watchMember
						.getLastLng() != null) {
					LatLng latLng = new LatLng(Double.parseDouble(watchMember
							.getLastLat()), Double.parseDouble(watchMember
							.getLastLng()));
					changeCamera(
							CameraUpdateFactory
									.newCameraPosition(new CameraPosition(latLng,
											18, 30, 0)), null);
				}
		
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.realtime_right_bar_back_btn:
			if (mRealTimeMapService != null) {
				mRealTimeMapService.finishWatch();
			}
			RealTimeRunManager.getInstance().initForSelectMember();
			RealTimeMapViewActivity.this.finish();
			break;
		case R.id.realtime_praise_btn:
			RealTimeRunManager.getInstance().SendPraiseToAllSelectPerson();
			break;
		case R.id.realtime_top_bar_close_btn:
			mTopBar.setVisibility(View.GONE);
			break;
		case R.id.realtime_right_ctrl_btn:
			if (isOpened) {
				this.closeRightBar();
				isOpened = ! isOpened;
			}else {
				this.openRightBar();
				isOpened = ! isOpened;
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		Log.e("RealTimeMapViewActivity", "RealTimeMapViewActivity onResume");
		super.onResume();
		if (mapView != null) {
			mapView.onResume();
		}
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}

		IntentFilter filter = new IntentFilter(
				RealTimeMapService.WATCHING_STATUS_FILTER);
		registerReceiver(mRealTimeMapReceiver, filter);
		// RealTimeMapService bind
		Intent serviceIntent = new Intent(RealTimeMapViewActivity.this,
				RealTimeMapService.class);
		bindService(serviceIntent, mConn, Context.BIND_AUTO_CREATE);
		Log.e("onResume", "onResume--bindService RealTimeMapService");
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.e("RealTimeMapViewActivity", "RealTimeMapViewActivity onPause");
		if (mapView != null) {
			mapView.onPause();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mapView != null) {
			mapView.onSaveInstanceState(outState);
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		Log.e("RealTimeMapViewActivity", "RealTimeMapViewActivity onDestroy");
		if (mapView != null) {
			mapView.onDestroy();
		}
		if (mRealTimeMapService != null) {
			mRealTimeMapService.onDestroy();
		}
		unregisterReceiver(mRealTimeMapReceiver);
		super.onDestroy();
	}

	@Override
	public void finish() {
		Log.e("RealTimeMapViewActivity", "RealTimeMapViewActivity finish");
		super.finish();
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		isZoomfinished = true;
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		isZoomfinished = true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			mBackBtn.performClick();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
