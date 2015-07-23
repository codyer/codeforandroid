package com.pao123.contrl.timecount;

import com.pao123.contrl.startrun.RunningActivity;
import com.pao123.www.R;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class TimeCountActivity extends Activity implements OnClickListener {

	private TextView countNum;
	private Animation animation;
	private Animation animation2;
	private int count = 3;
	private Button btnCancelh;
	private int flag = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		// 去掉界面任务条  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  

		setContentView(R.layout.activity_time_count);
		countNum = (TextView) findViewById(R.id.count_num);
		animation = AnimationUtils.loadAnimation(this, R.anim.count_down_exit);
		animation2 = AnimationUtils.loadAnimation(this, R.anim.animation_text);
		btnCancelh = (Button) findViewById(R.id.btn_cancel);
		btnCancelh.setOnClickListener(this);
		countNum.startAnimation(animation);
		handler.sendEmptyMessageDelayed(1, 1000);
	}

	@Override
	protected void onStop() {
		super.onStop();
        this.finish();
	}

	public void small() {
		animation2.reset();
		countNum.startAnimation(animation2);
		if(count == 0)
		{
			handler.removeMessages(0);
			Intent intent = new Intent(this,RunningActivity.class); 
            startActivity(intent);
		}else {
			handler.sendEmptyMessageDelayed(0, 1000);
		}
	}

	public void big() {
		animation.reset();
		countNum.startAnimation(animation);
		if(count == 0)
		{
			handler.removeMessages(1);
			Intent intent = new Intent(this,RunningActivity.class); 
            startActivity(intent);
		}else {
			handler.sendEmptyMessageDelayed(1, 1000);
		}
	}

	private int getCount() {
		count--;
		if (count < 0) {
			count = 0;
		}
		return count;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			getCount();
			if(count > 0)
			{
				countNum.setText("" + count);
			}else {
				countNum.setText("");
			}
			if (msg.what == 0) {
				small();
			} else {
				big();
			}
		};
	};

	@Override
	public void onClick(View arg0) {
		switch (flag) {
		case 0:
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(1, 1000);
			flag = 1;
			break;
		case 1:
			handler.removeMessages(1);
			handler.sendEmptyMessageDelayed(0, 1000);
			flag = 0;
			break;

		default:
			break;
		}

	}
}
