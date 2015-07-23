package com.pao123.common;

import com.pao123.www.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;


public class LodingView {
	private static LodingView lodingViewInstance;
    private LinearLayout mLoadingView;
	
	public static LodingView getInstance() {
		if (lodingViewInstance == null) {
			synchronized (LodingView.class) {
				if (lodingViewInstance == null) {
					lodingViewInstance = new LodingView();
				}
			}
		}
		return lodingViewInstance;
	}
	@SuppressLint("InflateParams")
	public void showLoadingInfo(Activity act,final Context ctx,Boolean status)
    {
        if (null == mLoadingView)
        {
            mLoadingView = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.common_loading, null);
            act.addContentView(mLoadingView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            mLoadingView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    Toast.makeText(ctx, "Please Wait !", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (status)
        {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        else
        {
            mLoadingView.setVisibility(View.GONE);
            mLoadingView.removeAllViews();
            mLoadingView = null;
        }

    }
}
