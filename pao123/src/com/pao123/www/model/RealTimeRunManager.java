package com.pao123.www.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.pao123.bean.RunningFriend;
import com.pao123.bean.RunningGroup;
import com.pao123.bean.WatchMember;
import com.pao123.bean.json.GetFriendList;
import com.pao123.bean.json.GetPersonRungroupInfo;
import com.pao123.bean.json.GetRungroupMemberList;
import com.pao123.common.CallbackObject;
import com.pao123.common.Constants;
import com.pao123.common.PaoSdk;
import com.pao123.common.SdkAsyncCaller;

public class RealTimeRunManager {

	private static RealTimeRunManager realtimeManagerInstance;
	/**
	 * 实时观看第一个页面
	 */
	private List<String> mRunningTypeList;
	private List<Object> mRunningFriendList;// 正在跑步的朋友列表
	private List<Object> mRunningGroupList;// 正在跑步的跑团列表
	private RunningFriend mCurrentSelctedFriend;
	private RunningGroup mCurrentSelctedGroup;
	private String mCurrentSelctedType;

	/**
	 * 实时观看第二个画面
	 */
	private List<Boolean> mSelectItemList;
	private List<WatchMember> mSelectGroupRunningMemberList;// 选中的跑团的成员列表,对象是WatchMember
	private List<WatchMember> mWatchMemberList;// 需要观看的人的列表,对象是WatchMember类型

	/**
	 * 实时观看第三个画面，地图画面
	 */
	private List<WatchMember> mPraiseMemberList;// 被选中将被点赞的人的列表,对象是Friend类型
	private int mCurrentPosition;// 当前选中的人，需要显示这个人的信息
	private Map<String, List<Object>> mMsgQueue;// 消息队列

	private RealTimeRunManager() {
		super();
		init();
	}

	public static RealTimeRunManager getInstance() {
		if (realtimeManagerInstance == null) {
			synchronized (RealTimeRunManager.class) {
				if (realtimeManagerInstance == null) {
					realtimeManagerInstance = new RealTimeRunManager();
				}
			}
		}
		return realtimeManagerInstance;
	}
	public static void OnStart(){
		realtimeManagerInstance = null;
	}
	public void initForSelectGroupOrPerson() {
		mCurrentSelctedFriend = null;
		mCurrentSelctedGroup = null;
		mCurrentSelctedType = null;
	}

	public void initForSelectWatchPerson() {
		mSelectItemList.clear();
		mWatchMemberList.clear();
		mPraiseMemberList.clear();
		mCurrentPosition = 0;
		mMsgQueue.clear();
	}
	public void initForSelectMember() {
		for (int i = 0; i < mWatchMemberList.size(); i++) {
			mWatchMemberList.get(i).setLastLat(null);
			mWatchMemberList.get(i).setLastLng(null);
			mWatchMemberList.get(i).setLastTime(null);
			mMsgQueue.get(mWatchMemberList.get(i).getId()).clear();
		}
		mCurrentPosition = 0;
	}

	/**
	 * 需要调用此函数刷新数据，传入句柄接收数据返回的提示，数据在本实例中保存，不需要传输
	 * 
	 * @param msgHandler
	 *            接收数据获取成功与否提示的消息handler
	 */
	public void refreshData(final Handler msgHandler) {
		refreshGroupData(msgHandler);
		refreshFriendData(msgHandler);
	}

	private void refreshFriendData(final Handler msgHandler) {
		long id = LoginManager.getInstance().getmCurrentAccount()
				.getmAuthenticationId();
		PaoSdk sdk = new PaoSdk(Constants.URL_GET_FRIEND_LIST + id);

		SdkAsyncCaller<GetFriendList> async = new SdkAsyncCaller<GetFriendList>();
		async.postObject(sdk, GetFriendList.class,
				new CallbackObject<GetFriendList>() {
					@Override
					public void callback(GetFriendList info) {
						if (msgHandler != null) {
							if (info != null) {
								fillRunningFriendDataIntoInstance(info);
								msgHandler
										.sendEmptyMessage(Constants.MSG_GET_PERSON_RUN_FRIEND_INFO_SUCCESS);
							} else {
								msgHandler
										.sendEmptyMessage(Constants.MSG_GET_PERSON_RUN_FRIEND_INFO_FAILED);
							}
						}
					}
				});
	}

	private void refreshGroupData(final Handler msgHandler) {
		long id = LoginManager.getInstance().getmCurrentAccount()
				.getmAuthenticationId();
		PaoSdk sdk = new PaoSdk(Constants.URL_GET_USER_RUN_GROUP_INFO + id);

		SdkAsyncCaller<GetPersonRungroupInfo> async = new SdkAsyncCaller<GetPersonRungroupInfo>();
		async.postObject(sdk, GetPersonRungroupInfo.class,
				new CallbackObject<GetPersonRungroupInfo>() {
					@Override
					public void callback(GetPersonRungroupInfo info) {
						if (msgHandler != null) {
							if (info != null) {
								fillRunningGroupDataIntoInstance(info);
								msgHandler
										.sendEmptyMessage(Constants.MSG_GET_PERSON_RUN_GROUP_INFO_SUCCESS);
							} else {
								msgHandler
										.sendEmptyMessage(Constants.MSG_GET_PERSON_RUN_GROUP_INFO_FAILED);
							}
						}
					}
				});
	}

