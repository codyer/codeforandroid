package com.pao123.bean;

import java.util.List;

public class History {
	private List<WorkOut> mWorkOuts;

	public WorkOut removeWorkOut(int index) {
		return mWorkOuts.remove(index);
	}
	public List<WorkOut> getmWorkOuts() {
		return mWorkOuts;
	}

	public void setmWorkOuts(List<WorkOut> mWorkOuts) {
		this.mWorkOuts = mWorkOuts;
	}
	
}
