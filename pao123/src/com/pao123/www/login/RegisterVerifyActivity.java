package com.pao123.www.login;

import com.pao123.common.Constants;
import com.pao123.contrl.menu.MenuActivity;
import com.pao123.www.R;
import com.pao123.www.model.LoginManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterVerifyActivity extends Activity implements OnClickListener {

	private EditText mVerifyNumber;
	private int countTimeNum;
	private String phoneNum;
	private TextView mVerifyInfo;
	private Button mGetVerifyNumBtn;
	private Button mVerifyNumBackBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉界面任务条
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_register_verify);
		initViews();
		setListeners();
		// 新页面接收数据
		Bundle bundle = this.getIntent().getExtras();
		// 接收name值
		phoneNum = bundle.getString("PHONENUM");

		// 2. 通过sdk发送短信验证
		LoginManager.getInstance().LoginGetVerifyNum(this,
				registerVerifyhandler, phoneNum);
		startCountDown60s();
	}

	@SuppressLint("HandlerLeak")
	public Handler registerVerifyhandler = new Handler() {
		public void handleMessage(Message msg) {
			Intent intent;
			switch (msg.what) {
			case Constants.LOGIN_SUCCESS_MSG_ID:
				// LodingView.getInstance().showLoadingInfo(
				// RegisterVerifyActivity.this,
				// RegisterVerifyActivity.this.getApplicationContext(),
				// false);
				intent = new Intent(RegisterVerifyActivity.this,
						MenuActivity.class);
				startActivity(intent);
				break;
			case Constants.LOGIN_FAILED_MSG_ID:
				// LodingView.getInstance().showLoadingInfo(
				// RegisterVerifyActivity.this,
				// RegisterVerifyActivity.this.getApplicationContext(),
				// false);
				Toast.makeText(getApplicationContext(), "登录失败",
						Toast.LENGTH_SHORT).show();
				break;
			case Constants.SEND_SMS_COUNTDOWN_MSG_CONTINUE_ID:
				mGetVerifyNumBtn.setText("重新发送(" + countTimeNum + ")");
				break;
			case Constants.SEND_SMS_COUNTDOWN_MSG_FINISHED_ID:
				mGetVerifyNumBtn.setText("获取验证码");
				mGetVerifyNumBtn.setClickable(true);
				break;
			case Constants.SEND_SMS_VERIFY_CODE_ALREADY_SEND_ID:
				mVerifyInfo.setText("验证码已经发送到手机：" + phoneNum);
				// Toast.makeText(getApplicationContext(),
				// "验证码已经发送到手机：" + phoneNum, Toast.LENGTH_SHORT).show();
				break;
			case Constants.SEND_SMS_VERIFY_CODE_IS_SUCCESS_ID:
				// LodingView.getInstance().showLoadingInfo(
				// RegisterVerifyActivity.this,
				// RegisterVerifyActivity.this.getApplicationContext(),
				// true);
				LoginManager.getInstance().LoginByPhoneNum(
						registerVerifyhandler, phoneNum);// 此处需要访问服务器
				break;
			default:
				break;
			}
			{

			}
		}
	};

	private void startCountDown60s() {
		countTimeNum = Constants.SEND_SMS_WAIT_TIME;
		// 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
		mGetVerifyNumBtn.setClickable(false);
		mGetVerifyNumBtn.setText("重新发送(" + countTimeNum + ")");
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (; countTimeNum > 0; countTimeNum--) {
					registerVerifyhandler
							.sendEmptyMessage(Constants.SEND_SMS_COUNTDOWN_MSG_CONTINUE_ID);
					if (countTimeNum <= 0) {
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				registerVerifyhandler
						.sendEmptyMessage(Constants.SEND_SMS_COUNTDOWN_MSG_FINISHED_ID);
			}
		}).start();

	}

	private void initViews() {
		mVerifyNumber = (EditText) findViewById(R.id.editTextRegisterVerify);
		mVerifyInfo = (TextView) findViewById(R.id.str_verify_info);
		mGetVerifyNumBtn = (Button) findViewById(R.id.btn_get_verify);
		mVerifyNumBackBtn = (Button) findViewById(R.id.btn_registerverify_back);
	}

	private void setListeners() {
		mVerifyNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String verifyNumber = mVerifyNumber.getText().toString().trim();
				if (verifyNumber.length() == 4) {
					LoginManager.getInstance()
							.LoginVerifyPhoneAndVerifyNumber(
									RegisterVerifyActivity.this, phoneNum,
									verifyNumber);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mGetVerifyNumBtn.setOnClickListener(this);
		mVerifyNumBackBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_get_verify:
			LoginManager.getInstance().LoginGetVerifyNum(
					RegisterVerifyActivity.this, registerVerifyhandler,
					phoneNum);
			startCountDown60s();
			break;
		case R.id.btn_registerverify_back:
			intent = new Intent(RegisterVerifyActivity.this,
					LoginActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onDestroy() {
		LoginManager.getInstance().onDestroy();
		super.onDestroy();
	}
}
