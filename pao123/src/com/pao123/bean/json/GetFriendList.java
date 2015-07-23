package com.pao123.bean.json;

import java.util.List;
import com.pao123.bean.Friend;

/**
 * http://121.41.86.134/xingjiansport/V1/User/getFriendList/id/{userid}
 * @author 旭
 *
 */
public class GetFriendList {
	private int id;
	private List<Friend> friends;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Friend> getFriends() {
		return friends;
	}
	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
}
