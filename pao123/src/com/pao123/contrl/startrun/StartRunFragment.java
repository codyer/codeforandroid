package com.pao123.contrl.startrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import com.pao123.contrl.menu.ui.ResideMenu;
import com.pao123.contrl.timecount.TimeCountActivity;
import com.pao123.www.R;


public class StartRunFragment extends Fragment {

    private View parentView;
//    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.startrun, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
//        MenuActivity parentActivity = (MenuActivity) getActivity();
//        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.btn_startrun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent intent = new Intent(); 
            	intent.setClass(getActivity(), TimeCountActivity.class);
                startActivity(intent);
            }
        });

        // add gesture operation's ignored views
//        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
//        resideMenu.addIgnoredView(ignored_view);
    }

}
