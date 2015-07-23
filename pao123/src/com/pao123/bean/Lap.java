package com.pao123.bean;

import java.util.List;

public class Lap {
	private String starttime;
	private String duration;
	private String lap;
	private String length;
	private List<Location> locationdata;
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getLap() {
		return lap;
	}
	public void setLap(String lap) {
		this.lap = lap;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public List<Location> getLocationdata() {
		return locationdata;
	}
	public void setLocationdata(List<Location> locationdata) {
		this.locationdata = locationdata;
	}
	
}
