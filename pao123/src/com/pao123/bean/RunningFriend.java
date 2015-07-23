package com.pao123.bean;

public class RunningFriend {
	private String mId;//朋友的id，用来标识
	private String mFriendPrtraitUrl;//头像url，需要的时候才加载，有时候可以在本地加载
	private String mFriendName;//
	private String mFriendFrom;//来自微信好友等等
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmFriendPrtraitUrl() {
		return mFriendPrtraitUrl;
	}
	public void setmFriendPrtraitUrl(String mFriendPrtraitUrl) {
		this.mFriendPrtraitUrl = mFriendPrtraitUrl;
	}
	public String getmFriendName() {
		return mFriendName;
	}
	public void setmFriendName(String mFriendName) {
		this.mFriendName = mFriendName;
	}
	public String getmFriendFrom() {
		return mFriendFrom;
	}
	public void setmFriendFrom(String mFriendFrom) {
		this.mFriendFrom = mFriendFrom;
	}
	
}
