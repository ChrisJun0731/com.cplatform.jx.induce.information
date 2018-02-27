package com.cplatform.jx.induce.information.traffic.info;

import com.alibaba.fastjson.JSON;

public class AliWeatherInfo {

	private String code;
	private AliWeatherDataInfo data;
	private String msg;
	public String getCode() {
		return code;
	}
	public AliWeatherDataInfo getData() {
		return data;
	}
	public String getMsg() {
		return msg;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setData(AliWeatherDataInfo data) {
		this.data = data;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
}
