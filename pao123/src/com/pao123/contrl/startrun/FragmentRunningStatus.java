package com.pao123.contrl.startrun;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.pao123.contrl.startrun.ui.CircleProgressView;
import com.pao123.contrl.startrun.ui.CircleProgressView.onProgressListener;
import com.pao123.www.R;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

//import android.widget.Toast;

public class FragmentRunningStatus extends Fragment implements iWorkOutStatus {

	private CircleProgressView progressView;
	protected View mMainView;
	private Button long_press_to_pause;

	// for animation

	protected View mFinish_btn_layout;
	protected View mContinue_btn_layout;
	protected View mRunning_btn_layout;

	protected View mRunning_page;
	protected View mPaused_page;
	// private CircleProgressView progress_continue_View;
	private CircleProgressView progress_finish_View;
	private Button finish_btn;
	private Button continue_btn;
	private int width;
	private TextView running_time;
	private TextView pause_time;
	private RunningService mStatusService;
	private StatusBroadcastReceiver mStatusReceiver;

	private class StatusBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			long time = intent.getLongExtra("RUNNING_TIME",0);
//			Log.e("StatusBroadcastReceiver", "onReceive:"+time+"->"+FormatMiss(time));
			running_time.setText(FormatMiss(time));
			pause_time.setText(FormatMiss(time));
		}
	}

	private ServiceConnection mConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			mStatusService = ((RunningService.RunningBinder) binder)
					.getService();
			if (mStatusService != null) {
				mStatusService.StartRun();		
				Log.e("TAG", "ServiceConnection ...");		
			}else {
				Log.e("TAG", "mStatusService==null ...");				
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.d("TAG", "onServiceDisconnected ...");
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.e("onCreate", "onCreate");
		super.onCreate(savedInstanceState);
		mStatusReceiver = new StatusBroadcastReceiver();

	}

	@Override
	public void onPause() {
		Log.e("onPause", "onPause");
		getActivity().unregisterReceiver(mStatusReceiver);
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(
				RunningService.RUNNING_STATUS_FILTER);
		getActivity().registerReceiver(mStatusReceiver, filter);
		Intent serviceIntent = new Intent(getActivity(), RunningService.class);
		getActivity().getApplicationContext().bindService(serviceIntent, mConn,
				Context.BIND_AUTO_CREATE);
		Log.e("onResume", "onResume--bindService");
	}

	@Override
	public void onDestroy() {
		Log.e("onDestroy", "onDestroy");
		mStatusService.onDestroy();
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.runningandpaused, container,
				false);
		if (mMainView == null) {
			return mMainView;
		}
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		initPageRunning();
		initPagePaused();
//		setTime();
		Log.e("onCreateView", "onCreateView==width" + width);
		return mMainView;
	}

	public static String FormatMiss(long miss) {
		String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
		String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0"
				+ (miss % 3600) / 60;
		String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0"
				+ (miss % 3600) % 60;
		return hh + ":" + mm + ":" + ss;
	}

	private void initPageRunning() {
		long_press_to_pause = (Button) mMainView
				.findViewById(R.id.btn_long_press_to_pause);
		progressView = (CircleProgressView) mMainView
				.findViewById(R.id.progressbar);
		running_time = (TextView) mMainView
				.findViewById(R.id.str_running_time);
		mRunning_btn_layout = mMainView.findViewById(R.id.paused_btn_layout);
		mRunning_page = mMainView.findViewById(R.id.running_page);
		progressView.setOnProgressListener(new onProgressListener() {

			@Override
			public void onEnd() {
				if (mStatusService != null) {
					mStatusService.pauseRun();
				}
				progressView.released();
				progressView.setProgress(0);
				pauseAnimation();
				// Toast.makeText(getActivity(), "go to paused",
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onInit() {
				// Toast.makeText(getActivity(), "长按暂停",
				// Toast.LENGTH_SHORT).show();
			}

		});
		long_press_to_pause.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.i("onTouchEvent", "ACTION_DOWN");
					progressView.pressed();
					break;
				case MotionEvent.ACTION_UP:
					Log.i("onTouchEvent", "ACTION_UP");
					progressView.released();
					break;
				default:
					Log.i("onTouchEvent", "default");
					break;
				}
				return false;
			}
		});
	}

	private void initPagePaused() {

		mPaused_page = mMainView.findViewById(R.id.paused_page);
		mFinish_btn_layout = mMainView.findViewById(R.id.finish_btn_layout);
		mContinue_btn_layout = mMainView.findViewById(R.id.continue_btn_layout);
		finish_btn = (Button) mMainView.findViewById(R.id.btn_finish);
		continue_btn = (Button) mMainView.findViewById(R.id.btn_continue);
		pause_time = (TextView) mMainView.findViewById(R.id.str_pause_time);
		// progress_continue_View = (CircleProgressView) mMainView
		// .findViewById(R.id.progressbar_continue);
		progress_finish_View = (CircleProgressView) mMainView
				.findViewById(R.id.progressbar_finish);
		progress_finish_View.setOnProgressListener(new onProgressListener() {

			@Override
			public void onEnd() {
				if (mStatusService != null) {
					mStatusService.finishRun();
				}
				progress_finish_View.released();
				progress_finish_View.setProgress(0);
				continueAnimation();
				// Toast.makeText(getActivity(), "go to paused",
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onInit() {
				// Toast.makeText(getActivity(), "长按暂停",
				// Toast.LENGTH_SHORT).show();
			}

		});
		finish_btn.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.i("onTouchEvent", "ACTION_DOWN");
					progress_finish_View.pressed();
					break;
				case MotionEvent.ACTION_UP:
					Log.i("onTouchEvent", "ACTION_UP");
					progress_finish_View.released();

					break;
				default:
					Log.i("onTouchEvent", "default");
					break;
				}
				return false;
			}
		});
		continue_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mStatusService != null) {
					mStatusService.continueRun();
				}
				continueAnimation();
				// running_time.setBase(miss);
