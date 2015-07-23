package com.pao123.bean.json;

import java.util.List;

import com.pao123.bean.Lap;

public class GetLatestData {
	private String id;
	private List<Lap> lap;
	private String condition;
	private String starttime;
	private String status;
	private String duration;
	private String length;
	private String calorie;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Lap> getLap() {
		return lap;
	}
	public void setLap(List<Lap> lap) {
		this.lap = lap;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getCalorie() {
		return calorie;
	}
	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}
	
}
