package com.pao123.www.model;


public interface iWorkOutManager {
    public enum status {
    	idle,
        running,
        paused,
        finished
        }
	public int getStatus();
	public void resetWorkOut() ;
	public void pauseWorkOut() ;
	public void continueWorkOut() ;
	public void finishWorkOut() ;
	public void getWorkOut() ;	
}