//				running_time.start();
				// Toast.makeText(getActivity(), "go to continue",
				// Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void pauseAnimation() {
		AnimatorSet set = new AnimatorSet();
		AnimatorSet set1 = new AnimatorSet();
		fadeAnimation(mRunning_page, 1, 0, 500, 0);
		fadeAnimation(mPaused_page, 0, 1, 800, 200);
		set.playTogether(ObjectAnimator.ofFloat(mFinish_btn_layout,
				"translationX", 0, -width / 4), ObjectAnimator.ofFloat(
				mFinish_btn_layout, "alpha", 0, 1), ObjectAnimator.ofFloat(
				mFinish_btn_layout, "scaleY", 1.5f, 1f), ObjectAnimator
				.ofFloat(mFinish_btn_layout, "scaleX", 1.5f, 1f));
		set1.playTogether(ObjectAnimator.ofFloat(mContinue_btn_layout,
				"translationX", 0, width / 4), ObjectAnimator.ofFloat(
				mContinue_btn_layout, "alpha", 0, 1), ObjectAnimator.ofFloat(
				mContinue_btn_layout, "scaleY", 1.5f, 1f), ObjectAnimator
				.ofFloat(mContinue_btn_layout, "scaleX", 1.5f, 1f));
		set.setStartDelay(0);
		set1.setStartDelay(0);
		set.setDuration(1000).start();
		set1.setDuration(1000).start();
		mPaused_page.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				mRunning_page.setVisibility(View.GONE);
			}
		}, 500);
	}

	private void continueAnimation() {
		AnimatorSet set = new AnimatorSet();
		AnimatorSet set1 = new AnimatorSet();
		fadeAnimation(mRunning_page, 0, 1, 800, 200);
		fadeAnimation(mPaused_page, 1, 0, 500, 0);
		set.playTogether(ObjectAnimator.ofFloat(mFinish_btn_layout,
				"translationX", -width / 4, 0), ObjectAnimator.ofFloat(
				mFinish_btn_layout, "alpha", 1, 0), ObjectAnimator.ofFloat(
				mFinish_btn_layout, "scaleY", 1f, 1.5f), ObjectAnimator
				.ofFloat(mFinish_btn_layout, "scaleX", 1f, 1.5f));
		set1.playTogether(ObjectAnimator.ofFloat(mContinue_btn_layout,
				"translationX", width / 4, 0), ObjectAnimator.ofFloat(
				mContinue_btn_layout, "alpha", 1, 0), ObjectAnimator.ofFloat(
				mContinue_btn_layout, "scaleY", 1f, 1.5f), ObjectAnimator
				.ofFloat(mContinue_btn_layout, "scaleX", 1f, 1.5f));
		set.setStartDelay(0);
		set1.setStartDelay(0);
		set.setDuration(1000).start();
		set1.setDuration(1000).start();
		mRunning_page.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				mPaused_page.setVisibility(View.GONE);
			}
		}, 500);
	}

	private void fadeAnimation(final View view, final float from,
			final float to, final long durationMillis, final long delayMillis) {
		AlphaAnimation animation = new AlphaAnimation(from, to);
		// animation.setInterpolator(new OvershootInterpolator());
		animation.setDuration(durationMillis);
		animation.setStartOffset(delayMillis);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.clearAnimation();
				Log.e("fadeAnimation", "onAnimationEnd");
			}
		});
		view.startAnimation(animation);
	}

}
