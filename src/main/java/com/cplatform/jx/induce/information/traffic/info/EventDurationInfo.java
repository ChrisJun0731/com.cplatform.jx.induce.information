package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

public class EventDurationInfo {

	private int daySel;
	private int startDate;
	private int endDate;
	private int startTime;
	private int endTime;
	
	
	
	public int getDaySel() {
		return daySel;
	}



	public void setDaySel(int daySel) {
		this.daySel = daySel;
	}



	public int getStartDate() {
		return startDate;
	}



	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}



	public int getEndDate() {
		return endDate;
	}



	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}



	public int getStartTime() {
		return startTime;
	}



	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}



	public int getEndTime() {
		return endTime;
	}



	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}



	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
