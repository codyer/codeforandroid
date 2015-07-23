package com.pao123.www.realtime;

import java.util.List;

import com.pao123.bean.WatchMember;
import com.pao123.contrl.menu.ui.ResideMenu;
import com.pao123.www.R;
import com.pao123.www.model.RealTimeRunManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RealTimeRuningSelectActivity extends Activity {

	private ListView mListView;
	private RealTimeRunningAdapter mAdapter;
	private ImageView mBackBtn;
	private Button mWatchBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉界面任务条
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.real_time_running_select);
		mListView = (ListView)findViewById(R.id.realtime_running_select_list);
		mBackBtn = (ImageView)findViewById(R.id.realtime_running_select_back_btn);
		mWatchBtn = (Button)findViewById(R.id.realtime_watch_btn);
		RealTimeRunManager.getInstance().initForSelectWatchPerson();
	}
	@Override
	public void onStart() {
		super.onStart();
		Log.e("RealTimeRuningSelectActivity", "RealTimeRuningSelectActivity.onStart()");
		mAdapter = new RealTimeRunningAdapter(RealTimeRuningSelectActivity.this);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				mAdapter.setClickPos(position);
				mAdapter.notifyDataSetChanged();
			}

		});
		mBackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RealTimeRunManager.getInstance().initForSelectGroupOrPerson();
				onDestroy();
			}
		});
		mWatchBtn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				// TODO 观看地图画面各个成员的位置,到地图画面
				List<WatchMember> lfFriends =  RealTimeRunManager.getInstance().getmWatchMemberList();
				String string = "mWatch list = ";
				for (int i = 0; i < lfFriends.size(); i++) {
					string += (lfFriends.get(i)).getNickname();
				}
				Toast.makeText(RealTimeRuningSelectActivity.this, string, Toast.LENGTH_LONG).show();
				Intent intent = new Intent(RealTimeRuningSelectActivity.this,RealTimeMapViewActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
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
