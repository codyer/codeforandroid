package com.pao123.common;

import android.graphics.Color;

public class Constants {
	public static int LineColors[] = {
		Color.RED,
		Color.GREEN,
		Color.BLUE,
		Color.YELLOW,
		Color.BLACK,
		Color.DKGRAY,
		Color.GRAY,
		Color.LTGRAY,
		Color.CYAN,
		Color.MAGENTA
	};
	public static final String RUNNING_LIST_TYPE_FRIEND = "正在跑步的好友";//正在跑步的好友
	public static final String RUNNING_LIST_TYPE_GROUP = "正在跑步的跑团";//正在跑步的跑团
	
    public static final int MSG_LOAD_IMAGE_FAILED = 0;
    public static final int MSG_LOAD_IMAGE_SUCCESS = 1;
    
    //实时观看-朋友
    public static final int MSG_GET_PERSON_RUN_FRIEND_INFO_FAILED = 2;
    public static final int MSG_GET_PERSON_RUN_FRIEND_INFO_SUCCESS = 3;
    //实时观看-跑团
    public static final int MSG_GET_PERSON_RUN_GROUP_INFO_FAILED = 4;
    public static final int MSG_GET_PERSON_RUN_GROUP_INFO_SUCCESS = 5;
    //实时观看-获取跑团正在跑的成员
    public static final int MSG_GET_RUN_GROUP_MEMBER_FAILED = 6;
    public static final int MSG_GET_RUN_GROUP_MEMBER_SUCCESS = 7;
    
    
	public static final int SEND_SMS_WAIT_TIME = 60;
	public static final int SEND_SMS_COUNTDOWN_MSG_CONTINUE_ID = 10;//倒计时继续
	public static final int SEND_SMS_COUNTDOWN_MSG_FINISHED_ID = 11;//倒计时结束
	public static final int LOGIN_SUCCESS_MSG_ID = 12;//登录成功
	public static final int LOGIN_FAILED_MSG_ID = 13;//登录失败
	public static final int LOGIN_GUEST_USER_ID = 10001;//14;//默认游客ID
	public static final String LOGIN_GUEST_USER_NAME = "游客";//默认游客
	public static final String LOGIN_GUEST_USER_NICK_NAME = "游客昵称";//默认游客昵称
	public static final String LOGIN_GUEST_USER_AGE = "18";//默认游客年龄
	public static final String LOGIN_GUEST_USER_GENDER = "男";//默认游客性别
	public static final String LOGIN_GUEST_USER_WEIGHT = "65";//默认游客体重
	public static final String LOGIN_GUEST_USER_HEIGHT = "175";//默认游客身高
	public static final int SEND_SMS_VERIFY_CODE_IS_SUCCESS_ID = 15;//验证码正确
	public static final int SEND_SMS_VERIFY_CODE_IS_FAILED_ID = 16;//验证码错误
	public static final int SEND_SMS_VERIFY_CODE_ALREADY_SEND_ID = 17;//验证码已经发送到手机
	public static final String PAO123_PREFERENCES = "Pao123_Preferences";
	public static final int GPS_SIGNAL_STRENGTH_THRESHOLD = 50;
	public static final int HTTP_CONNECT_TIMEOUT = 3000;
	public static final int HTTP_SOCKET_TIMEOUT = 50000;
	public static final String SMS_APP_KEY = "8d11ec42aa18";
	public static final String SMS_APP_SECRET = "6fdf77e98a32ec70e2a368bfe63d6b7f";
	public static final String HTTP_ADDRESS = "www.123yd.cn";//"121.41.86.134";
	public static final String HTTP_PORT = "80";

	public static final String MOBILE_PHONE_NUMBER = "^1(3[0-9]|5[0-35-9]|8[0125-9])\\d{8}$";

	public static final String REMOTE_SERVER = "/xingjiansport/V1/Workout/saveWorkoutSegment/";
	public static final String URL_GET_WORKOUT = "/xingjiansport/V1/Workout/getWorkoutInfo/id/";
	public static final String URL_GET_WORKOUT_LIST = "/xingjiansport/V1/Workout/listWorkout/userid/";
	public static final String URL_POST_DEBUGINFO = "/xingjiansport/V1/Workout/saveDebugInfo";
	public static final String URL_GET_USER_INFO = "/xingjiansport/V1/User/getGuestUserId/";
	public static final String URL_UPDATE_USER_INFO = "/xingjiansport/V1/User/updateUserInfo/id/";
	public static final String URL_PULL_REALTIME_WORKOUT = "/xingjiansport/V1/Workout/getLatestData/userid/";
	public static final String URL_USER_REGISTER = "/xingjiansport/V1/User/registerUser/";
	public static final String URL_USER_LOGIN = "/xingjiansport/V1/User/login";
	public static final String URL_SEARCH_USER_BY_NAME = "/xingjiansport/V1/User/searchUserByName/name/";
	public static final String URL_SEARCH_USERS_BY_NAME_LIST = "/xingjiansport/V1/User/searchUserByNameList/name/";
	public static final String URL_ADD_FRIEND = "/xingjiansport/V1/User/addFriends/id/";
	public static final String URL_GET_FRIEND_LIST = "/xingjiansport/V1/User/getFriendList/id/";
	public static final String URL_CREATE_RUN_GROUP = "/xingjiansport/V1/Rungroup/createRungroup/userid/";
	public static final String URL_GET_USER_RUN_GROUP_INFO = "/xingjiansport/V1/Rungroup/getPersonRungroupInfo/userid/";
	public static final String URL_SEARCH_RUN_GROUP = "/xingjiansport/V1/Rungroup/queryRungroup/name/";
	public static final String URL_GET_RUN_GROUP_INFO = "/xingjiansport/V1/Rungroup/getRungroupInfo/id/";
	public static final String URL_USER_APPLY_RUN_GROUP = "/xingjiansport/V1/Rungroup/applyJoinRungroup/";
	public static final String URL_GET_RUN_GROUP_APPLY_INFO = "/xingjiansport/V1/Rungroup/getRungroupApplicationList/id/";
	public static final String URL_GET_RUN_GROUP_MEMBERS = "/xingjiansport/V1/Rungroup/getRungroupMemberList/id/";
	public static final String URL_APPROVE_JOIN_RUN_GROUP = "/xingjiansport/V1/Rungroup/approveJoinRungroup/";

	public static final int USERID = 14;
	public static final int EF_UPDATE_STATE = 1;
	public static final int EF_UPDATE_LOCATION = 2;
	public static final int EF_UPDATE_HEARTRATE = 4;
	public static final int EF_UPDATE_REALTIME = 8;
	public static final int EF_UPDATE_DURATION = 16;
	// public static final int EF_UPDATE_ALL =0xffffffffffffffff;

	// public static final String ="DEBUG_ENTER";
	// public static final String= "DEBUG_LEAVE";

	// -----------------------------------------
	// UI theme
	// -----------------------------------------
	// system status bar height: 20 pixels
	public static final int STATUSBARHEIGHT = 20;
	// height of title bar, button, etc.
	public static final int TITLEBARHEIGHT = 44;
	// menu width
	public static final int MENUWIDTH = 240;

	// public static final String DEFBKCOLOR [UIColor colorWithRed:0.7
	// green:0.75 blue:0.7 alpha:1.0];
	// public static final String STATUSBARTINTCOLOR [UIColor colorWithRed:0.24
	// green:0.45 blue:0.76 alpha:1.0];

}
