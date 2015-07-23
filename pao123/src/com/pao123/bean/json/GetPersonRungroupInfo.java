package com.pao123.bean.json;

import java.util.List;

import com.pao123.bean.ApplyGroup;
import com.pao123.bean.JoinGroup;
import com.pao123.bean.OwnGroup;
/**
 * for interface 
 * 29）查询单个用户的跑团信息
 * curl http://www.123yd.cn/xingjiansport/V1/Rungroup/getPersonRungroupInfo/userid/{userid}   -X POST
 * @author 旭
 */
public class GetPersonRungroupInfo {
	private int id;
	private List<OwnGroup> owngroup;
	private List<String> query;//debug info
	private List<JoinGroup> joingroup;
	private List<ApplyGroup> applygroup;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<OwnGroup> getOwngroup() {
		return owngroup;
	}
	public void setOwngroup(List<OwnGroup> owngroup) {
		this.owngroup = owngroup;
	}
	public List<String> getQuery() {
		return query;
	}
	public void setQuery(List<String> query) {
		this.query = query;
	}
	public List<JoinGroup> getJoingroup() {
		return joingroup;
	}
	public void setJoingroup(List<JoinGroup> joingroup) {
		this.joingroup = joingroup;
	}
	public List<ApplyGroup> getApplygroup() {
		return applygroup;
	}
	public void setApplygroup(List<ApplyGroup> applygroup) {
		this.applygroup = applygroup;
	}
}
