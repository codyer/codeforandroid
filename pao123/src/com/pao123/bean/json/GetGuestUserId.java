package com.pao123.bean.json;
/**
 * 2.1)request a guest user
 *  curl http://121.41.86.134/xingjiansport/V1/User/getGuestUserId/ -d "cellphone=15921613015" -X POST
 *  如果没有该用户，就添加一个，并返回user id。如果已经注册，就直接返回 user id.
 * @author 旭
 */
public class GetGuestUserId {
	private long id;
	private String name;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
