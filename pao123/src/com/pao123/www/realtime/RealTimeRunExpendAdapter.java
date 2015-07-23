package com.pao123.www.realtime;

import com.pao123.bean.RunningFriend;
import com.pao123.bean.RunningGroup;
import com.pao123.www.R;
import com.pao123.www.model.RealTimeRunManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RealTimeRunExpendAdapter extends BaseExpandableListAdapter {
    Context context;
    private ImageView mFriendPortrait;//这阶段使用默认的
    private TextView mFriendName;
    private ImageView mGroupIco;//这阶段使用默认的-现在不使用
    private TextView mGroupName;
    private TextView mGroupRunCount;
	private TextView mListType;
	private ImageView mListTypeSwitch;
//	private List<String> mTypeList;
	
    public RealTimeRunExpendAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return RealTimeRunManager.getInstance().getRunningObject(groupPosition, childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

	@SuppressLint("InflateParams")
	@Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
		String tl=null;
        	switch (groupPosition) {
			case 0://显示实时跑步的朋友title
	            convertView = LayoutInflater.from(context).inflate(
	                    R.layout.realtime_run_friend_item, null);	
				 mFriendName = (TextView) convertView
			                .findViewById(R.id.real_time_friend_user_name_txt);
				 RunningFriend rf = (RunningFriend) RealTimeRunManager.getInstance().getRunningObject(groupPosition, childPosition);
		        if (null != rf) {
		        	mFriendName.setText(rf.getmFriendName()+"("+rf.getmFriendFrom()+")");
				}
	            tl = RealTimeRunManager.getInstance().getmRunningTypeList().get(groupPosition);
	            convertView.setTag(tl);
	            convertView.setTag(R.id.real_time_friend_user_name_txt, childPosition);
				break;
			case 1://显示实时跑步的跑团title
	            convertView = LayoutInflater.from(context).inflate(
	                    R.layout.realtime_run_group_item, null);	
				 mGroupName = (TextView) convertView
			                .findViewById(R.id.real_time_group_user_name_txt);
				 mGroupRunCount = (TextView) convertView
			                .findViewById(R.id.real_time_group_count_txt);
				 RunningGroup rg = (RunningGroup) RealTimeRunManager.getInstance().getRunningObject(groupPosition, childPosition);
			        if (null != rg) {
			        	mGroupName.setText(rg.getName());
			        	mGroupRunCount.setText(rg.getMemberrunningcount()+"");
					}
		            tl = RealTimeRunManager.getInstance().getmRunningTypeList().get(groupPosition);
		            convertView.setTag(tl);
		            convertView.setTag(R.id.real_time_group_user_name_txt, childPosition);
					break;
			default:
				break;
			}
//        convertView.setBackgroundResource(R.drawable.menu_bg);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (null == RealTimeRunManager.getInstance().getRunningList(groupPosition))
        {
            return 0;
        }
        return RealTimeRunManager.getInstance().getRunningList(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return RealTimeRunManager.getInstance().getmRunningTypeList().get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return RealTimeRunManager.getInstance().getmRunningTypeList().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

	@SuppressLint("InflateParams")
	@Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.realtime_run_type_item, null);				
		}
        mListType = (TextView) convertView
                .findViewById(R.id.realtime_list_switch_txt);
        mListTypeSwitch = (ImageView) convertView
                .findViewById(R.id.realtime_list_switch_btn);
        String ls = RealTimeRunManager.getInstance().getmRunningTypeList().get(groupPosition);
        mListType.setText(ls);

        if (isExpanded) {
        	mListTypeSwitch.setImageResource(R.drawable.realtime_list_open);
//            convertView.setBackgroundColor(Color.GRAY);
        } else {
        	mListTypeSwitch.setImageResource(R.drawable.realtime_list_close);
//            convertView.setBackgroundColor(R.color.audi_color_light_gray);
        }
        convertView.setTag(ls);
        convertView.setTag(R.id.realtime_list_switch_txt, -1);
        convertView.setAlpha(0.5f);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

}