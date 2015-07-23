package com.pao123.www.realtime;

import com.pao123.bean.Friend;
import com.pao123.www.R;
import com.pao123.www.model.RealTimeRunManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RealTimeRunningAdapter extends BaseAdapter {

	Context context;
	ImageView mPortrait;
	TextView mUserName;
	ImageView mCheckBtn;
	
	public RealTimeRunningAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return RealTimeRunManager.getInstance().getmCurrentSelctedGroup()
				.getMemberrunningcount();
	}

	@Override
	public Object getItem(int position) {
		return RealTimeRunManager.getInstance().getSelectGroupMember(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.realtime_running_list_item, null);
		}
		mPortrait = (ImageView) convertView
				.findViewById(R.id.real_time_running_portrait_img);
		mUserName = (TextView) convertView
				.findViewById(R.id.real_time_running_user_name_txt);
		mCheckBtn = (ImageView) convertView
				.findViewById(R.id.real_time_running_check_btn);
		
		mUserName.setText(((Friend)RealTimeRunManager.getInstance().getSelectGroupMember(position)).getNickname());
		if (RealTimeRunManager.getInstance().getmSelectItemList().size() > position) {
			if (RealTimeRunManager.getInstance().getmSelectItemList().get(position)) {
				mCheckBtn.setImageResource(R.drawable.check);
			}else {
				mCheckBtn.setImageResource(R.drawable.uncheck);
			}
		}else {
			RealTimeRunManager.getInstance().getmSelectItemList().add(position,false);
			mCheckBtn.setImageResource(R.drawable.uncheck);
		}
		convertView.setTag(R.id.real_time_running_user_name_txt);
		
		
		return convertView;
	}

	/**
	 * check or uncheck
	 * @param position
	 */
	public void setClickPos(int position) {
		if (RealTimeRunManager.getInstance().getmSelectItemList() != null) {
			if (RealTimeRunManager.getInstance().getmSelectItemList().size() > position) {
				RealTimeRunManager.getInstance().getmSelectItemList().set(position, !RealTimeRunManager.getInstance().getmSelectItemList().get(position));
				RealTimeRunManager.getInstance().setSelectMember(position,RealTimeRunManager.getInstance().getmSelectItemList().get(position));
			}
		}
	}

}
