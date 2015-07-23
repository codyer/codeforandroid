package com.pao123.www.login;

import com.pao123.common.Constants;
import com.pao123.contrl.menu.MenuActivity;
import com.pao123.www.R;
import com.pao123.www.model.LoginManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private Button btnLogin;
	private Button btnRegister;
	private Button btnLoginByWechat;
	private Button btnLoginByGuest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉界面任务条
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		long id = LoginManager.getInstance().CheckLogin(LoginActivity.this);
		if (id != Constants.LOGIN_GUEST_USER_ID) {
			Log.e("LoginActivity", "LoginActivity 用户已经登录/注册过 id=" + id);
			Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
			startActivity(intent);
		}
		initViews();
		setListeners();
	}

	private void initViews() {
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnRegister = (Button) findViewById(R.id.btn_register);
		btnLoginByWechat = (Button) findViewById(R.id.btn_login_by_wechat);
		btnLoginByGuest = (Button) findViewById(R.id.btn_login_by_guest);
	}
	private void setListeners() {
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnLoginByWechat.setOnClickListener(this);
		btnLoginByGuest.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_login:
			intent = new Intent(LoginActivity.this, LoginByPhoneActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_register:
			intent = new Intent(LoginActivity.this, LoginByPhoneActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_login_by_wechat:
			Toast.makeText(LoginActivity.this, "暂时还不支持，请使用电话注册登录。",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.btn_login_by_guest:
			intent = new Intent(LoginActivity.this, MenuActivity.class);
			startActivity(intent);
			break;
		}
	}
}
