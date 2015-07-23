package com.pao123.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

public class WatchMember extends Friend {
	private String lastTime = null;//最后取回的数据时间
	private String lastLat = null;//最后lat
	private String lastLng = null;//最后lng

	@SuppressLint("SimpleDateFormat")
	public String getLastTime() {
		if (lastTime == null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			Date lastDate = new Date(0);
			lastTime = format.format(lastDate); 
			Log.e("lastTime","lastTime="+lastTime);
		}
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getLastLat() {		
		return lastLat;
	}

	public void setLastLat(String lastLat) {
		this.lastLat = lastLat;
	}

	public String getLastLng() {
		return lastLng;
	}

	public void setLastLng(String lastLng) {
		this.lastLng = lastLng;
	}
}
