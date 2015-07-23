package com.pao123.www.realtime;

import java.util.ArrayList;
import java.util.List;

import com.pao123.bean.Friend;
import com.pao123.www.R;
import com.pao123.www.model.RealTimeRunManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RealTimeMapFriendListAdapter extends BaseAdapter {

	Context context;
	ImageView mPortrait;
	TextView mUserName;
	ImageView mCheckBtn;
	List<Boolean> mSelectItemList;// 被选中人的列表
	int mCurrentPersonIndex;
	private RelativeLayout mItemLayout;

	public RealTimeMapFriendListAdapter(Context context) {
		this.context = context;
		mCurrentPersonIndex = 0;// 默认选择第一个人
		mSelectItemList = new ArrayList<Boolean>();
		RealTimeRunManager.getInstance().clearmPraiseMemberList();
	}

	@Override
	public int getCount() {
		return RealTimeRunManager.getInstance().getmWatchMemberList().size();
	}

	@Override
	public Object getItem(int position) {
		return RealTimeRunManager.getInstance().getmWatchMemberList()
				.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "InflateParams", "ResourceAsColor" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.realtime_map_right_list_item, null);
		}
		mItemLayout = (RelativeLayout) convertView
				.findViewById(R.id.real_time_map_item_layout);

		mPortrait = (ImageView) convertView
				.findViewById(R.id.real_time_map_portrait_img);
		mUserName = (TextView) convertView
				.findViewById(R.id.real_time_map_user_name_txt);
		mCheckBtn = (ImageView) convertView
				.findViewById(R.id.real_time_map_check_btn);

		mUserName.setText(((Friend) RealTimeRunManager.getInstance()
				.getmWatchMemberList().get(position)).getNickname());
		if (mSelectItemList.size() > position) {
			if (mSelectItemList.get(position)) {
				mCheckBtn.setImageResource(R.drawable.check);
			} else {
				mCheckBtn.setImageResource(R.drawable.uncheck);
			}
		} else {
			mSelectItemList.add(position, false);
			mCheckBtn.setImageResource(R.drawable.uncheck);
		}
		if (mCurrentPersonIndex == position) {
			mItemLayout.setBackgroundColor(context.getResources().getColor(R.color.pao123_select_color));
		}else {
			mItemLayout.setBackgroundColor(Color.TRANSPARENT);

//			mItemLayout.setBackgroundColor(R.drawable.transparent);
		}
		mCheckBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("mCheckBtn", "mCheckBtn:position="+position);
				setClickCheck(position);
				notifyDataSetChanged();
			}
		});
		convertView.setTag(R.id.real_time_map_user_name_txt);

		return convertView;
	}

	/**
	 * check or uncheck
	 * 
	 * @param position
	 */
	public void setClickCheck(int position) {
		if (mSelectItemList != null) {
			if (mSelectItemList.size() > position) {
				mSelectItemList.set(position, !mSelectItemList.get(position));
				RealTimeRunManager.getInstance().setSelectPraiseMember(
						position, mSelectItemList.get(position));
			}
		}
	}
	public void setClickPos(int position) {
		mCurrentPersonIndex = position;
		RealTimeRunManager.getInstance().setmCurrentPosition(position);
	}

}
