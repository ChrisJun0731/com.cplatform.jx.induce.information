package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

public class InduceTimeResponse {

	private String sid;
	private StatusInfo status;
	private TimeDataInfo data;
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public StatusInfo getStatus() {
		return status;
	}
	public void setStatus(StatusInfo status) {
		this.status = status;
	}
	public TimeDataInfo getData() {
		return data;
	}
	public void setData(TimeDataInfo data) {
		this.data = data;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}
