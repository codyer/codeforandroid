package com.pao123.bean.json;

import java.util.List;

import com.pao123.bean.Friend;
/**
 * http://www.123yd.cn/xingjiansport/V1/Rungroup/getRungroupMemberList/id/31
 * @author 旭
 *
 */
public class GetRungroupMemberList {
	//
	private int id;
	private List<Friend> member;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Friend> getMember() {
		return member;
	}
	public void setMember(List<Friend> member) {
		this.member = member;
	}
	
}
