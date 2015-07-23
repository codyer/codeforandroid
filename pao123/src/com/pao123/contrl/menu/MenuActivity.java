package com.pao123.contrl.menu;

import android.R.color;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.Toast;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.pao123.contrl.menu.ui.ResideMenu;
import com.pao123.contrl.startrun.StartRunFragment;
import com.pao123.www.R;
import com.pao123.www.login.LoginActivity;
import com.pao123.www.model.LoginManager;
import com.pao123.www.realtime.RealTimeFragment;

public class MenuActivity extends FragmentActivity implements
		View.OnClickListener {

	private ResideMenu resideMenu;
	// private MenuActivity mContext;

	private TextView realTimeSwitchBtn;

	private LinearLayout mbackGroud;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉界面任务条
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		mbackGroud = (LinearLayout)findViewById(R.id.main_linear);
		mbackGroud.setBackgroundResource(R.drawable.bg_start);
		realTimeSwitchBtn = (TextView) findViewById(R.id.title_bar_right_menu);
		// mContext = this;
		setUpMenu();
		if (savedInstanceState == null)
			changeFragment(new StartRunFragment());
	}

	private void setUpMenu() {

		// attach to currenitemRealTimet activity;
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.menu_bg);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		resideMenu.setTransScaleValue(0.8f);
		resideMenu.addMenuItems(MenuActivity.this);
		// You can disable a direction by setting ->
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		findViewById(R.id.title_bar_left_menu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
					}
				});
		realTimeSwitchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				switchRealShow();
			}
		});
	}

	static boolean switchFlag = true;

	protected void switchRealShow() {
		if (switchFlag) {
			realTimeSwitchBtn.setTextColor(color.darker_gray);
		} else {
			realTimeSwitchBtn.setTextColor(color.holo_orange_light);
		}
		switchFlag = !switchFlag;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.menu_profile_item:
			changeFragment(new ProfileFragment());
			break;
		case R.id.menu_startrun_item:
			changeFragment(new StartRunFragment());
			mbackGroud.setBackgroundResource(R.drawable.bg_start);
			break;
		case R.id.menu_history_item:
			changeFragment(new HistoryFragment());
			break;
		case R.id.menu_realtime_item:
			changeFragment(new RealTimeFragment());
			mbackGroud.setBackgroundResource(R.drawable.list_bg);
			break;
		case R.id.menu_friends_item:
			changeFragment(new FriendsFragment());
			mbackGroud.setBackgroundResource(R.drawable.list_bg);
			break;
		case R.id.menu_group_item:
			changeFragment(new GroupFragment());
			break;
		case R.id.menu_settings_item:
			changeFragment(new SettingsFragment());
			break;
		default:
			break;
		}
		resideMenu.closeMenu();
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
			// Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT)
			// .show();
		}

		@Override
		public void closeMenu() {
			// Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT)
			// .show();
		}
	};

	private long firClick;

	private long secClick;

	private static int count = 0;

	private void changeFragment(Fragment targetFragment) {
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	// What good method is to access resideMenu�?
	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			count++;
			if (count == 1) {
				firClick = System.currentTimeMillis();
			} else if (count == 2) {
				secClick = System.currentTimeMillis();
				if (secClick - firClick < 1000) {
					// 创建退出对话框
					AlertDialog isExit = new AlertDialog.Builder(this).create();
					// 设置对话框标题
					isExit.setTitle("系统提示");
					// 设置对话框消息
					isExit.setMessage("确定要注销账号吗？");
					// 添加选择按钮并注册监听
					isExit.setButton("确定", listener);
					isExit.setButton2("取消", listener);
					// 显示对话框
					isExit.show();
					count = 0;
					firClick = 0;
					secClick = 0;
					return true;
				}
				count = 0;
				firClick = 0;
				secClick = 0;
			}
			if (resideMenu.isOpened()) {
				resideMenu.closeMenu();				
			}else {
				resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);				
			}
			return true;
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				LoginManager.getInstance().LogoutCurrentUser(MenuActivity.this);
				Intent intent = new Intent(MenuActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};
}
