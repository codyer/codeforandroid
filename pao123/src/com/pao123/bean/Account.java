package com.pao123.bean;

import java.util.List;

import com.pao123.common.Constants;

public class Account {
	private long mAuthenticationId;
	private String mName;
	private String mNickName;
	private String mAge;
	private String mGender;
	private String mWeight;
	private String mHeight;
	private History mHistory;
	private List<Friend> mFriends;
	private List<RunGroup> mRunGroups;
	public Account() {
		this.mAuthenticationId = Constants.LOGIN_GUEST_USER_ID;
		this.mName = Constants.LOGIN_GUEST_USER_NAME;
		this.mNickName = Constants.LOGIN_GUEST_USER_NAME;
		this.mAge = Constants.LOGIN_GUEST_USER_AGE;
		this.mGender = Constants.LOGIN_GUEST_USER_GENDER;
		this.mWeight = Constants.LOGIN_GUEST_USER_WEIGHT;
		this.mHeight = Constants.LOGIN_GUEST_USER_HEIGHT;
	}
	public long getmAuthenticationId() {
		return mAuthenticationId;
	}
	public void setmAuthenticationId(long mAuthenticationId) {
		this.mAuthenticationId = mAuthenticationId;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmNickName() {
		return mNickName;
	}
	public void setmNickName(String mNickName) {
		this.mNickName = mNickName;
	}
	public String getmAge() {
		return mAge;
	}
	public void setmAge(String mAge) {
		this.mAge = mAge;
	}
	public String getmGender() {
		return mGender;
	}
	public void setmGender(String mGender) {
		this.mGender = mGender;
	}
	public String getmWeight() {
		return mWeight;
	}
	public void setmWeight(String mWeight) {
		this.mWeight = mWeight;
	}
	public String getmHeight() {
		return mHeight;
	}
	public void setmHeight(String mHeight) {
		this.mHeight = mHeight;
	}
	public History getmHistory() {
		return mHistory;
	}
	public void setmHistory(History mHistory) {
		this.mHistory = mHistory;
	}
	public List<Friend> getmFriends() {
		return mFriends;
	}
	public void setmFriends(List<Friend> mFriends) {
		this.mFriends = mFriends;
	}
	public List<RunGroup> getmRunGroups() {
		return mRunGroups;
	}
	public void setmRunGroups(List<RunGroup> mRunGroups) {
		this.mRunGroups = mRunGroups;
	}
	
}