	/**
	 * 把数据填充到此实例，也可以在此处保存本地数据
	 * 
	 * @param info
	 */
	private void fillRunningFriendDataIntoInstance(GetFriendList info) {
		RunningFriend rf = new RunningFriend();
		mRunningFriendList.clear();
		for (int i = 0; i < info.getFriends().size(); i++) {
			if (info.getFriends().get(i).getIsrunning() > 0) {
				rf.setmFriendName(info.getFriends().get(i).getNickname());
				rf.setmFriendFrom(info.getFriends().get(i).getName());
				if (!mRunningFriendList.contains(rf)) {
					mRunningFriendList.add(rf);
				}
			}
		}
	}

	/**
	 * 把数据填充到此实例，也可以在此处保存本地数据
	 * 
	 * @param info
	 */
	private void fillRunningGroupDataIntoInstance(GetPersonRungroupInfo info) {
		RunningGroup rg = new RunningGroup();
		mRunningGroupList.clear();
		for (int i = 0; i < info.getOwngroup().size(); i++) {
			if (info.getOwngroup().get(i).getMemberrunningcount() > 0) {
				rg.setMemberrunningcount(info.getOwngroup().get(i)
						.getMemberrunningcount());
				rg.setMembercount(info.getOwngroup().get(i).getMembercount());
				rg.setName(info.getOwngroup().get(i).getName());
				rg.setId(info.getOwngroup().get(i).getId());
				rg.setDescription(info.getOwngroup().get(i).getDescription());
				if (!mRunningGroupList.contains(rg)) {
					mRunningGroupList.add(rg);
				}
			}
		}
		for (int i = 0; i < info.getJoingroup().size(); i++) {
			if (info.getJoingroup().get(i).getMemberrunningcount() > 0) {
				rg.setMemberrunningcount(info.getJoingroup().get(i)
						.getMemberrunningcount());
				rg.setMembercount(info.getJoingroup().get(i).getMembercount());
				rg.setName(info.getJoingroup().get(i).getName());
				rg.setId(info.getJoingroup().get(i).getId());
				rg.setDescription(info.getJoingroup().get(i).getDescription());
				if (!mRunningGroupList.contains(rg)) {
					mRunningGroupList.add(rg);
				}
			}
		}
	}

	private void init() {
		mRunningFriendList = new ArrayList<Object>();
		mRunningGroupList = new ArrayList<Object>();
		mRunningTypeList = new ArrayList<String>();
		mSelectGroupRunningMemberList = new ArrayList<WatchMember>();
		mWatchMemberList = new ArrayList<WatchMember>();
		mPraiseMemberList = new ArrayList<WatchMember>();
		mSelectItemList = new ArrayList<Boolean>();
		mCurrentPosition = 0;
		mMsgQueue = new HashMap<String, List<Object>>();
		mRunningTypeList.add(Constants.RUNNING_LIST_TYPE_FRIEND);
		mRunningTypeList.add(Constants.RUNNING_LIST_TYPE_GROUP);
	}


	public List<Object> getRunningList(int groupPosition) {
		switch (groupPosition) {
		case 0:
			return mRunningFriendList;
		case 1:
			return mRunningGroupList;
		default:
			break;
		}
		return null;
	}

	public Object getRunningObject(int groupPosition, int childPosition) {
		switch (groupPosition) {
		case 0:
			return getRuningFriend(childPosition);
		case 1:
			return getRuningGroup(childPosition);
		default:
			break;
		}
		return null;
	}

	private RunningFriend getRuningFriend(int childPosition) {
		if (mRunningFriendList != null) {
			return (RunningFriend) mRunningFriendList.get(childPosition);
		}
		return null;
	}

	private RunningGroup getRuningGroup(int childPosition) {
		if (mRunningGroupList != null) {
			return (RunningGroup) mRunningGroupList.get(childPosition);
		}
		return null;
	}

	// 设置当前观看的对象，是某个人还是某个跑团，在这里存储信息
	public void setCurrentObject(int groupPosition, int childPosition) {
		switch (groupPosition) {
		case 0:
			if (mRunningFriendList != null) {
				mCurrentSelctedFriend = (RunningFriend) mRunningFriendList
						.get(childPosition);
			}
			break;
		case 1:
			if (mRunningGroupList != null) {
				mCurrentSelctedGroup = (RunningGroup) mRunningGroupList
						.get(childPosition);
			}
			break;
		default:
			break;
		}
		if (mRunningTypeList != null) {
			mCurrentSelctedType = mRunningTypeList.get(groupPosition);
		}

	}

