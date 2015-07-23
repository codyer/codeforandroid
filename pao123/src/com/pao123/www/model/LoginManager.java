package com.pao123.www.model;

import com.pao123.bean.Account;
import com.pao123.bean.json.GetGuestUserId;
import com.pao123.common.CallbackObject;
import com.pao123.common.Constants;
import com.pao123.common.PaoSdk;
import com.pao123.common.SdkAsyncCaller;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

public class LoginManager implements iLoginManager {

	private static LoginManager loginManagerInstance;
	Account mCurrentAccount;

	public Account getmCurrentAccount() {
		return mCurrentAccount;
	}

	public void setmCurrentAccount(Account mCurrentAccount) {
		this.mCurrentAccount = mCurrentAccount;
	}

	private LoginManager() {
		super();
		mCurrentAccount = new Account();
	}

	public static LoginManager getInstance() {
		if (loginManagerInstance == null) {
			synchronized (LoginManager.class) {
				if (loginManagerInstance == null) {
					loginManagerInstance = new LoginManager();
				}
			}
		}
		return loginManagerInstance;
	}

	@Override
	public long CheckLogin(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Constants.PAO123_PREFERENCES, Context.MODE_PRIVATE);
		long id = sp.getLong("id", Constants.LOGIN_GUEST_USER_ID);
		String name = sp.getString("name", Constants.LOGIN_GUEST_USER_NAME);
		mCurrentAccount.setmAuthenticationId(id);
		mCurrentAccount.setmName(name);
		if(id != Constants.LOGIN_GUEST_USER_ID)
		{
			return id;
		}
		return Constants.LOGIN_GUEST_USER_ID;
	}

	@Override
	public void LoginGetVerifyNum(Context ctx,final Handler msgHandler,String phonenum) {
		// 启动短信验证sdk
		SMSSDK.initSDK(ctx, Constants.SMS_APP_KEY, Constants.SMS_APP_SECRET);
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Log.e("event", "event=" + event);
				if (result == SMSSDK.RESULT_COMPLETE) {
					// 短信注册成功后,然后提示
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
						msgHandler.sendEmptyMessage(Constants.SEND_SMS_VERIFY_CODE_IS_SUCCESS_ID);						
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//验证码已经发送
						msgHandler.sendEmptyMessage(Constants.SEND_SMS_VERIFY_CODE_ALREADY_SEND_ID);
					} else {
						((Throwable) data).printStackTrace();
					}
				}else {//验证失败
					msgHandler.sendEmptyMessage(Constants.SEND_SMS_VERIFY_CODE_IS_FAILED_ID);
				}
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		SMSSDK.getVerificationCode("86", phonenum);
	}
	@Override
	public void LoginVerifyPhoneAndVerifyNumber(Context ctx,String phonenum,
			String verifynum) {
		SMSSDK.initSDK(ctx, Constants.SMS_APP_KEY, Constants.SMS_APP_SECRET);
		SMSSDK.submitVerificationCode("86", phonenum, verifynum);		
	}
	@Override
	public long LoginByPhoneNum(final Handler msgHandler,String phonenum) {
		PaoSdk sdk = new PaoSdk(Constants.URL_GET_USER_INFO);
		sdk.setParameter("cellphone", phonenum);

		SdkAsyncCaller<GetGuestUserId> async = new SdkAsyncCaller<GetGuestUserId>();
		async.postObject(sdk, GetGuestUserId.class, new CallbackObject<GetGuestUserId>() {
			@Override
			public void callback(GetGuestUserId user) {
				if (mCurrentAccount != null && msgHandler != null) {
					if (user.getId() != 0) {
						mCurrentAccount.setmAuthenticationId(user.getId());
						msgHandler
								.sendEmptyMessage(Constants.LOGIN_SUCCESS_MSG_ID);
					} else {
						mCurrentAccount.setmAuthenticationId(Constants.LOGIN_GUEST_USER_ID);
						msgHandler
								.sendEmptyMessage(Constants.LOGIN_FAILED_MSG_ID);
					}
				}
			}
		});
		return 0;
	}

	@Override
	public long LogoutCurrentUser(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Constants.PAO123_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("id", Constants.LOGIN_GUEST_USER_ID);
        editor.putString("name", Constants.LOGIN_GUEST_USER_NAME);
        editor.commit();
		return 0;
	}

	@Override
	public long RegisterByPhoneNum(String phonenum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onDestroy() {
		SMSSDK.unregisterAllEventHandler();
	}

	

}
