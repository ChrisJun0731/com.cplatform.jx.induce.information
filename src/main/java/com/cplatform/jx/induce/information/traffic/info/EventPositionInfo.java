package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

public class EventPositionInfo {

	private String roadName;
	private String direction;
	private int locType;
	private String[] loc;
	private int [] lanes;
//	private int [] tollLanes;
	
	
	public String getRoadName() {
		return roadName;
	}


	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}


	public String getDirection() {
		return direction;
	}


	public void setDirection(String direction) {
		this.direction = direction;
	}


	public int getLocType() {
		return locType;
	}


	public void setLocType(int locType) {
		this.locType = locType;
	}


	public String[] getLoc() {
		return loc;
	}


	public void setLoc(String[] loc) {
		this.loc = loc;
	}


	public int[] getLanes() {
		return lanes;
	}


	public void setLanes(int[] lanes) {
		this.lanes = lanes;
	}


//	public int[] getTollLanes() {
//		return tollLanes;
//	}
//
//
//	public void setTollLanes(int[] tollLanes) {
//		this.tollLanes = tollLanes;
//	}


	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