	public void getGroupRunningMembers(final Handler msgHandler) {
		String id = mCurrentSelctedGroup.getId();
		PaoSdk sdk = new PaoSdk(Constants.URL_GET_RUN_GROUP_MEMBERS + id);

		SdkAsyncCaller<GetRungroupMemberList> async = new SdkAsyncCaller<GetRungroupMemberList>();
		async.postObject(sdk, GetRungroupMemberList.class,
				new CallbackObject<GetRungroupMemberList>() {
					@Override
					public void callback(GetRungroupMemberList info) {
						if (msgHandler != null) {
							if (info != null) {
								fillRunningGroupMembersIntoInstance(info);
								msgHandler
										.sendEmptyMessage(Constants.MSG_GET_RUN_GROUP_MEMBER_SUCCESS);
							} else {
								msgHandler
										.sendEmptyMessage(Constants.MSG_GET_RUN_GROUP_MEMBER_FAILED);
							}
						}
					}
				});

	}

	protected void fillRunningGroupMembersIntoInstance(
			GetRungroupMemberList info) {
		for (int i = 0; i < info.getId(); i++) {
			if (info.getMember().get(i).getIsrunning() > 0) {
				WatchMember watchMember = new WatchMember();
				watchMember.setId(info.getMember().get(i).getId());
				watchMember
						.setIsrunning(info.getMember().get(i).getIsrunning());
				watchMember.setName(info.getMember().get(i).getName());
				watchMember.setNickname(info.getMember().get(i).getNickname());
				watchMember.setLastLat(null);
				watchMember.setLastLng(null);
				watchMember.setLastTime(null);
				mSelectGroupRunningMemberList.add(watchMember);
			}
		}
	}

	public List<String> getmRunningTypeList() {
		return mRunningTypeList;
	}

	public void setmRunningTypeList(List<String> mRunningTypeList) {
		this.mRunningTypeList = mRunningTypeList;
	}

	public RunningFriend getmCurrentSelctedFriend() {
		return mCurrentSelctedFriend;
	}

	public RunningGroup getmCurrentSelctedGroup() {
		return mCurrentSelctedGroup;
	}

	public String getmCurrentSelctedType() {
		return mCurrentSelctedType;
	}

	public Object getSelectGroupMember(int position) {
		if (mSelectGroupRunningMemberList == null) {
			return null;
		}
		return mSelectGroupRunningMemberList.get(position);
	}

	public void setSelectMember(int position, Boolean boolean1) {
		if (mWatchMemberList != null) {
			WatchMember watchMember = mSelectGroupRunningMemberList
					.get(position);
			if (boolean1) {
				if (!mWatchMemberList.contains(watchMember)) {
					mWatchMemberList.add(watchMember);
					ArrayList<Object> msgList = new ArrayList<Object>();
					mMsgQueue.put(watchMember.getId(), msgList);
				}
			} else {
				mWatchMemberList.remove(watchMember);
				mMsgQueue.remove(watchMember.getId());
			}
		}
	}

	public void clearmWatchMemberList() {
		if (mWatchMemberList != null) {
			mWatchMemberList.clear();
		}
		clearmMsgQueue();
	}

	public void clearmMsgQueue() {
		if (mMsgQueue != null) {
			mMsgQueue.clear();
		}
	}

	public void clearmPraiseMemberList() {
		if (mPraiseMemberList != null) {
			mPraiseMemberList.clear();
		}
	}

	public List<WatchMember> getmWatchMemberList() {
		return mWatchMemberList;
	}

	public List<Boolean> getmSelectItemList() {
		return mSelectItemList;
	}

	public void setmSelectItemList(List<Boolean> mSelectItemList) {
		this.mSelectItemList = mSelectItemList;
	}

	public void setSelectPraiseMember(int position, Boolean boolean1) {
		if (mPraiseMemberList != null) {
			if (boolean1) {
				if (!mPraiseMemberList
						.contains((mWatchMemberList.get(position))))
					mPraiseMemberList.add((mWatchMemberList.get(position)));
			} else {
				mPraiseMemberList.remove((mWatchMemberList.get(position)));
			}
		}
	}

	public void SendPraiseToAllSelectPerson() {
		String string = "mPraiseMemberList:";
		for (int i = 0; i < mPraiseMemberList.size(); i++) {
			if (mPraiseMemberList != null) {
				string += ("|" + ((mPraiseMemberList.get(i))).getNickname());
			}
		}
		Log.e("mPraiseMemberList", string);
	}

	public void setmCurrentPosition(int position) {
		mCurrentPosition = position;
	}

	public int getmCurrentPosition() {
		return mCurrentPosition;
	}
	public WatchMember getCurrentWatchMember() {
		return mWatchMemberList.get(mCurrentPosition);
	}
	public Map<String, List<Object>> getmMsgQueue() {
		return mMsgQueue;
	}

}
