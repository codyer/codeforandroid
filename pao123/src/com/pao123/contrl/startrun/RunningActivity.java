package com.pao123.contrl.startrun;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;

import com.pao123.contrl.startrun.ui.IndicatorFragmentActivity;
import com.pao123.www.R;

public class RunningActivity extends IndicatorFragmentActivity {

	public static final int FRAGMENT_ONE = 0;
	public static final int FRAGMENT_TWO = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
	}

	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(FRAGMENT_ONE, getString(R.string.fragment_one),
				FragmentRunningMapView.class));
		tabs.add(new TabInfo(FRAGMENT_TWO, getString(R.string.fragment_two),
				FragmentRunningStatus.class));

		return FRAGMENT_TWO;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要结束此次运动吗？");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();
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
