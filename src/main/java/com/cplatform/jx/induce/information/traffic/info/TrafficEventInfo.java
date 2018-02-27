package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

/**
 * 
 * 交通事件实体类. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月20日 下午1:45:18
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */

public class TrafficEventInfo {

	private int id;
	private String type;
	private String loc;
	private String lanes;
	private String startTime;
	private String endTime;
	
	private String desc;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	
	public String getLanes() {
		return lanes;
	}
	public void setLanes(String lanes) {
		this.lanes = lanes;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}
