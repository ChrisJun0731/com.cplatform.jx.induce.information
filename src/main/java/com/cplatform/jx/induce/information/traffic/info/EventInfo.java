package com.cplatform.jx.induce.information.traffic.info;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class EventInfo {

	private String sign;
	private List<EventDataInfo> data;
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public List<EventDataInfo> getData() {
		return data;
	}
	public void setData(List<EventDataInfo> data) {
		this.data = data;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
