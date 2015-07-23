package com.pao123.www.model;

import android.content.Context;
import android.os.Handler;

public interface iLoginManager {
	/***
	 * 检查是否已经登录
	 * @return 已登录：userid，未登录：0
	 * 只需要检查本地
	 */
	public long CheckLogin(Context context);
	
	/***
	 * 通过电话号码获得验证码
	 * @param phonenum
	 * @return 成功：返回的验证码，失败：null
	 * 发送短信到手机，验证是否为自己的手机，本地应用存储验证码，
	 * 收到短信验证码则视为是本人手机
	 * LoginManager管理此验证码，其实可以不返回验证码
	 */
	public void LoginGetVerifyNum(Context ctx,Handler msgHandler,String phonenum);
	/***
	 * 验证验证码是否正确
	 * @param phonenum 电话号码
	 * @param verifynum 验证码
	 */
	public void LoginVerifyPhoneAndVerifyNumber(Context ctx,String phonenum,String verifynum);
	/***
	 * 通过电话号码登录应用
	 * @param phonenum 电话号码
	 * @return 登录成功：userId，失败：0
	 * 需要本地保存登录状态
	 */
	public long LoginByPhoneNum(Handler msgHandler,String phonenum);
	
	/***
	 * 登出
	 * @return 成功：返回登出userId，失败：0
	 */
	public long LogoutCurrentUser(Context context);
	
	public long RegisterByPhoneNum(String phonenum);
	
	public void onDestroy();
}
