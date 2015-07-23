package com.pao123.www.login;

import com.pao123.www.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginByPhoneActivity extends Activity implements OnClickListener {

	private EditText mLoginNumber;
	private Button mBtnRegister;
	private Button mBtnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉界面任务条
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login_by_phone);
		initViews();
		setListeners();
	}

	private void initViews() {
		mLoginNumber = (EditText) findViewById(R.id.editTextRegister);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnRegister = (Button) findViewById(R.id.btn_register);
	}

	private void setListeners() {
		mBtnBack.setOnClickListener(this);
		mBtnRegister.setOnClickListener(this);
		mLoginNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String phoneNum = mLoginNumber.getText().toString().trim();
				if (phoneNum.length() == 11) {
					// 1. 通过规则判断手机号
					if (!judgePhoneNums(phoneNum)) {
						mLoginNumber.setText("");
						return;
					}
					Intent intent = new Intent(LoginByPhoneActivity.this,
							RegisterVerifyActivity.class);
					//用Bundle携带数据
				    Bundle bundle=new Bundle();
				    //传递name参数为tinyphp
				    bundle.putString("PHONENUM", phoneNum);
					intent.putExtras(bundle);
					startActivity(intent);
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
		
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_register:
			intent = new Intent(LoginByPhoneActivity.this,
					RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_back:
			intent = new Intent(LoginByPhoneActivity.this, LoginActivity.class);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 判断手机号码是否合理
	 * 
	 * @param phoneNums
	 */
	private boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
			return true;
		}
		Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * 判断一个字符串的位数
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static boolean isMatchLength(String str, int length) {
		if (str.isEmpty()) {
			return false;
		} else {
			return str.length() == length ? true : false;
		}
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobileNums) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else
			return mobileNums.matches(telRegex);
	}
}
