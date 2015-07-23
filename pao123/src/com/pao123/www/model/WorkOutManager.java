package com.pao123.www.model;

import com.amap.api.location.AMapLocation;

public class WorkOutManager implements iWorkOutManager{

	private static WorkOutManager workOutManagerInstance = null;

	private static AMapLocation startLocation = null;
	private static AMapLocation lastLocation = null;
	
	private WorkOutManager() {
		super();
	}

	public static WorkOutManager getInstance() {
		if (workOutManagerInstance == null) {
			synchronized (WorkOutManager.class) {
				if (workOutManagerInstance == null) {
					workOutManagerInstance = new WorkOutManager();
				}
			}
		}
		return workOutManagerInstance;
	}

	public AMapLocation getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(AMapLocation startLocation) {
		WorkOutManager.startLocation = startLocation;
		//Start a workout, send to server
	}

	public AMapLocation getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(AMapLocation lastLocation) {
		WorkOutManager.lastLocation = lastLocation;
		//Workout location changed, send to server
	}
	public void setWorkOutStart() {
		//Workout is started, send to server
	}
	public void setWorkOutPause() {
		//Workout is paused, send to server
	}
	public void setWorkOutFinish() {
		//Workout is finished, send to server
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resetWorkOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pauseWorkOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void continueWorkOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishWorkOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getWorkOut() {
		// TODO Auto-generated method stub
		
	}
	
}
