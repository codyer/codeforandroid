package com.pao123.www.realtime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.pao123.common.Constants;
import com.pao123.www.R;
import com.pao123.www.model.RealTimeRunManager;

public class RealTimeFragment extends Fragment {

	private View mParentView;
	private RealTimeRunExpendAdapter mAdapter;
	private ExpandableListView mRealtimeExpandlist;

	@SuppressLint("HandlerLeak")
	public Handler realTimeRefreshHandler = new Handler() {
		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.MSG_GET_PERSON_RUN_FRIEND_INFO_FAILED:
				Log.e("realTimeRefreshHandler", "realTime Refresh friend info failed");
				break;
			case Constants.MSG_GET_PERSON_RUN_GROUP_INFO_FAILED:
				Log.e("realTimeRefreshHandler", "realTime Refresh group info failed");
				break;
			case Constants.MSG_GET_PERSON_RUN_FRIEND_INFO_SUCCESS:
				Log.e("realTimeRefreshHandler", "realTime Refresh friend info success");
				notifyDataSet();	
				break;
			case Constants.MSG_GET_PERSON_RUN_GROUP_INFO_SUCCESS:
				Log.e("realTimeRefreshHandler", "realTime Refresh group info success");
				notifyDataSet();
				break;
			case Constants.MSG_GET_RUN_GROUP_MEMBER_SUCCESS:
				// TODO
				Log.e("realTimeRefreshHandler", "realTime Refresh get members success");
				Intent intent = new Intent(getActivity(),RealTimeRuningSelectActivity.class);
				startActivity(intent);
				break;
			case Constants.MSG_GET_RUN_GROUP_MEMBER_FAILED:
				Log.e("realTimeRefreshHandler", "realTime Refresh get members failed");
				Toast.makeText(getActivity(), "获取跑团成员失败！", Toast.LENGTH_SHORT);
				break;
			default:
				break;
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mParentView = inflater
				.inflate(R.layout.realtime_main, container, false);
		mRealtimeExpandlist = (ExpandableListView) mParentView
				.findViewById(R.id.realtime_list_view);
		return mParentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		mAdapter = new RealTimeRunExpendAdapter(getActivity());
		mRealtimeExpandlist.setAdapter(mAdapter);

		mRealtimeExpandlist.setGroupIndicator(null);
		listenListActivity();
		RealTimeRunManager.OnStart();
		RealTimeRunManager.getInstance().refreshData(realTimeRefreshHandler);
	}

	public void listenListActivity() {
		mRealtimeExpandlist.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				RealTimeRunManager.getInstance().setCurrentObject(
						groupPosition, childPosition);				
				// TODO
				// 此处跳转到跑团成员列表或者直接跳转到地图观看
				switch (groupPosition) {
				case 0://直接跳转
					
					break;
				case 1://获取跑团成员，再做跳转
					RealTimeRunManager.getInstance().getGroupRunningMembers(realTimeRefreshHandler);					
					break;
				default:
					break;
				}
				return false;
			}
		});

		mRealtimeExpandlist
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
					}
				});
	}

	public void notifyDataSet() {
		mAdapter.notifyDataSetChanged();
	}
}
